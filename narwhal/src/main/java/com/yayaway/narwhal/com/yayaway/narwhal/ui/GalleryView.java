package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.yayaway.narwhal.R;

import net.dean.jraw.models.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by erwinj on 12/9/15.
 */
public class GalleryView extends AbstractSubmissionView {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionView.class);

    @Bind(R.id.gallery_img_left)
    ImageView mLeft;


    @Bind(R.id.gallery_img_right)
    ImageView mRight;

    private Submission mLeftSubmission;
    private Submission mRightSubmission;

    public GalleryView(Context context) {
        this(context, null);
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.submission_gallery, this, true);

        ButterKnife.bind(this);
    }


    @OnClick(R.id.gallery_img_left)
    public void leftClick(View view) {
        if (mLinkListener != null) {
            mLinkListener.onLinkListener(mLeftSubmission.getUrl());
        }
    }


    @OnClick(R.id.gallery_img_right)
    public void rightClick(View view) {
        if (mLinkListener != null) {
            mLinkListener.onLinkListener(mRightSubmission.getUrl());
        }
    }


    @Override
    public void setSubmissions(List<Submission> submissions) {
        Iterator<Submission> iterator = submissions.iterator();
        if (iterator.hasNext()) {
            setLeft(iterator.next());
        }
        if (iterator.hasNext()) {
            setRight(iterator.next());
        }
    }

    public void setLeft(Submission submission) {
        mLeftSubmission = submission;
        mLeft.setVisibility(VISIBLE);
        mImageLoader.displayImage(submission.getUrl(), mLeft, 300, 300);
    }

    public void setRight(Submission submission) {
        mRightSubmission = submission;
        mRight.setVisibility(VISIBLE);
        logger.info("setRight: url: "  + submission.getUrl() + " title: " + submission.getTitle());
        mImageLoader.displayImage(submission.getUrl(), mRight, 300, 300);
    }
}
