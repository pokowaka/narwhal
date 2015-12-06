package com.yayaway.narwhal.reddit;

import net.dean.jraw.RedditClient;

import java.util.Set;

/**
 * Created by erwinj on 12/8/15.
 */
public interface AccountManager {
    String getActiveUser();

    void setActiveUser(String user);

    RedditClient getActiveClient();

    OAuthUserChallengeTask getOAuthUserChallengeTask();

    Set<String> getAccounts();
}
