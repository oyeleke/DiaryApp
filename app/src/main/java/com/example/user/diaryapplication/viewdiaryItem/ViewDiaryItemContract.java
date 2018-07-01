package com.example.user.diaryapplication.viewdiaryItem;

import android.widget.EditText;

/**
 * Created by timi on 30/06/2018.
 */

public interface ViewDiaryItemContract {

    interface View{
        void onPostSaved();
        void onEditedPostSaved();
        void showLoading();
        void dismissLoading();
        void onError(String errorMessage);
    }

    interface Presenter{
        void savePost(String PostTitle, String postBody);
        void saveEditedPost(String PostTitle, String postBody);
        boolean validateEditText(EditText titleEditText);
    }
}
