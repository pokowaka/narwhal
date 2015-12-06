package com.yayaway.narwhal.reddit;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.OkHttpAdapter;
import net.dean.jraw.http.UserAgent;

/**
 * Created by erwinj on 12/7/15.
 */
public class ReadOnlyRedditClient extends RedditClient {

    public ReadOnlyRedditClient(UserAgent userAgent) {
        super(userAgent, "www.reddit.com", new OkHttpAdapter());
    }
}
