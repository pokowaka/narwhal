package com.yayaway.narwhal;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yayaway.narwhal.reddit.AccountManager;
import com.yayaway.narwhal.reddit.OAuthUserChallengeTask;

import net.dean.jraw.http.oauth.OAuthData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginActivity extends Activity {
    private static final Logger logger = LoggerFactory.getLogger(LoginActivity.class);
    private AccountManager mAccountManager;

    private void clearCookies() {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        logger.info("onCreate() called with: " + "savedInstanceState = [" + savedInstanceState + "]");
        super.onCreate(savedInstanceState);
        mAccountManager = new AccountManager(this);
        setContentView(R.layout.activity_login);
        WebView webView = (WebView) findViewById(R.id.webview);

        final OAuthUserChallengeTask challengeTask = mAccountManager.getOAuthUserChallengeTask();
        challengeTask.addListener(
                new OAuthUserChallengeTask.TokenAcquiredListener() {
                    @Override
                    public void obtainedToken(OAuthData oAuthData) {
                        // challengeTask.getRedditClient().authenticate(oAuthData);
                        // challengeTask.getRedditClient().me();
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle error gracefully..
                    }
                }
        );
        // clear the cookies, so a user can add another account.
        clearCookies();
        webView.loadUrl(challengeTask.getAuthorizationUrl().toExternalForm());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                logger.info("onPageStarted() called with: " + "view = [" + view + "], url = [" + url + "], favicon = [" + favicon + "]");
                if (url.contains("code=")) {
                    // We've detected the redirect URL
                    challengeTask.execute(url);
                }
                if (url.contains("error=access_denied")) {
                    // Clearly user doesn't like us.
                }
            }
        });
    }

}

