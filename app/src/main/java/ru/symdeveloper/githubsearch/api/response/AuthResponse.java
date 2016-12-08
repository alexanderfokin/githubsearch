package ru.symdeveloper.githubsearch.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexander Fokin on 08.12.2016.
 */

// сам по себе, ответ на авторизационный запрос нам не важен. важен только result code у запроса
// поэтому, эта сущность, по сути, фейковая
public class AuthResponse {
    @SerializedName("current_user_url")
    private String currentUserUrl;
}
