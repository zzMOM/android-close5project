package com.teeterpal.androidteeterpal.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.teeterpal.androidteeterpal.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class TeeterSignInFragment extends TeeterSignInFragmentBase {



    public interface TeeterLoginFragmentListener {
        public void onLoginHelpClicked();

        public void onLoginSuccess();
    }

    private static final String LOG_TAG = "TeeterLoginFragment";
    private static final String USER_OBJECT_NAME_FIELD = "name";

    @InjectView(R.id.sign_in_error_message) TextView errorMessage;
    @InjectView(R.id.sign_in_user_name_edit_text) EditText signInUsername;
    @InjectView(R.id.sign_in_password_edit_text) EditText signInPassword;
    @InjectView(R.id.teeter_sign_in_button) Button teeterSignInButton;
    @InjectView(R.id.forgot_password_button) Button forgotPasswordButton;
    private TeeterLoginFragmentListener loginFragmentListener;
    private TeeterOnLoginSuccessListener onLoginSuccessListener;

    public static TeeterSignInFragment newInstance() {
        TeeterSignInFragment loginFragment = new TeeterSignInFragment();
        return loginFragment;
    }

    public TeeterSignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView = inflater.inflate(R.layout.fragment_teeter_signin, container, false);
        ButterKnife.inject(this, loginView);

        // Show action bar and title
        ((TeeterSignInSignUpActivity) getActivity()).showActionBar();
        ((TeeterSignInSignUpActivity) getActivity()).setActionBarTitle("Sign in");

        return loginView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TeeterLoginFragmentListener) {
            loginFragmentListener = (TeeterLoginFragmentListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implement TeeterLoginFragmentListener");
        }

        if (activity instanceof TeeterOnLoginSuccessListener) {
            onLoginSuccessListener = (TeeterOnLoginSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implement TeeterOnLoginSuccessListener");
        }

        if (activity instanceof TeeterOnLoadingListener) {
            onLoadingListener = (TeeterOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implement TeeterOnLoadingListener");
        }
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    @OnClick(R.id.teeter_sign_in_button) void onSignInClick() {
        String username = signInUsername.getText().toString();
        final String password = signInPassword.getText().toString();

        if(username.length() == 0) {
            showErrorMessage(getString(R.string.teeter_signin_warning_username_missing));
            return;
        } else if(password.length() == 0) {
            showErrorMessage(getString(R.string.teeter_signin_warning_password_missing));
            return;
        }

        loadingStart(true);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (isActivityDestroyed()) {
                    return;
                }

                if (user != null) {
                    loadingFinish();
                    loginSuccess();
                } else {
                    loadingFinish();
                    if (e != null) {
                        debugLog(getString(R.string.teeter_signin_warning_teeter_signin_failed) +
                                e.toString());
                        if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                            showErrorMessage(getString(R.string.teeter_signin_invalid_credentials_toast));
                            signInPassword.selectAll();
                            signInPassword.requestFocus();
                        } else {
                            showErrorMessage(getString(R.string.teeter_signin_failed_unknown_toast));
                        }
                    }
                }
            }
        });
    }

    // OnClick forgot password, open SignInHelp page
    @OnClick(R.id.forgot_password_button) void onForgotPasswordClick() {
        loginFragmentListener.onLoginHelpClicked();
    }

    private void showErrorMessage(String str){
        errorMessage.setText("");
        errorMessage.setText(str);
    }

    private void loginSuccess() {
        onLoginSuccessListener.onLoginSuccess();
    }

}
