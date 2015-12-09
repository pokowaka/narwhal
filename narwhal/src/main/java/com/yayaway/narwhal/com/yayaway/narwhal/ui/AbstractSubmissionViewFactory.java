package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;

import net.dean.jraw.models.Submission;

import java.util.List;

/**
 * Created by erwinj on 12/8/15.
 */
public abstract class AbstractSubmissionViewFactory {

    private final ImageLoader mImageLoader;
    private final Context mContext;
    private final LinkListener mLinkListener;
    private final int mItemsPerRow;

    AbstractSubmissionViewFactory(Context context, ImageLoader imageLoader, LinkListener
            listener, int itemsPerRow) {
        this.mImageLoader = imageLoader;
        this.mContext = context;
        this.mLinkListener = listener;
        this.mItemsPerRow = itemsPerRow;
    }

    public Context getContext() {
        return mContext;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public LinkListener getLinkListener() {
        return mLinkListener;
    }

    protected abstract AbstractSubmissionView getViewForSubmissionsInternal(List<Submission> submissions);

    public AbstractSubmissionView getViewForSubmissions(List<Submission> submissions) {
        AbstractSubmissionView submissionView = getViewForSubmissionsInternal(submissions);
        submissionView.setLinkListener(new LinkListener() {
            @Override
            public void onLinkListener(String uri) {
                getLinkListener().onLinkListener(uri);
            }
        });
        submissionView.setImageLoader(getImageLoader());
        submissionView.setSubmissions(submissions);
        return submissionView;
    }

    public int getItemsPerRow() {
        return mItemsPerRow;
    }
}
