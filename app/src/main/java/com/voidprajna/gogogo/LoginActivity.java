package com.voidprajna.gogogo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.voidprajna.utils.LogtoManager;

import io.logto.sdk.android.LogtoClient;

public class LoginActivity extends BaseActivity {
    private LogtoManager logtoManager;
    private LogtoClient logtoClient;
    private Button loginButton;
    private Button skipButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary, this.getTheme()));
        setContentView(R.layout.activity_login);

        logtoManager = LogtoManager.getInstance(getApplication());
        logtoClient = logtoManager.getLogtoClient();

        initViews();
        checkLoginStatus();
    }

    private void initViews() {
        loginButton = findViewById(R.id.login_button);
        skipButton = findViewById(R.id.skip_button);
        progressBar = findViewById(R.id.progress_bar);

        loginButton.setOnClickListener(v -> startLogin());
        skipButton.setOnClickListener(v -> skipLogin());
    }

    private void checkLoginStatus() {
        if (logtoManager.isLoggedIn()) {
            goToMainActivity();
        } else {
            skipButton.setVisibility(View.VISIBLE);
        }
    }

    private void startLogin() {
        showLoading(true);
        logtoClient.signIn(this, logtoManager.getRedirectUri(), logtoException -> {
            if (logtoException != null) {
                showLoading(false);
                Toast.makeText(this, getString(R.string.login_failed) + ": " + logtoException.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            onLoginSuccess();
        });
    }

    private void onLoginSuccess() {
        logtoClient.getIdTokenClaims((logtoException, idTokenClaims) -> {
            showLoading(false);
            if (logtoException != null) {
                Toast.makeText(this, getString(R.string.login_failed) + ": " + logtoException.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            saveUserInfo(idTokenClaims);
            goToMainActivity();
        });
    }

    private void saveUserInfo(Object claims) {
        if (claims != null) {
            logtoManager.setLoggedIn(true);
            try {
                String userId = (String) claims.getClass().getMethod("getSub").invoke(claims);
                logtoManager.setUserId(userId);
                
                try {
                    String name = (String) claims.getClass().getMethod("getName").invoke(claims);
                    if (name != null) {
                        logtoManager.setUserName(name);
                    }
                } catch (Exception e) {
                }
                
                try {
                    String email = (String) claims.getClass().getMethod("getEmail").invoke(claims);
                    if (email != null) {
                        logtoManager.setUserEmail(email);
                    }
                } catch (Exception e) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void skipLogin() {
        goToMainActivity();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!show);
        skipButton.setEnabled(!show);
    }

    public static void logout(Activity activity) {
        LogtoManager logtoManager = LogtoManager.getInstance(activity.getApplication());
        LogtoClient logtoClient = logtoManager.getLogtoClient();

        logtoClient.signOut(logtoException -> {
            logtoManager.clearUserData();
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        });
    }
}
