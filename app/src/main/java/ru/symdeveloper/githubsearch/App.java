package ru.symdeveloper.githubsearch;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.symdeveloper.githubsearch.api.ApiService;
import ru.symdeveloper.githubsearch.api.AuthorizationInterceptor;
import ru.symdeveloper.githubsearch.utils.Settings;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class App extends Application {

    private static App instance;
    public static App instance() {
        return instance;
    }

    private ApiService apiService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        Settings.init(this);

        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new AuthorizationInterceptor());
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApiService() {
        return instance.apiService;
    }
}
