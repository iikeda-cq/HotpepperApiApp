package com.google.codelab.hotpepperapiapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreWebViewBinding
import com.google.codelab.hotpepperapiapp.viewModel.StoreWebViewViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

@AndroidEntryPoint
class StoreWebViewFragment : Fragment() {
    private lateinit var binding: FragmentStoreWebViewBinding
    private val viewModel: StoreWebViewViewModel by viewModels()

    private val storeId: String
        get() = checkNotNull(arguments?.getString(STORE_ID))

    private val url: String
        get() = checkNotNull(arguments?.getString(URL))

    private val disposables = CompositeDisposable()

    companion object {
        private const val STORE_ID = "store_id"
        private const val URL = "url"
        fun newInstance(
            storeId: String,
            url: String
        ): StoreWebViewFragment {
            return StoreWebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(STORE_ID, storeId)
                    putString(URL, url)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreWebViewBinding.inflate(inflater)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requireActivity().setTitle(R.string.view_store_detail)
        setHasOptionsMenu(true)

        binding.viewModel = viewModel
        binding.url = url
        binding.storeId = storeId

        viewModel.addFavoriteStore
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                Toast.makeText(requireContext(), R.string.add_favorite, Toast.LENGTH_SHORT).show()
            }.addTo(disposables)

        viewModel.deleteFavoriteStore
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                Toast.makeText(requireContext(), R.string.delete_favorite, Toast.LENGTH_SHORT)
                    .show()
            }.addTo(disposables)

        viewModel.errorStream
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy {
                Toast.makeText(requireContext(), R.string.unexpected_error, Toast.LENGTH_SHORT)
                    .show()
            }.addTo(disposables)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchFavoriteStore(storeId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                parentFragmentManager.popBackStack()
                true
            }
            else -> {
                true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
