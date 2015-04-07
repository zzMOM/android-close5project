package com.teeterpal.androidteeterpal.login;

import android.telephony.SmsManager;
import android.util.Log;

import com.parse.ParseObject;
import com.teeterpal.androidteeterpal.data.TeeterParseContract;

import java.util.Random;

/**
 * Created by weiwu on 3/27/15.
 */
public class TeeterSignInSignUpHelper {
    TeeterSignInSignUpHelper(){}

    public boolean sentAndSavePhoneVerificationCode(String phoneNumber) {
        boolean isSuccess = false;
        // Generate 6 digits 100000 ~ 999999 vefication code
        Random random = new Random();
        String verificationCode = String.valueOf(100000 + random.nextInt(900000));

        // save in parse object
        ParseObject phoneVerificationCodeObject = new ParseObject(
                TeeterParseContract.phoneVerificationEntry.PHONE_VERIFICATION_OBJECT);
        phoneVerificationCodeObject.put(
                TeeterParseContract.phoneVerificationEntry.PHONE_VERIFICATION_OBJECT_PHONE_NUMBER, phoneNumber);
        phoneVerificationCodeObject.put(
                TeeterParseContract.phoneVerificationEntry.PHONE_VERIFICATION_OBJECT_VERIFICATION_CODE, verificationCode);
        phoneVerificationCodeObject.saveInBackground();

        // Send SMS
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, verificationCode, null, null);
            Log.e("SMS sent", phoneNumber + " " + verificationCode);
            isSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isSuccess;
    }
}
