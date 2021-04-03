package com.google.codelab.hotpepperapiapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

object FragmentExt {
    fun showFragment(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.popBackStack()
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    fun showFragmentBackStack(fragmentManager: FragmentManager, fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
