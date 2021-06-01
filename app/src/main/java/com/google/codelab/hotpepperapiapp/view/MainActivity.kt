package com.google.codelab.hotpepperapiapp.view

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.databinding.ActivityMainBinding
import com.google.codelab.hotpepperapiapp.ext.FragmentExt.showFragment
import com.google.codelab.hotpepperapiapp.ext.IntExt.actionBarColorToStatusBarColor

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_list -> {
                    showFragment(supportFragmentManager, StoreListFragment())
                    true
                }
                R.id.navigation_map -> {
                    showFragment(supportFragmentManager, MapsFragment())
                    true
                }
                R.id.navigation_favorite -> {
                    showFragment(supportFragmentManager, FavoriteStoreFragment())
                    true
                }
                else -> false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        // ステータスバーとアクションバーの色を合わせる処理
        val actionBarColor = Color.parseColor("#FF018786")
        this.window.statusBarColor = actionBarColor.actionBarColorToStatusBarColor()

        binding.navigation.apply {
            setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
            selectedItemId = R.id.navigation_map
        }

        setContentView(binding.root)
    }

    companion object {
        var lat: Double? = null
        var lng: Double? = null
    }
}
