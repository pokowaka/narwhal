package com.yayaway.narwhal.com.yayaway.narwhal.ui.submissions;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.yayaway.narwhal.R;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.LinkListener;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.SquareImageView;

import net.dean.jraw.models.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by erwinj on 11/30/15.
 */
public class SubmissionView extends LinearLayout {

    private static final Logger logger = LoggerFactory.getLogger(SubmissionView.class);

    @Bind(R.id.submission_title)
    TextView titleView;
    @Bind(R.id.submission_score)
    SubmissionScoreView submissionScoreView;
    @Bind(R.id.submission_thumbnail)
    SquareImageView thumbnailView;

    private final ViewPreloadSizeProvider<Submission> viewPreloadSizeProvider;
    private final RequestManager requestManager;
    private final LinkListener linkListener;

    private Submission submission;

    public SubmissionView(Context context) {
        this(context, null);
    }

    public SubmissionView(Context context, AttributeSet attrs) {
        this(context, attrs, Glide.with(context), new ViewPreloadSizeProvider<Submission>(), null);
    }

    public SubmissionView(Context context, AttributeSet attributeSet, RequestManager requestManager,
                          ViewPreloadSizeProvider<Submission> viewPreloadSizeProvider, LinkListener
                                  linkListener) {
        super(context, attributeSet);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.submission, this, true);

        ButterKnife.bind(this);

        this.requestManager = requestManager;
        this.viewPreloadSizeProvider = viewPreloadSizeProvider;
        this.linkListener = linkListener;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                linkClick(view);
            }
        });
    }

    public void linkClick(View v) {
        if (linkListener != null) {
            linkListener.onLinkListener(submission.getUrl());
        }
    }

    @OnClick(R.id.submission_score)
    public void commentsClick(View view) {
        if (linkListener != null) {
            linkListener.onLinkListener(submission.getPermalink());
        }
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
        submissionScoreView.setSubmission(submission);
        titleView.setText(submission.getTitle());
        if (submission.getThumbnailType() == Submission.ThumbnailType.URL) {
            requestManager
                    .load(Uri.parse(this.submission.getThumbnail()))
                    .into(thumbnailView);
            viewPreloadSizeProvider.setView(thumbnailView);
            thumbnailView.setVisibility(VISIBLE);
        } else {
            thumbnailView.setVisibility(INVISIBLE);
        }
    }
}

