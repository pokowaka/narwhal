package com.yayaway.narwhal.com.yayaway.narwhal.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yayaway.narwhal.R;

import net.dean.jraw.models.Submission;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;


/**
 * Created by erwinj on 11/30/15.
 */
public class SubmissionScoreView extends LinearLayout {

    private final TextView mTextAge;
    private final TextView mTextScore;
    private final TextView mTextComments;

    public SubmissionScoreView(Context context) {
        this(context, null);
    }

    public SubmissionScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.submission_score, this, true);

        mTextAge = (TextView) findViewById(R.id.textAge);
        mTextScore = (TextView) findViewById(R.id.textScore);
        mTextComments = (TextView) findViewById(R.id.textComments);
        setScoreCommentCount(123, 5423, new Date(System.currentTimeMillis() - 2133433424));
    }

    public void setSubmission(Submission s) {
        setScoreCommentCount(s.getScore(), s.getCommentCount(), s.getCreatedUtc());
    }

    private String compactDecimal(int comments) {
        return "" + comments;
    }

    private void setScoreCommentCount(Integer score, Integer comments, Date created) {
        PrettyTime prettyTime = new PrettyTime(new Date());
        if (mTextScore != null)
            mTextScore.setText("" + score);
        if (mTextComments != null)
            mTextComments.setText(compactDecimal(comments == null ? 0 : comments));
        if (mTextAge != null)
            mTextAge.setText(prettyTime.format(created));
    }

}
