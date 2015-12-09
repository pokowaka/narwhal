package com.yayaway.narwhal.com.yayaway.narwhal.ui.image;

import android.widget.ImageView;

public interface ImageLoader {

    void displayThumbnail(String url, ImageView view);

    void displayImage(String url, ImageView view, int width, int height);
}
