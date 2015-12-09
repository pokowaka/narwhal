package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;

import net.dean.jraw.models.Submission;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by erwinj on 12/9/15.
 */
public abstract class AbstractSubmissionView extends LinearLayout {
    protected LinkListener mLinkListener;
    @Inject
    ImageLoader mImageLoader;

    public AbstractSubmissionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setImageLoader(ImageLoader retriever) {
        this.mImageLoader = retriever;
    }

    public void setLinkListener(LinkListener listener) {
        mLinkListener = listener;
    }

    public abstract void setSubmissions(List<Submission> submissions);
}
