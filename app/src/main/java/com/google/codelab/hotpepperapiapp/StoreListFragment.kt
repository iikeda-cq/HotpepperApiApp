package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.codelab.hotpepperapiapp.FragmentExt.showFragmentBackStack
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreListBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener

class StoreListFragment : Fragment() {
    private lateinit var binding: FragmentStoreListBinding
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val dataSet: MutableList<Store> = ArrayList()

    private val onItemClickListener = OnItemClickListener { item, _ ->
        // どのitemがクリックされたかindexを取得
        val index = groupAdapter.getAdapterPosition(item)

        showFragmentBackStack(
            parentFragmentManager,
            StoreWebViewFragment.newInstance(
                dataSet[index].storeId,
                dataSet[index].name,
                dataSet[index].url,
                dataSet[index].price,
            )
        )
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

        binding.recyclerView.apply {
            adapter = groupAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        groupAdapter.update(createTestData().map { StoreItem(it) })
        groupAdapter.setOnItemClickListener(onItemClickListener)
    }

    fun createTestData(): List<Store> {
        var i = 1
        while (i <= 10) {
            val data = Store()

            data.apply {
                storeId = i.toString()
                image = R.drawable.store_image
                name = "[$i]クラフトビール×個室肉バル クラフトマーケット 海浜幕張店"
                price = "2001～3000円"
                genre = "居酒屋"
                url = "https://www.hotpepper.jp/strJ001219042/"
            }

            dataSet.add(data)
            i += 1
        }
        return dataSet
    }
}

