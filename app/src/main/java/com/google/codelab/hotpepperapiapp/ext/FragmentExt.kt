package com.google.codelab.hotpepperapiapp.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.codelab.hotpepperapiapp.R

fun Fragment.showFragment(fragmentManager: FragmentManager, setToRoot: Boolean = false) {
    if (setToRoot) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.frameLayout, this)
            .commit()
    } else {
        fragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.frameLayout, this)
            .addToBackStack(null)
            .commit()
    }
}
