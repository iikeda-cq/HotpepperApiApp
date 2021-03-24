package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.codelab.hotpepperapiapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_list -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, StoreListFragment())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_map -> {

                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_favorite -> {

                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        setContentView(binding.root)
    }
}
