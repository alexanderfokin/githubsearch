package ru.symdeveloper.githubsearch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static SharedPreferences sharedPreferences;

    public static void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static String getLogin() {
        return sharedPreferences.getString(USERNAME, "");
    }
    public static String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }

    public static boolean hasAuthInformation() {
        return sharedPreferences.contains(USERNAME) && sharedPreferences.contains(PASSWORD);
    }

    public static void clearAuthInformation() {
        sharedPreferences.edit()
                .remove(USERNAME)
                .remove(PASSWORD)
                .apply();
    }

    public static void setAuthInformation(String login, String password) {
        sharedPreferences.edit()
                .putString(USERNAME, login)
                .putString(PASSWORD, password)
                .apply();
    }
}
