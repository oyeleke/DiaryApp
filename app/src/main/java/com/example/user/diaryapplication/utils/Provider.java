package com.example.user.diaryapplication.utils;

import android.content.Context;

/**
 * Convenience class to instantiate single instance of repositories to be used
 * throughout the app and to make it easy to swap concrete implementations
 * of repositories in case a local storage will be used
 */

public final class Provider {

    private static PrefsUtils prefsUtils;

    public static PrefsUtils providePrefManager(Context context) {
        if (prefsUtils == null) {
            prefsUtils = new PrefsUtils(context.getApplicationContext());
        }
        return prefsUtils;
    }


}
