package com.yayaway.narwhal;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yayaway.narwhal.reddit.AccountManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class MainActivity extends FragmentActivity implements SubmissionListFragment
        .OnFragmentInteractionListener {

    static final Logger logger = LoggerFactory.getLogger(MainActivity.class);

    @Inject
    AccountManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.info("onCreate() called with: " + "savedInstanceState = [" + savedInstanceState
                + "]");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inject all the things..
        NarwhalApplication.from(this).getComponent().inject(this);

        // However, if we're being restored from a previous state,
        // then we don't need to do anything and should return or else
        // we could end up with overlapping fragments.
        if (savedInstanceState != null) {
            return;
        }

        FragmentManager fragmentManger = getSupportFragmentManager();
        // add
        FragmentTransaction ft = fragmentManger.beginTransaction();
        ft.add(R.id.listcontainer, SubmissionListFragment.newInstance("picsE"));
        // alternatively add it with a tag
        // trx.add(R.id.your_placehodler, new YourFragment(), "detail");
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(String uri) {
        logger.info("onFragmentInteraction() called with: " + "uri = [" + uri + "]");
    }
}