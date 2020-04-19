package com.rohin.jomrun.adapters;

import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import androidx.databinding.BindingAdapter;

public class CustomBindingAdapter {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if (!TextUtils.isEmpty(imageUrl))
            Picasso.get()
                    .load( imageUrl)
                    .into(view);
        else
            view.setImageResource(0);
    }
}