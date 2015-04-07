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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.teeterpal.androidteeterpal.R;
import com.teeterpal.androidteeterpal.data.TeeterParseContract.phoneVerificationEntry;
import com.teeterpal.androidteeterpal.data.TeeterParseContract.userEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class TeeterSignInHelpFragment extends TeeterSignInFragmentBase {

    @InjectView(R.id.sign_in_help_error_message) TextView signInHelpErrorMessage;
    @InjectView(R.id.sign_in_help_user_name_edit_text) EditText signInHelpUsername;
    @InjectView(R.id.sign_in_help_phone_edit_text) EditText signInHelpPhoneNumber;
    @InjectView(R.id.sign_in_help_verify_phone_button) Button signInHelpVerifyPhoneButton;
    @InjectView(R.id.sign_in_help_phone_verification_edit_text) EditText signInHelpPhoneVerifyCode;
    @InjectView(R.id.sign_in_help_password_edit_text) EditText signInHelpNewPassword;
    @InjectView(R.id.sign_in_help_reenter_password_edit_text) EditText signInHelpConfirmNewPassword;
    @InjectView(R.id.teeter_sign_in_help_button) Button signInHelpButton;

    private int minPasswordLength;

    private static final String LOG_TAG = "TeeterSignInHelpFragment";
    private static final int DEFAULT_MIN_PASSWORD_LENGTH = 6;
    private String phoneVerifyCode = "";
    private TeeterOnSignInHelpSuccessListener onSignInHelpSuccessListener;
    private TeeterSignInSignUpHelper teeterHelper;

    public interface TeeterOnSignInHelpSuccessListener{
        void onLogInHelpSuccess();
    }

    public static TeeterSignInHelpFragment newInstance() {
        TeeterSignInHelpFragment fragment = new TeeterSignInHelpFragment();
        return fragment;
    }

    public TeeterSignInHelpFragment() {
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
        View signInHelpView = inflater.inflate(R.layout.fragment_teeter_sign_in_help, container, false);
        ButterKnife.inject(this, signInHelpView);

        ((TeeterSignInSignUpActivity) getActivity()).showActionBar();
        ((TeeterSignInSignUpActivity) getActivity()).setActionBarTitle("Forgot Password");

        // initialize teeterHelper
        teeterHelper = new TeeterSignInSignUpHelper();

        minPasswordLength = DEFAULT_MIN_PASSWORD_LENGTH;
        // Phone number formatted
        signInHelpPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        return signInHelpView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof TeeterOnSignInHelpSuccessListener) {
            onSignInHelpSuccessListener = (TeeterOnSignInHelpSuccessListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implement TeeterOnSignInHelpSuccessListener");
        }

        if (activity instanceof TeeterOnLoadingListener) {
            onLoadingListener = (TeeterOnLoadingListener) activity;
        } else {
            throw new IllegalArgumentException(
                    "Activity must implement TeeterOnLoadingListener");
        }
    }


    @OnClick(R.id.teeter_sign_in_help_button) void resetPassword() {
        final String username = signInHelpUsername.getText().toString().trim();
        final String password = signInHelpNewPassword.getText().toString().trim();
        String passwordAgain = signInHelpConfirmNewPassword.getText().toString().trim();
        final String phoneNumber = signInHelpPhoneNumber.getText().toString().trim();
        phoneVerifyCode = signInHelpPhoneVerifyCode.getText().toString().trim();

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
            signInHelpConfirmNewPassword.selectAll();
            signInHelpConfirmNewPassword.requestFocus();
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
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if (e == null) {
                        String verificationCodeFromParse = object.getString(
                                phoneVerificationEntry.PHONE_VERIFICATION_OBJECT_VERIFICATION_CODE);
                        Log.e(phoneVerificationEntry.PHONE_VERIFICATION_OBJECT, verificationCodeFromParse);
                        if (phoneVerifyCode.equals(verificationCodeFromParse)) {
                            // Phone verify confirmed, parse signup
                            updateParseUser(username, password);
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

    @OnClick(R.id.sign_in_help_verify_phone_button) void onVerifyPhoneButtonClick() {
        // Check phone number length whether invalid
        String phoneNumber = signInHelpPhoneNumber.getText().toString().trim();
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

    private void updateParseUser(String username, final String password) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(userEntry.USER_OBJECT_USERNAME, username);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    Log.e("objectId", parseUser.getObjectId());
                    parseUser.setPassword(password);
                    parseUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null){  // reset success
                                loadingFinish();
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "Password reset successful! Please sign in!", Toast.LENGTH_SHORT).show();
                                resetPasswordSuccess();
                            } else {        // reset failed
                                loadingFinish();
                                showErrorMessage(getString(R.string.teeter_signup_warning_password_reset_failed));
                            }
                        }
                    });


                } else {    // parseUser not found
                    loadingFinish();
                    showErrorMessage(getString(R.string.teeter_signup_warning_password_reset_user_not_found));
                }
            }
        });
    }


    private void showErrorMessage(String str){
        signInHelpErrorMessage.setText("");
        signInHelpErrorMessage.setText(str);
    }

    private void resetPasswordSuccess() {
        onSignInHelpSuccessListener.onLogInHelpSuccess();
    }

}
