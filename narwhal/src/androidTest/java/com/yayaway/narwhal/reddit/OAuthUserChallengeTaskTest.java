package com.yayaway.narwhal.reddit;

import android.content.Context;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static com.yayaway.narwhal.reddit.NarwhalRedditClienMock.getNarwhalRedditClient;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erwinj on 12/4/15.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class OAuthUserChallengeTaskTest {

    private Context mContext;

    @Rule
    public UiThreadTestRule threadTestRule = new UiThreadTestRule();

    @Before
    public void initTargetContext() {
        mContext = getTargetContext();
        assertThat(mContext, notNullValue());
    }

    @Test
    public void getAuthorizationUrlTest() throws IOException {
        NarwhalRedditClient narwhalRedditClient = getNarwhalRedditClient("foo", "sometoken");
        Credentials creds = Credentials.installedApp("abc", "http://some/url");
        OAuthUserChallengeTask challengeTask = new OAuthUserChallengeTask(narwhalRedditClient, creds);

        assertThat(challengeTask.getAuthorizationUrl(), notNullValue());
    }

    @Test
    public void executeCallbackOnSuccess() throws Throwable {
        NarwhalRedditClient narwhalRedditClient = getNarwhalRedditClient("foo", "sometoken");
        Credentials creds = Credentials.installedApp("abc", "http://some/url");
        final CountDownLatch signal = new CountDownLatch(1);
        CallbackListener callback = new CallbackListener(signal);
        final OAuthUserChallengeTask challengeTask = new OAuthUserChallengeTask(narwhalRedditClient, creds, callback);
        threadTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                challengeTask.execute("http://narwhal.me/droid?state=tma6ut0127laltlptnp81fibp0&code=jRQuEwfSppqbsqAMR68H59UMncQ");
            }
        });
        signal.await(5, TimeUnit.SECONDS);
        assertThat(callback.getException(), is(notNullValue()));
        assertThat(callback.getoAuthData(), is(nullValue()));
    }


    @Test
    public void executeCallbackOnError() throws Throwable {
        final String url = "http://narwhal.me/droid?state=tma6ut0127laltlptnp81fibp0&code=jRQuEwfSppqbsqAMR68H59UMncQ";
        NarwhalRedditClient narwhalRedditClient = getNarwhalRedditClient("foo", "sometoken");
        Credentials creds = Credentials.installedApp("abc", "http://some/url");
        final CountDownLatch signal = new CountDownLatch(1);
        CallbackListener callback = new CallbackListener(signal);

        OAuthHelper helper = mock(OAuthHelper.class);
        when(narwhalRedditClient.getOAuthHelper()).thenReturn(helper);
        when(helper.onUserChallenge(url, creds)).thenReturn(NarwhalRedditClienMock.getAuth("foo"));

        final OAuthUserChallengeTask challengeTask = new OAuthUserChallengeTask(narwhalRedditClient, creds, callback);

        threadTestRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                challengeTask.execute(url);
            }
        });
        signal.await(5, TimeUnit.MINUTES);
        assertThat(callback.getException(), is(nullValue()));
        assertThat(callback.getoAuthData(), is(notNullValue()));
    }

    private class CallbackListener implements OAuthUserChallengeTask.TokenAcquiredListener {

        private final CountDownLatch latch;
        private OAuthData oAuthData;
        private Exception exception;

        public Exception getException() {
            return exception;
        }

        public OAuthData getoAuthData() {
            return oAuthData;
        }

        public CallbackListener(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void obtainedToken(OAuthData oAuthData) {
            this.oAuthData = oAuthData;
            latch.countDown();
        }

        @Override
        public void onError(Exception e) {
            this.exception = e;
            latch.countDown();
        }
    }
}