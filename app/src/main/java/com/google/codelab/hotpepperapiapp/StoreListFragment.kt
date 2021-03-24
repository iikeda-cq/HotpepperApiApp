package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.codelab.hotpepperapiapp.databinding.ActivityMainBinding
import com.google.codelab.hotpepperapiapp.databinding.FragmentStoreListBinding

class StoreListFragment : Fragment() {
    private lateinit var binding: FragmentStoreListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStoreListBinding.inflate(layoutInflater)
        return binding.root
    }

}
