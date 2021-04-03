package com.google.codelab.hotpepperapiapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentExt {
    fun showFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.popBackStack()
        fragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }
}
