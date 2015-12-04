package com.yayaway.narwhal.reddit;

import android.os.AsyncTask;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by erwinj on 12/3/15.
 */
public class OAuthUserChallengeTask
        extends AsyncTask<String, Void, OAuthData> {


    public interface TokenAcquiredListener {
        void obtainedToken(OAuthData oAuthData);

        void onError(Exception e);
    }

    private RedditClient redditClient;
    private Credentials credentials;
    private static final Logger logger = LoggerFactory.getLogger(OAuthUserChallengeTask.class);
    private final List<TokenAcquiredListener> listeners = new ArrayList<>();

    public OAuthUserChallengeTask(RedditClient redditClient, Credentials creds) {
        this.redditClient = redditClient;
        this.credentials = creds;
    }

    public OAuthUserChallengeTask(RedditClient redditClient, Credentials creds, TokenAcquiredListener listener) {
        this(redditClient, creds);
        addListener(listener);
    }


    public void addListener(TokenAcquiredListener l) {
        listeners.add(l);
    }

    public URL getAuthorizationUrl() {
        final OAuthHelper helper = redditClient.getOAuthHelper();
        boolean permanent = true;
        // OAuth2 scopes to request. See https://www.reddit.com/dev/api/oauth for a full list
        String[] scopes = {"identity", "read", "vote"};
        return helper.getAuthorizationUrl(credentials, permanent, scopes);
    }

    public RedditClient getRedditClient() {
        return redditClient;
    }

    @Override
    protected OAuthData doInBackground(String... params) {
        try {
            return redditClient.getOAuthHelper().onUserChallenge(params[0], credentials);
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            logger.error("Failed to authenticate " + e + "strace: " + errors) ;
            for (TokenAcquiredListener listener : listeners) {
                listener.onError(e);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(final OAuthData oAuthData) {
        for (TokenAcquiredListener listener : listeners) {
            listener.obtainedToken(oAuthData);
        }
    }
}
