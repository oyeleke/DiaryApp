package com.example.user.diaryapplication.diaryItemsList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.diaryapplication.BaseActivity;
import com.example.user.diaryapplication.R;
import com.example.user.diaryapplication.login.LoginActivity;
import com.example.user.diaryapplication.viewdiaryItem.ViewDiaryItemActivity;
import com.example.user.diaryapplication.model.DiaryModel;
import com.example.user.diaryapplication.utils.Constants;
import com.example.user.diaryapplication.utils.PrefsUtils;
import com.example.user.diaryapplication.utils.Provider;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiaryItemsActivity extends BaseActivity implements DiaryItemsContract.view {
    private static final String TAG = DiaryItemsActivity.class.getSimpleName();

    @BindView(R.id.dairy_item_recyclerview)
    RecyclerView diaryItemRecyclerView;
    @BindView(R.id.add_diary_item_fab)
    FloatingActionButton addDiaryItem;

    private DiaryItemsContract.presenter presenter;
    private DatabaseReference diaryItemDatabaseReference;
    private DiaryItemsAdapter diaryItemsAdapter;
    private PrefsUtils prefsUtils;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth firebaseAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_items);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Diary Items");
        prefsUtils = Provider.providePrefManager(getApplicationContext());
        diaryItemDatabaseReference = FirebaseDatabase.getInstance().getReference(Constants.DIARYITEMS);
        firebaseAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
        presenter = new DiaryItemsPresenter(this, new ArrayList<DiaryModel>(), diaryItemDatabaseReference);
        if(prefsUtils.doesContain(Constants.DIARYITEM)){
            prefsUtils.remove(Constants.DIARYITEM);
        }
        presenter.getValueFromDatabase();
    }



    @Override
    public void ondiaryItemsListSuccess() {
        Log.d("TAGD", "ondiaryItemsListSuccess: Are you called");
        diaryItemRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        diaryItemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        diaryItemsAdapter = new DiaryItemsAdapter(presenter,this);
        diaryItemRecyclerView.setAdapter(diaryItemsAdapter);
    }

    @Override
    public void getdiaryItemClicked(DiaryModel diaryModel) {
        prefsUtils.putObject(Constants.DIARYITEM, diaryModel);
        prefsUtils.putBoolean(Constants.PREF_KEY_NEW_DIARY_ITEM,false);
        startActivity(new Intent(this, ViewDiaryItemActivity.class));
    }

    @OnClick(R.id.add_diary_item_fab)
    public void addNewDiaryItem(){
        prefsUtils.putBoolean(Constants.PREF_KEY_NEW_DIARY_ITEM,true);
        startActivity(new Intent(DiaryItemsActivity.this,ViewDiaryItemActivity.class ));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_diary_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), "CLicked", Toast.LENGTH_LONG).show();
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut(){
        firebaseAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(DiaryItemsActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }
}
