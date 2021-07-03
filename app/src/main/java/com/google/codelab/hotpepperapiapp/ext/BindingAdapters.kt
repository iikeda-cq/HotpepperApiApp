package com.google.codelab.hotpepperapiapp.ext

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
            .circleCrop()
            .error(defaultImage)
            .into(this)
    }
}
