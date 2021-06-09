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
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreListBinding
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragment
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.viewModel.StoreListViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class StoreListFragment : Fragment() {
    private lateinit var binding: FragmentStoreListBinding
    private lateinit var viewModel: StoreListViewModel
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val storeList: MutableList<NearStore> = ArrayList()
    private var startPage = 1
    private var isMoreLoad = true

    private val onItemClickListener = OnItemClickListener { item, _ ->
        // どのitemがクリックされたかindexを取得
        val index = groupAdapter.getAdapterPosition(item)

        StoreWebViewFragment.newInstance(
            storeList[index].id,
            storeList[index].urls.url
        ).showFragment(parentFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreListBinding.inflate(layoutInflater)
        requireActivity().setTitle(R.string.view_list)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        viewModel = ViewModelProviders.of(this).get(StoreListViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = groupAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        if (storeList.isEmpty()) {
            viewModel.fetchStores(MainActivity.lat ?: return, MainActivity.lng ?: return)
        }

        viewModel.storesList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { stores ->
                if (stores.results.totalPages < 20) {
                    isMoreLoad = false
                }
                stores.results.store.map { storeList.add(it) }
                groupAdapter.update(storeList.map { StoreItem(it, requireContext()) })

                startPage += stores.results.totalPages
            }

        viewModel.errorStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { failure ->
                Snackbar.make(view, failure.message, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.retry) {
                        viewModel.fetchStores(
                            MainActivity.lat!!,
                            MainActivity.lng!!,
                            startPage
                        )
                    }.show()
            }

        groupAdapter.setOnItemClickListener(onItemClickListener)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && isMoreLoad) {
                    viewModel.fetchStores(
                        MainActivity.lat ?: return,
                        MainActivity.lng ?: return,
                        startPage
                    )
                }
            }
        })
    }
}

