package com.yayaway.narwhal;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yayaway.narwhal.com.yayaway.narwhal.ui.EndlessScrollListener;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.LinkListener;
import com.yayaway.narwhal.com.yayaway.narwhal.ui.SubmissionAdapter;
import com.yayaway.narwhal.injection.HasComponent;
import com.yayaway.narwhal.injection.components.DaggerFragmentComponent;
import com.yayaway.narwhal.injection.components.FragmentComponent;
import com.yayaway.narwhal.injection.modules.FragmentModule;
import com.yayaway.narwhal.reddit.AccountManager;

import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SUBREDDIT = "subreddit";
    private static final Logger logger = LoggerFactory.getLogger(SubmissionListFragment.class);
    private final List<Submission> submissions = new CopyOnWriteArrayList<>();

    @Inject
    Executor mExecutor;

    @Inject
    AccountManager mAccountManager;

    @Bind(R.id.submission_listview)
    ListView mSubmissionListView;

    private OnFragmentInteractionListener mListener;
    private SubredditPaginator mPaginator;
    private SubmissionAdapter mAdapter;
    private FragmentComponent mFragmentComponent;

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

    private void initializeInjector() {
        mFragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .build();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        logger.debug("onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);

        // Inject all the things..
        NarwhalApplication.from(this.getContext()).getComponent().inject(this);
        initializeInjector();
        mPaginator = new SubredditPaginator(mAccountManager.getActiveClient());

        if (getArguments() != null) {
            mPaginator.setSubreddit(getArguments().getString(ARG_SUBREDDIT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logger.debug("onCreateView() called with: " + "inflater = [" + inflater + "], container = [" + container + "], savedInstanceState = [" + savedInstanceState + "]");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_submission_list, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new SubmissionAdapter(this.getContext(),
                android.R.layout.simple_list_item_1, submissions);
        getComponent().inject(mAdapter);

        mSubmissionListView.setAdapter(mAdapter);
        mSubmissionListView.setOnScrollListener(new EndlessScrollListener(5) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                logger.info("onLoadMore() called with: " + "page = [" + page + "], totalItemsCount = [" + totalItemsCount + "]");
                mExecutor.execute(new FetchNext());
                return true;
            }
        });

        mAdapter.setLinkListener(new LinkListener() {
            @Override
            public void OnLinkListener(String uri) {
                mListener.onFragmentInteraction(uri);
            }
        });
        refresh();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        logger.debug("onAttach() called with: " + "context = [" + context + "]");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        logger.debug("onDetach() called with: " + "");
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        logger.debug("onDestroyView() called with: " + "");
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void updateSubreddit(String reddit) {
        if (!this.mPaginator.getSubreddit().equals(reddit))
            this.submissions.clear();

        this.mPaginator.setSubreddit(reddit);
        this.mExecutor.execute(new FetchNext());
    }

    public void refresh() {
        this.submissions.clear();
        this.mPaginator.reset();
        this.mExecutor.execute(new FetchNext());
    }

    public void setSorting(Sorting sort) {
        if (mPaginator.getSorting() == sort)
            return;
        mPaginator.setSorting(sort);
        refresh();
    }

//    @OnItemClick(R.id.submission_listview)
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        logger.info("onItemClick() called with: " + "adapterView = [" + adapterView + "], view = [" + view + "], i = [" + i + "], l = [" + l + "]");
//    }

    @Override
    public FragmentComponent getComponent() {
        return mFragmentComponent;
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
            logger.info("FetchNext:  Getting next from: " + mPaginator.getSubreddit() + " after page: " + mPaginator.getPageIndex());
            long start = SystemClock.currentThreadTimeMillis();
            final List<Submission> next = mPaginator.next();
            logger.info("FetchNext: Received " + next.size() + " submissions in: " + (SystemClock.currentThreadTimeMillis() - start) + " ms.");
            Activity act = getActivity();
            if (act != null)
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (Submission s : next) {
                            mAdapter.add(s);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
        }
    }

}
