package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener

class StoreListFragment : Fragment() {
    private lateinit var binding: FragmentStoreListBinding
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val dataSet: MutableList<Store> = ArrayList()

    private val onItemClickListener = OnItemClickListener { item, view ->
        // どのitemがクリックされたかindexを取得
        val index = groupAdapter.getAdapterPosition(item)

        parentFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, StoreWebViewFragment.newInstance(dataSet[index].name,dataSet[index].url))
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreListBinding.inflate(layoutInflater)
        requireActivity().setTitle(R.string.view_list)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = groupAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)

        groupAdapter.update(createTestData().map { StoreItem(it) })
        groupAdapter.setOnItemClickListener(onItemClickListener)
    }

    fun createTestData(): List<Store> {
        var i = 1
        while (i <= 10) {
            val data = Store()
            data.image = R.drawable.store_image
            data.name = "居酒屋$i"
            data.price = "約4000円"
            data.genre = "イタリアン"
            data.url = "https://www.hotpepper.jp/strJ001219042/"

            dataSet.add(data)
            i += 1
        }
        return dataSet
    }
}

