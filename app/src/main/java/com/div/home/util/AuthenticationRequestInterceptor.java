package com.div.home.util;

import android.text.TextUtils;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Nirav Mandani on 14-02-2019.
 * Followal Solutions
 */
@Singleton
public class AuthenticationRequestInterceptor implements Interceptor {

    @Inject
    AuthenticationRequestInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        request.header("Connection", "close");

        // Provide authToken in request header if user is logged in
        if (AppContext.getInstance().getAccessToken() != null && !AppContext.getInstance().getAccessToken().isEmpty())
            request.addHeader("X-Authorization", AppContext.getInstance().getAccessToken());

        Response response = chain.proceed(request.build());

        // get authToken from response header and set updated authToken
        Headers allHeaders = response.headers();
        String authToken = allHeaders.get("X-Authorization");
        if (!TextUtils.isEmpty(authToken)) {
            AppContext.getInstance().setAccessToken(authToken);
        }

        return response;
    }
}
