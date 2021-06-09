package com.google.codelab.hotpepperapiapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.RealmClient
import com.google.codelab.hotpepperapiapp.RealmClient.addStore
import com.google.codelab.hotpepperapiapp.RealmClient.deleteStore
import com.google.codelab.hotpepperapiapp.RealmClient.fetchFirstStore
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreWebViewBinding
import com.google.codelab.hotpepperapiapp.viewModel.FavoriteStoreViewModel
import com.google.codelab.hotpepperapiapp.viewModel.StoreWebViewViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.realm.Realm

class StoreWebViewFragment : Fragment() {
    private lateinit var binding: FragmentStoreWebViewBinding
    private lateinit var viewModel: StoreWebViewViewModel

    private val storeId: String
        get() = checkNotNull(arguments?.getString(STORE_ID))

    private val url: String
        get() = checkNotNull(arguments?.getString(URL))

    private var isFavorite = false

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
        viewModel = ViewModelProviders.of(this).get(StoreWebViewViewModel::class.java)

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        requireActivity().setTitle(R.string.view_store_detail)
        setHasOptionsMenu(true)

        binding.viewModel = viewModel
        binding.isFab = isFavorite

        binding.storeWebView.loadUrl(url)

        // すでにお気に入りに追加済みかどうかをチェックする
        checkAlreadyAdd()

        viewModel.onClickFab
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy { changeFavoriteStore(isFavorite) }

        return binding.root
    }

    private fun changeFavoriteStore(isFav: Boolean) {
        Realm.getDefaultInstance().use { realm ->
            if (isFav) {
                realm.deleteStore(storeId)
                Toast.makeText(requireContext(), R.string.delete_favorite, Toast.LENGTH_SHORT)
                    .show()
            } else {
                realm.addStore(storeId)
                Toast.makeText(requireContext(), R.string.add_favorite, Toast.LENGTH_SHORT).show()
            }
        }
        binding.isFab = !isFav
        isFavorite = !isFav
    }

    private fun checkAlreadyAdd() {
        Realm.getDefaultInstance().use { realm ->
            val store = realm.fetchFirstStore(storeId)

            if (store != null) {
                binding.apply {
                    isFavorite = true
                    binding.isFab = true
                }
            }
        }
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
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
