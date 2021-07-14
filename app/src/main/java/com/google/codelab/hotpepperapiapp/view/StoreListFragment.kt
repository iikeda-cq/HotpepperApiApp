package com.google.codelab.hotpepperapiapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.codelab.hotpepperapiapp.CurrentLatLng
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreListBinding
import com.google.codelab.hotpepperapiapp.ext.showAlertDialog
import com.google.codelab.hotpepperapiapp.ext.showFragment
import com.google.codelab.hotpepperapiapp.model.response.NearStore
import com.google.codelab.hotpepperapiapp.viewModel.StoreListViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

@AndroidEntryPoint
class StoreListFragment : Fragment() {
    private lateinit var binding: FragmentStoreListBinding
    private val viewModel: StoreListViewModel by viewModels()
    private val groupAdapter = GroupAdapter<GroupieViewHolder>()
    private val storeList: MutableList<NearStore> = ArrayList()
    private var startPage = 1
    private var isMoreLoad = true
    private val disposables = CompositeDisposable()

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
        binding.viewModel = viewModel
        requireActivity().setTitle(R.string.view_list)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (CurrentLatLng.lat == null || CurrentLatLng.lng == null) {
            requireContext().showAlertDialog(
                R.string.no_locations_title,
                R.string.no_locations_message,
                parentFragmentManager
            )
        }

        val lat = CurrentLatLng.lat ?: return
        val lng = CurrentLatLng.lng ?: return

        binding.recyclerView.apply {
            adapter = groupAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        }

        if (storeList.isEmpty()) {
            viewModel.fetchStores(lat, lng)
        }

        viewModel.storesList
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { stores ->
                if (stores.results.totalPages < 20) {
                    isMoreLoad = false
                }
                storeList.addAll(stores.results.store)
                groupAdapter.update(storeList.map { StoreItem(it, requireContext()) })

                startPage += stores.results.totalPages
            }.addTo(disposables)

        viewModel.errorStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { failure ->
                Snackbar.make(view, failure.message.message, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) {
                        failure.retry
                    }.show()
            }.addTo(disposables)

        groupAdapter.setOnItemClickListener(onItemClickListener)

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && isMoreLoad) {
                    viewModel.fetchStores(
                        lat,
                        lng,
                        startPage
                    )
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }
}

