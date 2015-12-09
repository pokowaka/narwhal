package com.yayaway.narwhal.reddit.injection.modules;

import com.google.common.collect.Sets;
import com.yayaway.narwhal.reddit.AccountManager;
import com.yayaway.narwhal.reddit.OAuthUserChallengeTask;

import net.dean.jraw.RedditClient;

import java.util.Set;

import static org.mockito.Mockito.mock;

/**
 * Created by erwinj on 12/8/15.
 */
public class TestAccountManager implements AccountManager {

    final RedditClient client;

    public TestAccountManager() {
        client = mock(RedditClient.class);

    }

    @Override
    public String getActiveUser() {
        return "mock";
    }

    @Override
    public void setActiveUser(String user) {

    }

    @Override
    public RedditClient getActiveClient() {
        return client;
    }

    @Override
    public OAuthUserChallengeTask getOAuthUserChallengeTask() {
        return mock(OAuthUserChallengeTask.class);
    }

    @Override
    public Set<String> getAccounts() {
        return Sets.newHashSet("mock");
    }
}
