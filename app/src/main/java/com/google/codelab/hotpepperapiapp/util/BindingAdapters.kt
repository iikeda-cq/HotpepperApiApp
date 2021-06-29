package com.google.codelab.hotpepperapiapp.util

import android.graphics.drawable.Drawable
import android.webkit.WebView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("url")
    fun WebView.loadUrlForBinding(url: String?) {
        if (url.isNullOrEmpty()) return

        loadUrl(url)
    }

    @JvmStatic
    @BindingAdapter("imageUrl", "defaultImage")
    fun ImageView.loadImageUrl(url: String?, defaultImage: Drawable) {
        Glide.with(context)
            .load(url)
            .error(defaultImage)
            .into(this)
    }
}
