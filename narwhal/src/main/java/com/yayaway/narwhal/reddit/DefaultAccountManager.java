package com.yayaway.narwhal.reddit;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yayaway.narwhal.BuildConfig;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.LoggingMode;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by erwinj on 12/3/15.
 */
public class DefaultAccountManager implements AccountManager {

    public static final String REDIRECT_URL = "http://narwhal.me/droid";
    public static final String CLIENT_ID = "2a8w1KAEac6hfg";
    public static final String ACCOUNT_PREFS = "RedditTokens";
    public static final String REGISTERED_ACCOUNTS = "@Accounts";
    public static final String DEFAULT_USER = "@Default_user";
    private static final Logger logger = LoggerFactory.getLogger(DefaultAccountManager.class);

    private final Map<String, RedditClient> mClientMap = new HashMap<>();
    private final Context mContext;
    private final UserAgent mUserAgent;
    private final RedditClient mReadOnlyClient;

    public DefaultAccountManager(Context context) {
        this.mContext = context;
        String version = "unknown";
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext
                    .getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // Shouldn't happen.
            logger.error("Couldn't find package name " + e);
        }
        mUserAgent = UserAgent.of("android", "com.yayaway.narwhal", version, "pokowaka");
        mReadOnlyClient = new ReadOnlyRedditClient(mUserAgent);
        if (BuildConfig.DEBUG) {
            mReadOnlyClient.setLoggingMode(LoggingMode.ALWAYS);
        }
    }

    private RedditClient newClient() {
        RedditClient redditClient = new RedditClient(mUserAgent);
        if (BuildConfig.DEBUG) {
            redditClient.setLoggingMode(LoggingMode.ALWAYS);
        }

        return redditClient;
    }

    @Override
    public String getActiveUser() {
        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, 0);
        return settings.getString(DEFAULT_USER, null);
    }

    @Override
    public void setActiveUser(String user) {
        if (user != null && !getAccounts().contains(user)) {
            throw new IllegalArgumentException("User is not in the account set");
        }

        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DEFAULT_USER, user);
        editor.apply();
    }

    @Override
    public RedditClient getActiveClient() {
        return getRedditClient(getActiveUser());
    }

    RedditClient getRedditClient(String user) {
        if (user == null) {
            // Return the read only client.
            return getReadOnlyClient();
        }

        if (!getAccounts().contains(user)) {
            throw new IllegalArgumentException("User is not in the account set");
        }

        if (mClientMap.containsKey(user)) {
            return mClientMap.get(user);
        }

        // We need to construct a client
        RedditClient client = newClient();
        String token = mContext.getSharedPreferences(ACCOUNT_PREFS, 0).getString(user, null);
        client.getOAuthHelper().setRefreshToken(token);

        return client;
    }

    RedditClient getReadOnlyClient() {
        return mReadOnlyClient;
    }

    @Override
    public OAuthUserChallengeTask getOAuthUserChallengeTask() {
        RedditClient client = newClient();
        Credentials credentials = Credentials.installedApp(CLIENT_ID, REDIRECT_URL);
        return new OAuthUserChallengeTask(client, credentials);
    }

    void remove(NarwhalRedditClient redditClient) {
        String name = redditClient.me().getFullName();

        Set<String> accounts = getAccounts();
        accounts.remove(name);

        // We removed the default user, lets update it.
        if (getActiveUser().equals(name)) {
            if (accounts.isEmpty()) {
                setActiveUser(null);
            } else {
                setActiveUser(accounts.iterator().next());
            }
        }

        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(name);
        if (accounts.isEmpty()) {
            editor.remove(REGISTERED_ACCOUNTS);
        } else {
            editor.putString(REGISTERED_ACCOUNTS, TextUtils.join(",", accounts));
        }
        editor.apply();
        mClientMap.remove(name);
    }

    void register(NarwhalRedditClient redditClient) {
        if (!redditClient.isAuthenticated()) {
            throw new IllegalArgumentException("redditClient is not authenticated");
        }

        String name = redditClient.me().getFullName();
        String refresh = redditClient.getOAuthHelper().getRefreshToken();
        if (refresh == null) {
            throw new NullPointerException("refresh token cannot be null!");
        }


        Set<String> accounts = getAccounts();
        accounts.add(name);

        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, Context
                .MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, refresh);
        editor.putString(REGISTERED_ACCOUNTS, TextUtils.join(",", accounts));
        editor.apply();

        // Add it to the open client map
        mClientMap.put(name, redditClient);

        // Set this to be the active user
        if (getActiveUser() == null) {
            setActiveUser(name);
        }
    }

    @Override
    public Set<String> getAccounts() {
        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, Context
                .MODE_PRIVATE);
        String accounts = settings.getString(REGISTERED_ACCOUNTS, null);
        return accounts == null ? new HashSet<String>() :
                new HashSet<>(Arrays.asList(accounts.split(",")));
    }
}
