package com.setjy.practiceapp.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide

fun Fragment.hideKeyboard() {
    val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.getImageViewFromUrl(url: String, view: ImageView) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .into(view)
}
fun getImageViewFromUrl(view:View,url: String, imageView: ImageView) {
    Glide.with(view)
        .load(url)
        .centerCrop()
        .into(imageView)
}