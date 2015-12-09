package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;

import net.dean.jraw.models.Submission;

import java.util.List;

/**
 * Created by erwinj on 12/8/15.
 */
public class GalleryViewFactory extends AbstractSubmissionViewFactory {

    public GalleryViewFactory(Context context, ImageLoader imageLoader, LinkListener listener) {
        super(context, imageLoader, listener, 2);
    }

    @Override
    public AbstractSubmissionView getViewForSubmissionsInternal(List<Submission> submissions) {
        GalleryView galleryView = new GalleryView(getContext());
        return galleryView;
    }
}
