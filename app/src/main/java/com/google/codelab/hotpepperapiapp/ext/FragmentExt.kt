package com.google.codelab.hotpepperapiapp.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.codelab.hotpepperapiapp.R

object FragmentExt {

    fun Fragment.showFragment(fragmentManager: FragmentManager) {
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.frameLayout, this)
            .addToBackStack(null)
            .commit()
    }
}
