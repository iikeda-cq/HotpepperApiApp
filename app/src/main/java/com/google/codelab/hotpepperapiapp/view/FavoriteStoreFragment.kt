package com.google.codelab.hotpepperapiapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.FragmentFavoriteStoreBinding
import com.google.codelab.hotpepperapiapp.ext.showFragment
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.viewModel.FavoriteStoreViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

class FavoriteStoreFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteStoreBinding
    private lateinit var viewModel: FavoriteStoreViewModel

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val favoriteStoreList: MutableList<NearStore> = ArrayList()
    private var isMoreLoad = true
    private val disposables = CompositeDisposable()

    private val onItemClickListener = OnItemClickListener { item, _ ->
        // どのitemがクリックされたかindexを取得
        val index = groupAdapter.getAdapterPosition(item)

        StoreWebViewFragment.newInstance(
            favoriteStoreList[index].id,
            favoriteStoreList[index].urls.url
        ).showFragment(parentFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteStoreBinding.inflate(inflater)
        viewModel = ViewModelProviders.of(this).get(FavoriteStoreViewModel::class.java)
        binding.isLoading = false

        requireActivity().setTitle(R.string.navigation_favorite)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFavorite.apply {
            adapter = groupAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        /**
         * WebViewからお気に入り画面に戻るとsetup()が走り、同じ内容が複数RecyclerViewに表示されてしまったため、
         * favoriteStoreListが空の時だけ実行するようにしている。
         * ただ、これのせいでお気に入り画面→店舗一覧で適当なお店をお気に入り登録する→お気に入り画面と遷移しても最新のおきに入り情報が反映されません。。。
          */
        if (favoriteStoreList.isEmpty()) {
            viewModel.setup()
            binding.isLoading = true
        }

        viewModel.favoriteStoresList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { stores ->
                if (stores.results.totalPages < 20) {
                    isMoreLoad = false
                }
                favoriteStoreList.addAll(stores.results.store)
                groupAdapter.update(favoriteStoreList.map { StoreItem(it, requireContext()) })
                binding.isLoading = false
            }.addTo(disposables)

        viewModel.hasNoFavoriteStores
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                binding.isLoading = false

                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.no_favorite_title)
                    .setMessage(R.string.no_favorite_message)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        MapsFragment.newInstance().showFragment(parentFragmentManager, true)
                    }
                    .show()
            }.addTo(disposables)

        viewModel.errorStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { failure ->
                binding.isLoading = false
                Snackbar.make(view, failure.message.message, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry) {
                        failure.retry
                    }.show()
            }.addTo(disposables)

        groupAdapter.setOnItemClickListener(onItemClickListener)

        // 最下部までスクロールした際の制御
        binding.recyclerViewFavorite.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && isMoreLoad) {
                    binding.isLoading = true
                    viewModel.fetchFavoriteStores()
                }
            }
        })
    }
}
