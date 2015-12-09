package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.image.ImageLoader;

import net.dean.jraw.models.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
    private final HashMap<List<Submission>, View> mViewMap = new HashMap<>();

    private AbstractSubmissionViewFactory mViewFactory;

    @Inject
    ImageLoader mImageLoader;

    public SubmissionAdapter(Context context, int resource, List<Submission> objects) {
        super(context, resource, objects);
    }

    @Override
    public int getCount() {
        int count = super.getCount();
        return count / mViewFactory.getItemsPerRow();

    }

    public void setViewFactory(AbstractSubmissionViewFactory viewFactory) {
        this.mViewFactory = viewFactory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List<Submission> submissions = new ArrayList<>();
        for (int i = 0; i < mViewFactory.getItemsPerRow(); i++) {
            submissions.add(getItem((position * mViewFactory.getItemsPerRow()) + i));
        }

        if (convertView == null) {
            logger.info("New view");
            convertView = mViewFactory.getViewForSubmissions(submissions);
        } else {
            logger.info("Reuse view");
            ((AbstractSubmissionView) convertView).setSubmissions(submissions);
        }

        return convertView;
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


