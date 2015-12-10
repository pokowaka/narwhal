package com.yayaway.narwhal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.EndlessRecyclerOnScrollListener;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.LinkListener;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.submissions.SubmissionAdapter;
import com.yayaway.narwhal.injection.HasComponent;
import com.yayaway.narwhal.injection.components.FragmentComponent;
import com.yayaway.narwhal.reddit.AccountManager;

import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubmissionListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubmissionListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmissionListFragment extends Fragment implements HasComponent<FragmentComponent> {
    private static final String ARG_SUBREDDIT = "subreddit";
    private static final Logger logger = LoggerFactory.getLogger(SubmissionListFragment.class);

    @Inject
    Executor executor;

    @Inject
    AccountManager accountManager;

    @Inject
    LinkListener linkListener;

    @Bind(R.id.submission_listview)
    RecyclerView recyclerListView;

    private OnFragmentInteractionListener fragmentInteractionListener;
    private SubredditPaginator subredditPaginator;
    private SubmissionAdapter submissionAdapter;
    private FragmentComponent fragmentComponent;

    public SubmissionListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param subreddit Parameter 1.
     * @return A new instance of fragment SubmissionListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubmissionListFragment newInstance(String subreddit) {
        SubmissionListFragment fragment = new SubmissionListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBREDDIT, subreddit);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inject all the things..
        NarwhalApplication.from(this.getContext()).getComponent().inject(this);
        subredditPaginator = new SubredditPaginator(accountManager.getActiveClient());

        if (getArguments() != null) {
            subredditPaginator.setSubreddit(getArguments().getString(ARG_SUBREDDIT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submission_list, container, false);
        ButterKnife.bind(this, view);

        constructList();

        refresh();
        return view;
    }

    private void constructList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerListView.setLayoutManager(layoutManager);

        ViewPreloadSizeProvider<Submission> preloadSizeProvider =
                new ViewPreloadSizeProvider<>();
        submissionAdapter = new SubmissionAdapter(this.getContext(), Glide.with(this),
                preloadSizeProvider, linkListener);
        recyclerListView.setAdapter(submissionAdapter);

        RecyclerViewPreloader<Submission> preloader =
                new RecyclerViewPreloader<>(Glide.with(this), submissionAdapter,
                        preloadSizeProvider, 4);
        recyclerListView.addOnScrollListener(preloader);
        recyclerListView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                executor.execute(new FetchNext());
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        logger.debug("onAttach() called with: " + "context = [" + context + "]");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            fragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        logger.debug("onDetach() called with: " + "");
        super.onDetach();
        fragmentInteractionListener = null;
    }

    @Override
    public void onDestroyView() {
        logger.debug("onDestroyView() called with: " + "");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void updateSubreddit(String reddit) {
        if (!this.subredditPaginator.getSubreddit().equals(reddit)) {
            submissionAdapter.clear();
        }

        this.subredditPaginator.setSubreddit(reddit);
        this.executor.execute(new FetchNext());
    }

    public void refresh() {
        this.submissionAdapter.clear();
        this.subredditPaginator.reset();
        this.executor.execute(new FetchNext());
    }

    public void setSorting(Sorting sort) {
        if (subredditPaginator.getSorting() == sort) {
            return;
        }
        subredditPaginator.setSorting(sort);
        refresh();
    }

    @Override
    public FragmentComponent getComponent() {
        return fragmentComponent;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
    }

    private class FetchNext implements Runnable {

        @Override
        public void run() {
            final List<Submission> next = subredditPaginator.next();
            Activity act = getActivity();
            if (act != null) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submissionAdapter.addAll(next);
                    }
                });
            }
        }
    }
}
