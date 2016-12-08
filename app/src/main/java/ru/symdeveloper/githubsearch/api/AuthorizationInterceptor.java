package ru.symdeveloper.githubsearch.api;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.symdeveloper.githubsearch.utils.Settings;

public class AuthorizationInterceptor implements Interceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.addHeader(AUTHORIZATION_HEADER, getAuthorizationHeader());
        return chain.proceed(builder.build());
    }

    private String getAuthorizationHeader() {
        return "Basic " + Base64.encodeToString((Settings.getLogin() + ":"
                + Settings.getPassword()).getBytes(), Base64.NO_WRAP);
    }
}
