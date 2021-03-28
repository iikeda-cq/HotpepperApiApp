package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreWebViewBinding

class StoreWebViewFragment : Fragment() {
    lateinit var binding: FragmentStoreWebViewBinding

    private val url: String
        get() = checkNotNull(arguments?.getString(URL))

    private val name: String
        get() = checkNotNull(arguments?.getString(NAME))

    companion object {
        private const val URL = "url"
        private const val NAME = "name"
        fun newInstance(name: String,url: String): StoreWebViewFragment {
            return StoreWebViewFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, name)
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

        requireActivity().title = name
        binding.storeWebView.loadUrl(url)
        return binding.root
    }

}
