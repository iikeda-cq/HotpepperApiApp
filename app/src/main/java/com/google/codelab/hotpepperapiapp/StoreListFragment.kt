package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.codelab.hotpepperapiapp.databinding.CellStoreBinding
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.viewbinding.BindableItem


class StoreListFragment : Fragment() {
    private lateinit var binding: FragmentStoreListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val groupAdapter = GroupAdapter<GroupieViewHolder>()
        binding.recyclerView.adapter = groupAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        groupAdapter.update(createTestData().map { StoreItem(it) })
    }

}

fun createTestData(): List<Store> {
    val dataSet: MutableList<Store> = ArrayList()
    var i = 1
    while (i <= 10) {
        val data = Store()
        data.image = R.drawable.store_image
        data.name = "居酒屋"
        data.price = "4000"

        dataSet.add(data)
        i += 1
    }
    return dataSet
}
