package com.example.user.diaryapplication.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.Toast;


public final class ViewUtils {

    private ViewUtils() {
    }

    public static void show(@NonNull View... views) {
        for (View view : views) {
            show(view);
        }
    }

    public static void hide(@NonNull View... views) {
        for (View view : views) {
            hide(view);
        }
    }

    public static void invis(@NonNull View... views) {
        for (View view : views) {
            invis(view);
        }
    }



    public static void show(@NonNull View view) {
        view.setVisibility(View.VISIBLE);
    }

    public static void hide(@NonNull View view) {
        view.setVisibility(View.GONE);
    }

    public static void invis(@NonNull View view) {
        view.setVisibility(View.INVISIBLE);
    }




    public static boolean validateEditTexts(EditText... editTexts) {
        for (EditText newEdittext : editTexts) {
            if (newEdittext.getText().toString().trim().length() < 1) {
                newEdittext.setError("This Field is required");
                return false;
            }
        }
        return true;
    }


}
