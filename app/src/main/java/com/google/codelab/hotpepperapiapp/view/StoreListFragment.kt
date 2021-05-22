package com.google.codelab.hotpepperapiapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.Shop
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreListBinding
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragmentBackStack
import com.google.codelab.hotpepperapiapp.viewModel.StoreListViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener

class StoreListFragment : Fragment() {
    private lateinit var binding: FragmentStoreListBinding
    private lateinit var viewModel: StoreListViewModel
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val storeList: MutableList<Shop> = ArrayList()

    private val onItemClickListener = OnItemClickListener { item, _ ->
        // どのitemがクリックされたかindexを取得
        val index = groupAdapter.getAdapterPosition(item)

        showFragmentBackStack(
            parentFragmentManager,
            StoreWebViewFragment.newInstance(
                storeList[index].id,
                storeList[index].urls.url
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreListBinding.inflate(layoutInflater)
        requireActivity().setTitle(R.string.view_list)

        viewModel = StoreListViewModel()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.apply {
            adapter = groupAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        viewModel.fetchStores(MainActivity.lat ?: return, MainActivity.lng ?: return)

        viewModel.storeRepos.observe(viewLifecycleOwner, { stores ->
            groupAdapter.update(stores.results.store.map { StoreItem(it, requireContext()) })
            stores.results.store.map { storeList.add(it) }
            groupAdapter.setOnItemClickListener(onItemClickListener)
        })
    }
}

