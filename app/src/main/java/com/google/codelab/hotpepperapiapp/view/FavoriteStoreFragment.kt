package com.google.codelab.hotpepperapiapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.RealmClient.fetchStores
import com.google.codelab.hotpepperapiapp.StoreMapper
import com.google.codelab.hotpepperapiapp.databinding.FragmentFavoriteStoreBinding
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragment
import com.google.codelab.hotpepperapiapp.model.StoreModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import io.realm.Realm

class FavoriteStoreFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteStoreBinding
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val dataSet: MutableList<StoreModel> = ArrayList()

    private val onItemClickListener = OnItemClickListener { item, _ ->
        // どのitemがクリックされたかindexを取得
        val index = groupAdapter.getAdapterPosition(item)

        StoreWebViewFragment.newInstance(
            dataSet[index].storeId,
            dataSet[index].name,
            dataSet[index].url,
            dataSet[index].price,
        ).showFragment(parentFragmentManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteStoreBinding.inflate(inflater)
        requireActivity().setTitle(R.string.navigation_favorite)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFavorite.apply {
            adapter = groupAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        fetchRealmData()
        groupAdapter.update(dataSet.map { StoreItem(it) })
        groupAdapter.setOnItemClickListener(onItemClickListener)
    }

    private fun fetchRealmData() {
        Realm.getDefaultInstance().use { realm ->
            val stores = realm.fetchStores()

            dataSet.clear()

            StoreMapper.transform(stores).forEach { store ->
                dataSet.add(store)
            }
        }
    }
}
