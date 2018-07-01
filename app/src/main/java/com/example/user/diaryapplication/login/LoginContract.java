package com.example.user.diaryapplication.login;


import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

public interface LoginContract {

    interface view {
        void showLoading();

        void dismissLoading();


        void getUserDetails(FirebaseUser currentUser);


        void moveToNextStep();
    }

    interface presenter {
        boolean validateUserEmail(EditText emaileditText);

        boolean validateUserPassword(EditText passwordEditText);

        void onUserLoggedIn();

        void onUserDetailsGotten();
    }
}