package com.yayaway.narwhal.reddit;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.dean.jraw.http.AuthenticationMethod;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthHelper;
import net.dean.jraw.models.LoggedInAccount;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by erwinj on 12/4/15.
 */
public class NarwhalRedditClienMock {

    @NonNull
    public static LoggedInAccount getAccount(String name) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fakeJson = " {\"name\": \"" + name + "\"}";
        return new LoggedInAccount(mapper.readTree(fakeJson));
    }

    @NonNull
    public static OAuthData getAuth(String token) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String fakeJson = " {\"access_token\": \"" + token + "\"}";
        return new OAuthData(AuthenticationMethod.APP, mapper.readTree(fakeJson));
    }

    @NonNull
    public static NarwhalRedditClient getNarwhalRedditClient(String user, String token) throws IOException {
        NarwhalRedditClient client = mock(NarwhalRedditClient.class);
        when(client.me()).thenReturn(getAccount(user));
        when(client.isAuthenticated()).thenReturn(true);
        OAuthHelper helper = new OAuthHelper(client);
        helper.setRefreshToken(token);
        when(client.getOAuthHelper()).thenReturn(helper);
        return client;
    }
}
