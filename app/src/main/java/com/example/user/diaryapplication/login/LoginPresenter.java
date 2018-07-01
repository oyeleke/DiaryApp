package com.example.user.diaryapplication.login;


import android.widget.EditText;

import com.example.user.diaryapplication.utils.ViewUtils;

public class LoginPresenter implements LoginContract.presenter {

    private LoginContract.view view;

    public LoginPresenter(LoginContract.view view) {
        this.view = view;
    }


    @Override
    public boolean validateUserEmail(EditText emaileditText) {

        return ViewUtils.validateEditTexts(emaileditText);
    }

    @Override
    public boolean validateUserPassword(EditText passwordEditText) {
        return ViewUtils.validateEditTexts(passwordEditText);
    }

    @Override
    public void onUserLoggedIn() {
    }

    @Override
    public void onUserDetailsGotten() {
        view.moveToNextStep();
    }
}
