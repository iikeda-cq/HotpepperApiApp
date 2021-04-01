package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.codelab.hotpepperapiapp.databinding.FragmentFavoriteStoreBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import io.realm.Realm

class FavoriteStoreFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteStoreBinding
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private var dataSet: MutableList<Store> = ArrayList()
    private lateinit var realm: Realm

    private val onItemClickListener = OnItemClickListener { item, _ ->
        // どのitemがクリックされたかindexを取得
        val index = groupAdapter.getAdapterPosition(item)

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.frameLayout,
                StoreWebViewFragment.newInstance(
                    dataSet[index].name,
                    dataSet[index].url,
                    dataSet[index].price,
                    false
                )
            )
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteStoreBinding.inflate(inflater)
        requireActivity().setTitle(R.string.navigation_favorite)
        realm = Realm.getDefaultInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewFavorite.apply {
            adapter = groupAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        dataSet = fetchRealmData()
        groupAdapter.update(dataSet.map { StoreItem(it) })
        groupAdapter.setOnItemClickListener(onItemClickListener)
    }

    private fun fetchRealmData(): MutableList<Store> {
        val realmResults = realm.where(Store::class.java)
            .distinct("name")
            .findAll()

        dataSet.clear()
        realmResults.map { store ->
            val data = Store()
            data.name = store.name
            data.url = store.url
            data.price = store.price

            dataSet.add(data)
        }
        return dataSet
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
