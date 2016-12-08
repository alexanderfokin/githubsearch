package ru.symdeveloper.githubsearch.api;

import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.symdeveloper.githubsearch.api.response.AuthResponse;
import ru.symdeveloper.githubsearch.api.response.GitHubResponse;
import rx.Observable;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public interface ApiService {

    @GET("./")
    Observable<AuthResponse> login();

    @GET("search/repositories")
    Observable<GitHubResponse> search(@Query("q") String text, @Query("page") int page);
}
