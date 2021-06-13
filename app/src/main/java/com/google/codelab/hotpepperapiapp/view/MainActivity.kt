package com.google.codelab.hotpepperapiapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.ActivityMainBinding
import com.google.codelab.hotpepperapiapp.ext.actionBarColorToStatusBarColor
import com.google.codelab.hotpepperapiapp.ext.showFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val storeListFragment = StoreListFragment()
    private val mapsFragment = MapsFragment()
    private val favoriteStoreFragment = FavoriteStoreFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            if (binding.navigation.selectedItemId == item.itemId) {
                return@OnNavigationItemSelectedListener true
            }
            when (item.itemId) {
                R.id.navigation_list -> {
                    storeListFragment.showFragment(supportFragmentManager)
                    true
                }
                R.id.navigation_map -> {
                    mapsFragment.showFragment(supportFragmentManager)
                    true
                }
                R.id.navigation_favorite -> {
                    favoriteStoreFragment.showFragment(supportFragmentManager)
                    true
                }
                else -> false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // ステータスバーとアクションバーの色を合わせる処理
        val actionBarColor = getColor(R.color.actionBar)
        this.window.statusBarColor = actionBarColor.actionBarColorToStatusBarColor()

        binding.navigation.apply {
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_map
        }

        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0) {
            finish()
        }
    }
}
