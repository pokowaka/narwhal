package com.yayaway.narwhal.reddit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import net.dean.jraw.RedditClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Set;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static com.yayaway.narwhal.reddit.NarwhalRedditClienMock.getNarwhalRedditClient;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by erwinj on 12/3/15.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class AccountManagerTest {
    private Context mContext;
    private DefaultAccountManager mAccountManager;

    @Before
    public void initTargetContext() {
        mContext = getTargetContext();
        mAccountManager = new DefaultAccountManager(mContext);
        assertThat(mContext, notNullValue());
        clearSettings();
    }

    @After
    public void clearSettings() {
        SharedPreferences settings = mContext.getSharedPreferences(
                DefaultAccountManager.ACCOUNT_PREFS, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }


    @Test
    public void testGetActiveUserIsNull() {
        assertThat(mAccountManager.getActiveUser(), is(nullValue()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getRedditClientUserMustExist() {
        mAccountManager.getRedditClient("foo");
    }


    @Test(expected = IllegalArgumentException.class)
    public void setActiveUserMustExist() {
        mAccountManager.setActiveUser("foo");
    }

    @Test
    public void testGetAccountsIsEmpty() {
        Set<String> accounts = mAccountManager.getAccounts();
        assertThat(accounts, notNullValue());
        assertThat(accounts.size(), is(0));
    }

    @Test
    public void testGetAccountsAfterAddition() {
        SharedPreferences settings = mContext.getSharedPreferences(DefaultAccountManager.ACCOUNT_PREFS, Context.MODE_PRIVATE);
        settings.edit()
                .putString(DefaultAccountManager.REGISTERED_ACCOUNTS, "a,b,c")
                .commit();

        Set<String> accounts = mAccountManager.getAccounts();
        assertThat(accounts.contains("a"), is(true));
        assertThat(accounts.contains("b"), is(true));
        assertThat(accounts.contains("c"), is(true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerUnAuthorizedClient() throws IOException {
        NarwhalRedditClient client = getNarwhalRedditClient("user", "token");
        when(client.isAuthenticated()).thenReturn(false);
        mAccountManager.register(client);
    }
    @Test
    public void registerAddsAnAccount() throws IOException {
        NarwhalRedditClient client = getNarwhalRedditClient("user", "token");
        mAccountManager.register(client);

        Set<String> accounts = mAccountManager.getAccounts();
        assertThat(accounts.contains("user"), is(true));
        assertThat(mAccountManager.getActiveUser(), is("user"));
        assertThat((RedditClient) client, is(mAccountManager.getActiveClient()));
    }

    @Test
    public void removeRemovesAnAccount() throws IOException {
        NarwhalRedditClient client = getNarwhalRedditClient("user", "token");
        NarwhalRedditClient client2 = getNarwhalRedditClient("user2", "token2");
        mAccountManager.register(client);
        mAccountManager.register(client2);

        Set<String> accounts = mAccountManager.getAccounts();
        assertThat(accounts.size(), is(2));

        mAccountManager.setActiveUser("user2");
        assertThat(mAccountManager.getActiveUser(), is("user2"));

        mAccountManager.remove(client2);
        assertThat(mAccountManager.getActiveUser(), not("user2"));

        accounts = mAccountManager.getAccounts();
        assertThat(accounts.size(), is(1));

        mAccountManager.remove(client);
        accounts = mAccountManager.getAccounts();
        assertThat(accounts.size(), is(0));

        assertThat(mAccountManager.getActiveUser(), is(nullValue()));
        assertThat(mAccountManager.getActiveClient(), is(mAccountManager.getReadOnlyClient()));
    }

    @Test
    public void getReadOnlyClient() {
        // We actually obtain and initialize a default read only client.
        assertThat(mAccountManager.getReadOnlyClient(), is(notNullValue()));
    }

    @Test
    public void accountsSurviveReboot() throws IOException {
        NarwhalRedditClient client = getNarwhalRedditClient("user", "token");
        NarwhalRedditClient client2 = getNarwhalRedditClient("user2", "token2");
        mAccountManager.register(client);
        mAccountManager.register(client2);
        mAccountManager.setActiveUser("user2");

        // "Reboot", second instance should fetch from persistent store.
        AccountManager accountManager2 = new DefaultAccountManager(mContext);
        Set<String> accounts = accountManager2.getAccounts();
        assertThat(accounts.size(), is(2));
        assertThat(accountManager2.getActiveUser(), is("user2"));
        assertThat(accountManager2.getActiveClient().getOAuthHelper().getRefreshToken(),
                is("token2"));

    }
}