package com.google.codelab.hotpepperapiapp.ext

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.google.codelab.hotpepperapiapp.R
import com.google.codelab.hotpepperapiapp.view.MapsFragment

fun Context.showAlertDialog(
    @StringRes title: Int,
    @StringRes message: Int,
    fragmentManager: FragmentManager
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { _, _ ->
            MapsFragment.newInstance().showFragment(fragmentManager, true)
        }
        .show()
}
