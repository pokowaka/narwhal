package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;

import net.dean.jraw.models.Submission;

import java.util.List;

/**
 * Created by erwinj on 12/8/15.
 */
public class SubmissionViewFactory extends AbstractSubmissionViewFactory {
    public SubmissionViewFactory(Context context, ImageLoader imageLoader, LinkListener listener) {
        super(context, imageLoader, listener, 1);
    }

    @Override
    public AbstractSubmissionView getViewForSubmissionsInternal(List<Submission> submissions) {
        SubmissionView submissionView = new SubmissionView(getContext());

        return submissionView;
    }
}
