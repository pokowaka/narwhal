package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yayaway.narwhal.R;

import net.dean.jraw.models.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by erwinj on 11/30/15.
 */
public class SubmissionView extends AbstractSubmissionView {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionView.class);

    @Bind(R.id.submission_title)
    TextView mTitle;
    @Bind(R.id.submission_score)
    SubmissionScoreView mSubmissionScoreView;
    @Bind(R.id.submission_thumbnail)
    SquareImageView mThumbnail;

    private Submission mSubmission;

    public SubmissionView(Context context) {
        this(context, null);
    }

    public SubmissionView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.submission, this, true);

        ButterKnife.bind(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                linkClick(view);
            }
        });
    }

    public void linkClick(View v) {
        if (mLinkListener != null) {
            mLinkListener.onLinkListener(mSubmission.getUrl());
        }
    }

    @OnClick(R.id.submission_score)
    public void commentsClick(View view) {
        if (mLinkListener != null) {
            mLinkListener.onLinkListener(mSubmission.getPermalink());
        }
    }

    @Override
    public void setSubmissions(List<Submission> submissions) {
        setSubmission(submissions.get(0));
    }

    public void setSubmission(Submission submission) {
        mSubmission = submission;
        mSubmissionScoreView.setSubmission(submission);
        mTitle.setText(submission.getTitle());
        if (submission.getThumbnailType() == Submission.ThumbnailType.URL) {
            mImageLoader.displayThumbnail(submission.getThumbnail(), mThumbnail);
            mThumbnail.setVisibility(VISIBLE);
        } else {
            mThumbnail.setVisibility(INVISIBLE);
        }
    }
}

