package nu.yona.app.utils;

import android.util.Log;

import nu.yona.app.BuildConfig;

/**
 * Created by spatni on 15/09/17.
 * A common Logger implementation to support loggin mechanism in application.
 */

public class Logger {

    public static void logi(String TAG, String message) {
        if(BuildConfig.DEBUG) {
            Log.i(TAG, message);
        }
    }

    public static void loge(String TAG, String message) {
        if(BuildConfig.DEBUG) {
            Log.e(TAG, message);
        }
    }

    public static void printStackTrace(Exception e) {
        if(BuildConfig.DEBUG) {
            e.printStackTrace();
        }
    }
}
