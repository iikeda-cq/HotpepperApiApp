package com.google.codelab.hotpepperapiapp.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.codelab.hotpepperapiapp.R

object FragmentExt {
    fun showFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
