package com.teeterpal.androidteeterpal.login;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.Toast;

import com.teeterpal.androidteeterpal.map.MainMapActivity;
import com.teeterpal.androidteeterpal.R;

public class TeeterSignInSignUpActivity extends ActionBarActivity implements
        TeeterSignInFragment.TeeterLoginFragmentListener,
        ThirdPartySignInFragment.ThirdPartySignInFragmentListener,
        TeeterSignInHelpFragment.TeeterOnSignInHelpSuccessListener,
        TeeterOnLoadingListener, TeeterOnLoginSuccessListener{

    public static final String LOG_TAG = "TeeterSignInSingUpActivity";

    // All login UI fragment transactions will happen within this parent layout element.
    // Change this if you are modifying this code to be hosted in your own activity.
    private final int fragmentContainer = R.id.content_container;//android.R.id.content;

    private ProgressDialog progressDialog;

    // Although Activity.isDestroyed() is in API 17, we implement it anyways for older versions.
    private boolean destroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teeter_signin_signup);

        // Show the third party sign in page
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(fragmentContainer,
                    ThirdPartySignInFragment.newInstance()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_teeter_signin_signup, menu);
        return true;
    }


    @Override
    public void onLoginHelpClicked() {
        // Show the SignInHelp page, but keep the transaction on the back stack
        // so that if the user clicks the back button, they are brought back
        // to the login form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer, TeeterSignInHelpFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Called when the user successfully logs in or signs up.
     */
    @Override
    public void onLoginSuccess() {
        // This default implementation returns to the parent activity with
        // RESULT_OK.
        // You can change this implementation if you want a different behavior.
        /*setResult(RESULT_OK);
        finish();*/
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainMapActivity.class);
        startActivity(intent);
    }

    /**
     * Called when we are in progress retrieving some data.
     *
     * @param showSpinner
     *     Whether to show the loading dialog.
     */
    @Override
    public void onLoadingStart(boolean showSpinner) {
        if (showSpinner) {
            progressDialog = ProgressDialog.show(this, null,
                    getString(R.string.teeter_progress_dialog_text), true, false);
        }
    }

    /**
     * Called when we are finished retrieving some data.
     */
    @Override
    public void onLoadingFinish() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onTeeterSignInClicked() {
        // Show the signin page, but keep the transaction on the back stack
        // so that if the user clicks the back button, they are brought back
        // to the login form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer, TeeterSignInFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onTeeterSignUpClicked() {
        // Show the signup page, but keep the transaction on the back stack
        // so that if the user clicks the back button, they are brought back
        // to the login form.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainer, TeeterSignUpFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onLogInHelpSuccess() {
        // Back to sign in page
        onBackPressed();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        destroyed = true;
    }

    /**
     * @see android.app.Activity#isDestroyed()
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        }
        return destroyed;
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void hideActionBar() {
        getSupportActionBar().hide();
    }

    public void showActionBar() {
        getSupportActionBar().show();
    }


}
