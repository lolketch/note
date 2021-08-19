package com.example.note

import android.widget.ImageView
import com.squareup.picasso.Picasso

fun ImageView.downloadAndSetImage(url: String) {
    /* Функция раширения ImageView, скачивает и устанавливает картинку*/
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.default_photo)
        .into(this)
}