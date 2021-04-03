package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreWebViewBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class StoreWebViewFragment : Fragment() {
    lateinit var binding: FragmentStoreWebViewBinding
    lateinit var realm: Realm

    private val url: String
        get() = checkNotNull(arguments?.getString(URL))

    private val name: String
        get() = checkNotNull(arguments?.getString(NAME))

    private val price: String
        get() = checkNotNull(arguments?.getString(PRICE))

    private val flag: Boolean
        get() = checkNotNull(arguments?.getBoolean(FLAG))

    companion object {
        private const val URL = "url"
        private const val NAME = "name"
        private const val PRICE = "price"
        private const val FLAG = "flag"
        fun newInstance(
            name: String,
            url: String,
            price: String,
            flag: Boolean
        ): StoreWebViewFragment {
            return StoreWebViewFragment().apply {
                arguments = Bundle().apply {
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
                .equalTo("name", name)
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
            .distinct("name")
            .findAll()

        realmResults.map {
            if (it.name == name) {
                binding.fabFavorite.isVisible = false
                binding.fabDelete.isVisible = true
                return
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
