package com.google.codelab.hotpepperapiapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.RealmClient.fetchStores
import com.google.codelab.hotpepperapiapp.databinding.FragmentFavoriteStoreBinding
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragment
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.viewModel.FavoriteStoreViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.realm.Realm

class FavoriteStoreFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteStoreBinding
    private lateinit var viewModel: FavoriteStoreViewModel

    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val favoriteStoreList: MutableList<NearStore> = ArrayList()
    private var isMoreLoad = true

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

        viewModel.fetchLocalStoresId()

        viewModel.favoriteStoresList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { stores ->
                if (stores.results.totalPages < 20) {
                    isMoreLoad = false
                }
                stores.results.store.map { favoriteStoreList.add(it) }
                groupAdapter.update(favoriteStoreList.map { StoreItem(it, requireContext()) })
            }

        viewModel.errorStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { failure ->
                Snackbar.make(view, failure.message, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry) {
                        viewModel.favoriteStoreIds?.let { ids -> viewModel.fetchFavoriteStores(ids) }
                    }.show()
            }

        groupAdapter.setOnItemClickListener(onItemClickListener)

        // 最下部までスクロールした際の制御
        binding.recyclerViewFavorite.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && isMoreLoad) {
                    viewModel.fetchLocalStoresId()
                    viewModel.favoriteStoreIds?.let { viewModel.fetchFavoriteStores(it) }
                }
            }
        })
    }
}
