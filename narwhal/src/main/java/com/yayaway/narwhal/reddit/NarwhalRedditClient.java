package com.yayaway.narwhal.reddit;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.OAuthData;

/**
 * Created by erwinj on 12/3/15.
 */
public class NarwhalRedditClient extends RedditClient {

    private final AccountManager manager;

    public NarwhalRedditClient(UserAgent userAgent, AccountManager manager) {
        super(userAgent);
        this.manager = manager;
    }

    @Override
    public void authenticate(OAuthData authData) throws NetworkException {
        super.authenticate(authData);

        manager.register(this);
    }

    @Override
    public void deauthenticate() {
        super.deauthenticate();
        manager.remove(this);
    }
}
