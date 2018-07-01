package com.example.user.diaryapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.diaryapplication.BaseActivity;
import com.example.user.diaryapplication.R;
import com.example.user.diaryapplication.diaryItemsList.DiaryItemsActivity;
import com.example.user.diaryapplication.model.User;
import com.example.user.diaryapplication.utils.Constants;
import com.example.user.diaryapplication.utils.PrefsUtils;
import com.example.user.diaryapplication.utils.Provider;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginContract.view {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    @BindView(R.id.login_signin)
    SignInButton loginButton;
    private LoginContract.presenter presenter;
    private FirebaseAuth firebaseAuth;
    private PrefsUtils prefsUtils;
    private Toast toast;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        presenter = new LoginPresenter(this);
        firebaseAuth = FirebaseAuth.getInstance();
        prefsUtils = Provider.providePrefManager(getApplicationContext());
        //configure google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            getUserDetails(currentUser);
        }
    }

    @OnClick(R.id.login_signin)
    public void onSignButtonClicked() {
        signUserIn();
    }

    private void signUserIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
    public void getUserDetails(FirebaseUser currentUser) {
        if (currentUser != null) {
            User user = new User(currentUser.getUid(), currentUser.getEmail(), currentUser.getDisplayName());
            Log.d(TAG, "getUserDetails: "+currentUser.getUid());
            prefsUtils.putObject(Constants.PREF_USER_KEY, user);
            presenter.onUserDetailsGotten();
        } else {
            showToast("Authentication Failed");
        }
    }


    @Override
    public void moveToNextStep() {
        startActivity(new Intent(LoginActivity.this, DiaryItemsActivity.class));
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        showLoading();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            getUserDetails(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            getUserDetails(null);
                        }
                        dismissLoading();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                getUserDetails(null);
                // [END_EXCLUDE]
            }
        }
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
        initializeOrCancelToast();
        toast.setText(message);
        toast.show();
    }
}
