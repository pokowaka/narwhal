package com.yayaway.narwhal.com.yayaway.narwhal.ui.submissions;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.LinkListener;

import net.dean.jraw.models.Submission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SubmissionAdapter
        extends RecyclerView.Adapter<SubmissionAdapter.SubmissionViewHolder>
        implements ListPreloader.PreloadModelProvider<Submission> {

    private final List<Submission> submissions = new ArrayList<>();
    private final ViewPreloadSizeProvider<Submission> preloadSizeProvider;
    private final Context context;
    private final RequestManager requestManager;
    private final LinkListener linkListener;

    public SubmissionAdapter(Context context, RequestManager requestManager,
                             ViewPreloadSizeProvider<Submission> preloadSizeProvider,
                             LinkListener listener) {
        this.context = context;
        this.requestManager = requestManager;
        this.preloadSizeProvider = preloadSizeProvider;
        this.linkListener = listener;
    }

    @Override
    public SubmissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SubmissionViewHolder(new SubmissionView(parent.getContext(),
                null, requestManager, preloadSizeProvider, linkListener));
    }

    @Override
    public void onBindViewHolder(SubmissionViewHolder holder, int position) {
        Submission submission = submissions.get(position);
        holder.submissionView.setSubmission(submission);
    }

    @Override
    public int getItemCount() {
        return submissions.size();
    }

    @Override
    public List<Submission> getPreloadItems(int position) {
        return Collections.singletonList(submissions.get(position));
    }

    private static final Logger logger = LoggerFactory.getLogger(SubmissionAdapter.class);

    @Override
    public RequestBuilder getPreloadRequestBuilder(Submission item) {
        return requestManager.load(item.getThumbnail());
    }

    public boolean addAll(Collection<? extends Submission> collection) {
        boolean added = submissions.addAll(collection);
        notifyDataSetChanged();
        return added;
    }

    public void clear() {
        submissions.clear();
        notifyDataSetChanged();
    }

    class SubmissionViewHolder extends RecyclerView.ViewHolder {
        private final SubmissionView submissionView;

        public SubmissionViewHolder(SubmissionView submissionView) {
            super(submissionView);
            this.submissionView = submissionView;
        }
    }
}


