package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreWebViewBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class StoreWebViewFragment : Fragment() {
    private lateinit var binding: FragmentStoreWebViewBinding
    private lateinit var realm: Realm

    private val storeId: String
        get() = checkNotNull(arguments?.getString(STORE_ID))

    private val url: String
        get() = checkNotNull(arguments?.getString(URL))

    private val name: String
        get() = checkNotNull(arguments?.getString(NAME))

    private val price: String
        get() = checkNotNull(arguments?.getString(PRICE))

    private val flag: Boolean
        get() = checkNotNull(arguments?.getBoolean(FLAG))

    companion object {
        private const val STORE_ID = "store_id"
        private const val URL = "url"
        private const val NAME = "name"
        private const val PRICE = "price"
        private const val FLAG = "flag"
        fun newInstance(
            storeId: String,
            name: String,
            url: String,
            price: String,
            flag: Boolean
        ): StoreWebViewFragment {
            return StoreWebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(STORE_ID, storeId)
                    putString(NAME, name)
                    putString(URL, url)
                    putString(PRICE, price)
                    putBoolean(FLAG, flag)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreWebViewBinding.inflate(inflater)

        requireActivity().title = name
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        realm = Realm.getDefaultInstance()
        binding.storeWebView.loadUrl(url)

        if (flag) {
            binding.fabDelete.isVisible = false
        } else {
            binding.fabFavorite.isVisible = false
        }

        // すでにお気に入りに追加済みかどうかをチェックする
        checkAlreadyAdd()

        binding.fabFavorite.setOnClickListener {
            realm.executeTransaction {
                val maxId = realm.where<Store>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val store = realm.createObject<Store>(nextId)

                store.storeId = storeId
                store.name = name
                store.url = url
                store.price = price
            }
            Toast.makeText(requireContext(), R.string.add_favorite, Toast.LENGTH_SHORT).show()

            // 複数回タップできないように設定
            it.isVisible = false
            binding.fabDelete.isVisible = true
        }

        binding.fabDelete.setOnClickListener {
            val target = realm.where(Store::class.java)
                .equalTo("storeId", storeId)
                .findAll()

            realm.executeTransaction {
                target.deleteFromRealm(0)
            }

            Toast.makeText(requireContext(), R.string.delete_favorite, Toast.LENGTH_SHORT).show()
            it.isVisible = false
            binding.fabFavorite.isVisible = true
        }

        return binding.root
    }

    private fun checkAlreadyAdd() {
        val realmResults = realm.where(Store::class.java)
            .distinct("storeId")
            .findAll()

        realmResults.map {
            if (it.storeId == storeId) {
                binding.fabFavorite.isVisible = false
                binding.fabDelete.isVisible = true
                return
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
