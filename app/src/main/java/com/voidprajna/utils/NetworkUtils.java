package com.voidprajna.utils;

import android.os.Handler;
import android.os.Looper;

import com.elvishew.xlog.XLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkUtils {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }

    public static void get(String urlString, Callback callback) {
        executorService.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line);
                    }
                    in.close();
                    
                    String result = response.toString();
                    mainHandler.post(() -> callback.onSuccess(result));
                } else {
                    mainHandler.post(() -> callback.onFailure(new Exception("HTTP error code: " + responseCode)));
                }
            } catch (Exception e) {
                XLog.e("NetworkUtils GET Error: " + e.getMessage());
                mainHandler.post(() -> callback.onFailure(e));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
