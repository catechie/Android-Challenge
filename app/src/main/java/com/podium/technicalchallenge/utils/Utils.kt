package com.podium.technicalchallenge.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.podium.technicalchallenge.R

@BindingAdapter("android:loadImg")
fun ImageView.loadImage(uri: String?){
    Glide.with(context)
        .load(uri)
        .apply(
            RequestOptions().placeholder(
                R.drawable.loading).error(R.mipmap.ic_launcher_round).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .transform(CenterCrop(), RoundedCorners(15))) .into(this)
}