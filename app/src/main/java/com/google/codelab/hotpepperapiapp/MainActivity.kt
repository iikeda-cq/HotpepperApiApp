package com.google.codelab.hotpepperapiapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.codelab.hotpepperapiapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_list -> {
                    showFragment(StoreListFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_map -> {
                    showFragment(MapsFragment())
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

        binding.navigation.apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_map
        }

        setContentView(binding.root)
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}
