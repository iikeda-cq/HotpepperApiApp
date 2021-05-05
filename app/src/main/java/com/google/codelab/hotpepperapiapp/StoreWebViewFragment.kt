package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.codelab.hotpepperapiapp.RealmClient.addStore
import com.google.codelab.hotpepperapiapp.RealmClient.deleteStore
import com.google.codelab.hotpepperapiapp.RealmClient.fetchFirstStore
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreWebViewBinding
import io.realm.Realm

class StoreWebViewFragment : Fragment() {
    private lateinit var binding: FragmentStoreWebViewBinding
    private lateinit var realm: Realm

    private val storeId: String
        get() = checkNotNull(arguments?.getString(STORE_ID))

    private val url: String
        get() = checkNotNull(arguments?.getString(URL))

    var isFavorite = false

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
        binding.isFab = isFavorite

//        requireActivity().title = name
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        realm = Realm.getDefaultInstance()
        binding.storeWebView.loadUrl(url)


        // すでにお気に入りに追加済みかどうかをチェックする
        checkAlreadyAdd()

        binding.fabFavorite.setOnClickListener {
            changeFavoriteStore(isFavorite)
        }

        return binding.root
    }

    private fun changeFavoriteStore(isFav: Boolean) {
        if (isFav) {
            deleteStore(realm, storeId)
            Toast.makeText(requireContext(), R.string.delete_favorite, Toast.LENGTH_SHORT).show()
        } else {
            addStore(realm, storeId)
            Toast.makeText(requireContext(), R.string.add_favorite, Toast.LENGTH_SHORT).show()
        }
        binding.isFab = !isFav
        isFavorite = !isFav
    }

    private fun checkAlreadyAdd() {
        val store = fetchFirstStore(realm, storeId)

        if (store != null){
            binding.apply {
                isFavorite = true
                binding.isFab = true
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
        realm.close()
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}
