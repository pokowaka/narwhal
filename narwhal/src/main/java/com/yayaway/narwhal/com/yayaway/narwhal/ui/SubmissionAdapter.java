package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;

import net.dean.jraw.models.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * A Simplistic ArrayAdapter that will cache all the views for for every submission.
 * <p/>
 * We do this because:
 * <p/>
 * 1. Getting a proper view holder pattern working that is fast is hard.
 * 2. These lists never get really long (i.e. more than 200 elems).
 */
public class SubmissionAdapter extends ArrayAdapter<Submission> {
    private static final Logger logger = LoggerFactory.getLogger(SubmissionAdapter.class);
    private final HashMap<Submission, View> mViewMap = new HashMap<>();
    private LinkListener mLinkListener;

    @Inject
    ImageLoader mImageLoader;

    public SubmissionAdapter(Context context, int resource, List<Submission> objects) {
        super(context, resource, objects);
    }

    public void setLinkListener(LinkListener listener) {
        mLinkListener = listener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Submission submission = getItem(position);

        if (!mViewMap.containsKey(submission)) {
            SubmissionView submissionView = new SubmissionView(getContext());
            submissionView.setImageLoader(mImageLoader);
            submissionView.setSubmission(submission);
            submissionView.setLinkListener(new LinkListener() {
                @Override
                public void onLinkListener(String uri) {
                    if (mLinkListener != null) {
                        mLinkListener.onLinkListener(uri);
                    }
                }
            });
            mViewMap.put(submission, submissionView);
        }

        return mViewMap.get(submission);
    }

    @Override
    public void notifyDataSetChanged() {
        // Available items less then our cache map..
        // We likely changed our whole data set.. time to flush the cache.
        if (getCount() < mViewMap.size()) {
            mViewMap.clear();
        }
        super.notifyDataSetChanged();
    }
}


