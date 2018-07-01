package com.example.user.diaryapplication.viewdiaryItem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.diaryapplication.BaseActivity;
import com.example.user.diaryapplication.R;
import com.example.user.diaryapplication.model.DiaryModel;
import com.example.user.diaryapplication.utils.Constants;
import com.example.user.diaryapplication.utils.PrefsUtils;
import com.example.user.diaryapplication.utils.Provider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewDiaryItemActivity extends BaseActivity implements ViewDiaryItemContract.View {

    @BindView(R.id.et_addPostEditText)
    EditText postEditTextView;
    @BindView(R.id.et_addTitleEditText)
    EditText titleEditTextView;
    @BindView(R.id.fab_save)
    FloatingActionButton savePostButton;

    private ViewDiaryItemContract.Presenter presenter;
    private DatabaseReference postDataBaseRefrence;
    private PrefsUtils prefsUtils;
    private Toast toast;
    private Boolean defauktBooleanValue = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_diary_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Diary Item");
        ButterKnife.bind(this);
        prefsUtils = Provider.providePrefManager(getApplicationContext());
        postDataBaseRefrence = FirebaseDatabase.getInstance().getReference(Constants.DIARYITEMS);
        presenter = new ViewDiaryItemPresenter(this, postDataBaseRefrence, prefsUtils);

        if (prefsUtils.doesContain(Constants.PREF_KEY_NEW_DIARY_ITEM)) {
            if (!prefsUtils.getBoolean(Constants.PREF_KEY_NEW_DIARY_ITEM, defauktBooleanValue)) {
                setUpViews();
            }
        }
    }

    @Override
    public void onPostSaved() {
        showToast("Diary entry saved");
        finish();
    }

    @Override
    public void onEditedPostSaved() {
        showToast("Diary item has been edited successfully");
        finish();
    }

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void dismissLoading() {
        hideProgressDialog();
    }

    @Override
    public void onError(String errorMessage) {
        showToast(errorMessage);
    }

    private void initializeOrCancelToast() {
        if (toast == null) {
            toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }
        cancelPendingToast();
    }

    public void cancelPendingToast() {
        if (toast.getView() != null && toast.getView().isShown()) {
            Log.d("Toast", "Cancel toast");
            toast.cancel();
        }
    }

    public void showToast(CharSequence message) {
        dismissLoading();
        initializeOrCancelToast();
        toast.setText(message);
        toast.show();
    }

    @OnClick(R.id.fab_save)
    public void onSaveFloatingButtonClicked() {
        if (!presenter.validateEditText(postEditTextView)) {
            postEditTextView.setError("This field cannot be left empty");
            return;
        }
        if (!presenter.validateEditText(titleEditTextView)) {
            titleEditTextView.setError("This field cannot be left empty");
            return;
        }

        if (prefsUtils.doesContain(Constants.PREF_KEY_NEW_DIARY_ITEM)) {
            if (!prefsUtils.getBoolean(Constants.PREF_KEY_NEW_DIARY_ITEM, defauktBooleanValue)) {
                presenter.saveEditedPost(titleEditTextView.getText().toString().trim(), postEditTextView.getText().toString().trim());
            } else {
                presenter.savePost(titleEditTextView.getText().toString().trim(), postEditTextView.getText().toString().trim());
            }
        }
    }

    private void setUpViews() {
        if (prefsUtils.doesContain(Constants.DIARYITEM)) {
            DiaryModel diaryModel = prefsUtils.getPrefAsObject(Constants.DIARYITEM, DiaryModel.class);
            postEditTextView.setText(diaryModel.getContent());
            titleEditTextView.setText(diaryModel.getTitle());
        }
    }
}
