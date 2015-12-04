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
import net.dean.jraw.http.oauth.OAuthException;

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
public class AccountManager {

    public static final String REDIRECT_URL = "http://narwhal.me/droid";
    public static final String CLIENT_ID = "2a8w1KAEac6hfg";
    public static final String ACCOUNT_PREFS = "RedditTokens";
    public static final String REGISTERED_ACCOUNTS = "@Accounts";
    public static final String DEFAULT_USER = "@Default_user";
    private static final Logger logger = LoggerFactory.getLogger(AccountManager.class);

    private final Map<String, RedditClient> mClientMap = new HashMap<>();
    private final Context mContext;
    private RedditClient mReadOnlyClient;

    public AccountManager(Context context) {
        this.mContext = context;
    }

    private RedditClient newClient() {
        String version = "unknown";
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // Shouldn't happen.
        }
        UserAgent agent = UserAgent.of("android", "com.yayaway.narwhal", version, "pokowaka");
        RedditClient redditClient = new RedditClient(agent);
        if (BuildConfig.DEBUG)
            redditClient.setLoggingMode(LoggingMode.ALWAYS);

        return redditClient;
    }


    public void setActiveUser(String user) {
        if (user != null && !getAccounts().contains(user)) {
            throw new IllegalArgumentException("User is not in the account set");
        }

        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DEFAULT_USER, user);
        editor.commit();
    }

    public String getActiveUser() {
        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, 0);
        return settings.getString(DEFAULT_USER, null);
    }

    public RedditClient getActiveClient() {
        return getRedditClient(getActiveUser());
    }

    public RedditClient getRedditClient(String user) {
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

    public RedditClient getReadOnlyClient() {
        if (mReadOnlyClient == null) {
            try {
                initializeReadOnlyClient();
            } catch (OAuthException oae) {
                logger.error("getReadOnlyClient: Unable to initalize read only client: " + oae);
            }
        }
        return mReadOnlyClient;
    }

    private void initializeReadOnlyClient() throws OAuthException {
        mReadOnlyClient = newClient();
        Credentials credentials = Credentials.userlessApp(CLIENT_ID, DeviceIdentifier.getDeviceId(mContext));
        mReadOnlyClient.getOAuthHelper().easyAuth(credentials);
    }

    public OAuthUserChallengeTask getOAuthUserChallengeTask() {
        RedditClient client = newClient();
        Credentials credentials = Credentials.installedApp(CLIENT_ID, REDIRECT_URL);
        return new OAuthUserChallengeTask(client, credentials);
    }

    public void remove(NarwhalRedditClient redditClient) {
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

        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(name);
        if (accounts.isEmpty()) {
            editor.remove(REGISTERED_ACCOUNTS);
        } else {
            editor.putString(REGISTERED_ACCOUNTS, TextUtils.join(",", accounts));
        }
        editor.commit();
        mClientMap.remove(name);
    }

    public void register(NarwhalRedditClient redditClient) {
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

        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(name, refresh);
        editor.putString(REGISTERED_ACCOUNTS, TextUtils.join(",", accounts));
        editor.commit();

        // Add it to the open client map
        mClientMap.put(name, redditClient);

        // Set this to be the active user
        if (getActiveUser() == null) {
            setActiveUser(name);
        }
    }

    public Set<String> getAccounts() {
        SharedPreferences settings = mContext.getSharedPreferences(ACCOUNT_PREFS, Context.MODE_PRIVATE);
        String accounts = settings.getString(REGISTERED_ACCOUNTS, null);
        return accounts == null ? new HashSet<String>() :
                new HashSet<>(Arrays.asList(accounts.split(",")));
    }
}
