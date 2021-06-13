package com.google.codelab.hotpepperapiapp.view

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragment
import com.google.codelab.hotpepperapiapp.ext.IntExt.actionBarColorToStatusBarColor
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_list -> {
                    StoreListFragment().showFragment(supportFragmentManager)
                    true
                }
                R.id.navigation_map -> {
                    MapsFragment().showFragment(supportFragmentManager)
                    true
                }
                R.id.navigation_favorite -> {
                    FavoriteStoreFragment().showFragment(supportFragmentManager)
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
}
