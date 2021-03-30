package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.codelab.hotpepperapiapp.databinding.FragmentFavoriteStoreBinding

class FavoriteStoreFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteStoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteStoreBinding.inflate(inflater)
        requireActivity().setTitle(R.string.navigation_favorite)
        return binding.root
    }
}
