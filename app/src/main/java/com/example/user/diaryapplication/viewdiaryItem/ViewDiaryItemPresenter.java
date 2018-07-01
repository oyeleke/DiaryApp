package com.example.user.diaryapplication.viewdiaryItem;

import android.util.Log;
import android.widget.EditText;

import com.example.user.diaryapplication.model.DiaryModel;
import com.example.user.diaryapplication.model.User;
import com.example.user.diaryapplication.utils.Constants;
import com.example.user.diaryapplication.utils.DateUtils;
import com.example.user.diaryapplication.utils.PrefsUtils;
import com.example.user.diaryapplication.utils.ViewUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.Date;

/**
 * Created by timi on 30/06/2018.
 */

public class ViewDiaryItemPresenter implements ViewDiaryItemContract.Presenter {

    private static final String TAG = ViewDiaryItemPresenter.class.getSimpleName();
    private ViewDiaryItemContract.View view;
    private DatabaseReference databaseReference;
    private PrefsUtils prefsUtils;

    public ViewDiaryItemPresenter(ViewDiaryItemContract.View view, DatabaseReference databaseReference, PrefsUtils prefsUtils) {
        this.view = view;
        this.databaseReference = databaseReference;
        this.prefsUtils = prefsUtils;
    }

    @Override
    public void savePost(String postTitle, String postBody) {
        view.showLoading();
        if (prefsUtils.doesContain(Constants.PREF_USER_KEY)){
            User user = prefsUtils.getPrefAsObject(Constants.PREF_USER_KEY, User.class);
            Log.d(TAG, "savePost: "+user);
            Date date = new Date();
            String createdDate = DateUtils.formatDate(date,DateUtils.FULL_DATE);
            String diaryId = databaseReference.push().getKey();
            DiaryModel diaryModel = new DiaryModel(diaryId,user.getUser_id(),postTitle,postBody,createdDate,createdDate);
            databaseReference.child(diaryId).setValue(diaryModel);
            view.onPostSaved();
            view.dismissLoading();
        }else{
            view.onError("User not found");
            view.dismissLoading();
        }

    }

    @Override
    public void saveEditedPost(String postTitle, String postBody) {
        view.showLoading();
        if(prefsUtils.doesContain(Constants.PREF_USER_KEY) && prefsUtils.doesContain(Constants.DIARYITEM)){
            User user = prefsUtils.getPrefAsObject(Constants.PREF_USER_KEY,User.class);
            Date date = new Date();
            String updatedAt = DateUtils.formatDate(date,DateUtils.FULL_DATE);
            DiaryModel oldDiaryModel = prefsUtils.getPrefAsObject(Constants.DIARYITEM, DiaryModel.class);
            DiaryModel edittedDiaryModel = new DiaryModel(oldDiaryModel.getDiaryId(),oldDiaryModel.getUserId(),postTitle,postBody,oldDiaryModel.getCreatedAt(),updatedAt);
            databaseReference.child(oldDiaryModel.getDiaryId()).setValue(edittedDiaryModel);
            view.onEditedPostSaved();
            view.dismissLoading();
        }else{
            view.onError("User or diary entry not found");
            view.dismissLoading();
        }
    }

    @Override
    public boolean validateEditText(EditText titleEditText) {
        return ViewUtils.validateEditTexts(titleEditText);
    }
}
