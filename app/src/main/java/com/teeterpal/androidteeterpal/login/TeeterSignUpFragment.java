package com.teeterpal.androidteeterpal.login;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.teeterpal.androidteeterpal.R;
import com.teeterpal.androidteeterpal.data.TeeterParseContract.phoneVerificationEntry;
import com.teeterpal.androidteeterpal.data.TeeterParseContract.userEntry;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class TeeterSignUpFragment extends TeeterSignInFragmentBase {

    @InjectView(R.id.sign_up_error_message) TextView signUpErrorMessage;
    @InjectView(R.id.sign_up_user_name_edit_text) EditText signUpUsername;
    @InjectView(R.id.sign_up_email_edit_text) EditText signUpEmail;
    @InjectView(R.id.sign_up_phone_edit_text) EditText signUpPhoneNumber;
    @InjectView(R.id.sign_up_verify_phone_button) Button signUpVerifyPhoneButton;
    @InjectView(R.id.sign_up_phone_verification_edit_text) EditText signUpPhoneVerifyCode;
    @InjectView(R.id.sign_up_password_edit_text) EditText signUpPassword;
    @InjectView(R.id.sign_up_reenter_password_edit_text) EditText signUpConfirmPassword;
    @InjectView(R.id.teeter_sign_up_button) Button teeterSignUpButton;

    private int minPasswordLength;

    private static final String LOG_TAG = "TeeterSignUpFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
    private String phoneVerifyCode = "";

    private TeeterOnLoginSuccessListener onLoginSuccessListener;
    private TeeterSignInSignUpHelper teeterHelper;


    public static TeeterSignUpFragment newInstance() {
        TeeterSignUpFragment fragment = new TeeterSignUpFragment();
        return fragment;
    }

    public TeeterSignUpFragment() {
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
        View signUpView = inflater.inflate(R.layout.fragment_teeter_sign_up, container, false);
        ButterKnife.inject(this, signUpView);

        // Show action bar and title
        ((TeeterSignInSignUpActivity) getActivity()).showActionBar();
        ((TeeterSignInSignUpActivity) getActivity()).setActionBarTitle("Create Account");

        // initialize teeterHelper
        teeterHelper = new TeeterSignInSignUpHelper();

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;

        // Phone number formatted
        signUpPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        return signUpView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    @OnClick(R.id.teeter_sign_up_button) void onSignUpButtonClick() {
        final String username = signUpUsername.getText().toString().trim();
        final String password = signUpPassword.getText().toString().trim();
        String passwordAgain = signUpConfirmPassword.getText().toString().trim();
        final String email = signUpEmail.getText().toString().trim();
        final String phoneNumber = signUpPhoneNumber.getText().toString().trim();
        phoneVerifyCode = signUpPhoneVerifyCode.getText().toString().trim();

        if (username.length() == 0) {
            showErrorMessage(getText(R.string.teeter_signup_field_missing) + "username");
        } else if (password.length() == 0) {
            showErrorMessage(getText(R.string.teeter_signup_field_missing) + "password");
        } else if (password.length() < minPasswordLength) {
            showErrorMessage(getString(R.string.teeter_signup_password_too_short));
        } else if (passwordAgain.length() == 0) {
            showErrorMessage(getText(R.string.teeter_signup_field_missing) + "re-enter password");
        } else if (!password.equals(passwordAgain)) {
            showErrorMessage(getString(R.string.teeter_signup_mismatch_confirm_password));
            signUpConfirmPassword.selectAll();
            signUpConfirmPassword.requestFocus();
        } else if (email.length() == 0) {
            showErrorMessage(getText(R.string.teeter_signup_field_missing) + "email");
        } else if (phoneNumber.length() == 0) {
            showErrorMessage(getText(R.string.teeter_signup_field_missing) + "phone");
        } else if (phoneVerifyCode.length() == 0) {
            showErrorMessage(getText(R.string.teeter_signup_field_missing) + "verification code");
        } else {
            // Check verification code user entered whether match with
            // verification code send by sms and saved in parse object PHONE_VERIFICATION_OBJECT
            // Set parse query
            ParseQuery<ParseObject> query = ParseQuery.getQuery(
                    phoneVerificationEntry.PHONE_VERIFICATION_OBJECT);
            query.whereEqualTo(phoneVerificationEntry.PHONE_VERIFICATION_OBJECT_PHONE_NUMBER, phoneNumber);
            query.orderByDescending("updatedAt");
            loadingStart(true);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parseObjects, ParseException e) {
                    if (e == null) {
                        String verificationCodeFromParse = parseObjects.get(0).getString(
                                phoneVerificationEntry.PHONE_VERIFICATION_OBJECT_VERIFICATION_CODE);
                        Log.e(phoneVerificationEntry.PHONE_VERIFICATION_OBJECT, verificationCodeFromParse);
                        if(phoneVerifyCode.equals(verificationCodeFromParse)){
                            // Phone verify confirmed, parse signup
                            parseSignUp(username, password, email, phoneNumber);
                        } else {
                            loadingFinish();
                            showErrorMessage(getString(R.string.teeter_signup_verification_code_not_match));
                        }
                    } else {
                        loadingFinish();
                        Log.e(phoneVerificationEntry.PHONE_VERIFICATION_OBJECT, "Error: " + e.getMessage());
                        showErrorMessage(getString(R.string.teeter_signup_verification_code_not_found));
                    }
                }
            });
        }
    }

    @OnClick(R.id.sign_up_verify_phone_button) void onVerifyPhoneButtonClick() {
        // Check phone number length whether invalid
        String phoneNumber = signUpPhoneNumber.getText().toString().trim();
        if(phoneNumber.length() < 10) {
            showErrorMessage(getString(R.string.teeter_signup_phone_number_invalid));
            return;
        }

        if(teeterHelper.sentAndSavePhoneVerificationCode(phoneNumber)) {
            Toast.makeText(getActivity(), "SMS sent!", Toast.LENGTH_SHORT).show();
        } else {
            showErrorMessage(getString(R.string.teeter_signup_sms_sent_failed));
        }


    }


    private void parseSignUp(String username, String password, String email, String phoneNumber){
        ParseUser user = new ParseUser();

        // Set standard fields
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Set additional custom fields
        user.put(userEntry.USER_OBJECT_PHONE_FIELD, phoneNumber);

        //loadingStart();
        user.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {
                if (isActivityDestroyed()) {
                    return;
                }

                if (e == null) {
                    loadingFinish();
                    signUpSuccess();
                } else {
                    loadingFinish();
                    if (e != null) {
                        debugLog(getString(R.string.teeter_signup_warning_parse_signup_failed) +
                                e.toString());
                        switch (e.getCode()) {
                            case ParseException.INVALID_EMAIL_ADDRESS:
                                showErrorMessage(getString(R.string.teeter_signup_invalid_email));
                                break;
                            case ParseException.USERNAME_TAKEN:
                                showErrorMessage(getString(R.string.teeter_signup_username_taken));
                                break;
                            case ParseException.EMAIL_TAKEN:
                                showErrorMessage(getString(R.string.teeter_signup_email_taken));
                                break;
                            default:
                                showErrorMessage(getString(R.string.teeter_signup_failed_unknown));
                        }
                    }
                }
            }
        });
    }


    private void showErrorMessage(String str){
        signUpErrorMessage.setText("");
        signUpErrorMessage.setText(str);
    }

    private void signUpSuccess() {
        onLoginSuccessListener.onLoginSuccess();
    }

}
