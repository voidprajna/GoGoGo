package com.voidprajna.utils;

import android.app.Application;
import android.content.SharedPreferences;

import io.logto.sdk.android.LogtoClient;
import io.logto.sdk.android.type.LogtoConfig;

import java.util.Collections;

public class LogtoManager {
    private static final String LOGTO_ENDPOINT = "https://admin.voidprajna.qzz.io";
    private static final String LOGTO_APP_ID = "yozdk4v6dtnrpdnzi27ds";
    private static final String REDIRECT_URI = "io.logto.android://com.voidprajna.gogogo/callback";

    private static final String KEY_IS_LOGGED_IN = "key_is_logged_in";
    private static final String KEY_USER_NAME = "key_user_name";
    private static final String KEY_USER_EMAIL = "key_user_email";
    private static final String KEY_USER_ID = "key_user_id";

    private static LogtoManager instance;
    private LogtoClient logtoClient;
    private Application application;
    private SharedPreferences preferences;

    private LogtoManager(Application application) {
        this.application = application;
        this.preferences = application.getSharedPreferences("logto_prefs", MODE_PRIVATE);
        initLogtoClient();
    }

    public static synchronized LogtoManager getInstance(Application application) {
        if (instance == null) {
            instance = new LogtoManager(application);
        }
        return instance;
    }

    private void initLogtoClient() {
        LogtoConfig logtoConfig = new LogtoConfig(
                LOGTO_ENDPOINT,
                LOGTO_APP_ID,
                Collections.emptyList(),
                Collections.emptyList(),
                true,
                null
        );
        logtoClient = new LogtoClient(logtoConfig, application);
    }

    public LogtoClient getLogtoClient() {
        return logtoClient;
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        preferences.edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public String getUserName() {
        return preferences.getString(KEY_USER_NAME, "");
    }

    public void setUserName(String userName) {
        preferences.edit().putString(KEY_USER_NAME, userName).apply();
    }

    public String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        preferences.edit().putString(KEY_USER_EMAIL, userEmail).apply();
    }

    public String getUserId() {
        return preferences.getString(KEY_USER_ID, "");
    }

    public void setUserId(String userId) {
        preferences.edit().putString(KEY_USER_ID, userId).apply();
    }

    public void clearUserData() {
        preferences.edit()
                .remove(KEY_IS_LOGGED_IN)
                .remove(KEY_USER_NAME)
                .remove(KEY_USER_EMAIL)
                .remove(KEY_USER_ID)
                .apply();
    }

    public String getRedirectUri() {
        return REDIRECT_URI;
    }
}
