package com.teeterpal.androidteeterpal.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.teeterpal.androidteeterpal.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class ThirdPartySignInFragment extends TeeterSignInFragmentBase {
    private static final String LOG_TAG = ThirdPartySignInFragment.class.getSimpleName();
    private TeeterOnLoginSuccessListener onLoginSuccessListener;
    private TeeterOnLoadingListener onLoadingListener;
    private ThirdPartySignInFragmentListener thirdPartySignInFragmentListener;

    @InjectView(R.id.third_party_error_message) TextView errorMessage;
    @InjectView(R.id.facebook_login_button) Button facebookLoginButton;
    @InjectView(R.id.teeter_sign_in) Button teeterSignInButton;
    @InjectView(R.id.teeter_sign_up) Button teeterSignUpButton;

    public interface ThirdPartySignInFragmentListener{
        void onTeeterSignInClicked();
        void onTeeterSignUpClicked();
    }

    public static ThirdPartySignInFragment newInstance() {
        ThirdPartySignInFragment signInFragment = new ThirdPartySignInFragment();
        return signInFragment;
    }

    public ThirdPartySignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Hide action bar
        ((TeeterSignInSignUpActivity) getActivity()).hideActionBar();

        // Inflate the layout for this fragment
        View signInView = inflater.inflate(R.layout.fragment_third_party_signin, container, false);
        ButterKnife.inject(this, signInView);
        return signInView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ThirdPartySignInFragmentListener) {
            thirdPartySignInFragmentListener = (ThirdPartySignInFragmentListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implement ThirdPartySignInFragmentListener");
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

    private void showErrorMessage(String str){
        errorMessage.setText("");
        errorMessage.setText(str);
    }

    private void loginSuccess() {
        onLoginSuccessListener.onLoginSuccess();
    }

    @OnClick(R.id.teeter_sign_in) void onTeeterSignInClick(){
        thirdPartySignInFragmentListener.onTeeterSignInClicked();
    }

    @OnClick(R.id.teeter_sign_up) void OnTeeterSignUpClick(){
        thirdPartySignInFragmentListener.onTeeterSignUpClicked();
    }

    @OnClick(R.id.facebook_login_button) void onFacebookLoginClick(){
        loadingStart(true);
        ParseFacebookUtils.initialize();
        ParseFacebookUtils.logIn(getActivity(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (isActivityDestroyed()) {
                            return;
                        }

                        if (user == null) {
                            loadingFinish();
                            if (e != null) {
                                showErrorMessage(getString(R.string.teeter_facebook_login_failed_toast));
                                debugLog(getString(R.string.teeter_login_warning_facebook_login_failed) +
                                        e.toString());
                            }
                        } else {
                            loginSuccess();
                        }
                    }
                });

    }

}
