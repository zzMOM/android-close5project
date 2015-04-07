package com.teeterpal.androidteeterpal.login;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.parse.Parse;

/**
 * Created by weiwu on 3/24/15.
 */
public class TeeterSignInFragmentBase extends Fragment {
    protected TeeterOnLoadingListener onLoadingListener;

    protected String getLogTag() {
        return null;
    }


    protected void loadingStart(boolean showSpinner) {
        if (onLoadingListener != null) {
            onLoadingListener.onLoadingStart(showSpinner);
        }
    }

    protected void loadingFinish() {
        if (onLoadingListener != null) {
            onLoadingListener.onLoadingFinish();
        }
    }

    protected void debugLog(int id) {
        debugLog(getString(id));
    }

    protected void debugLog(String text) {
        if (Parse.getLogLevel() <= Parse.LOG_LEVEL_DEBUG &&
                Log.isLoggable(getLogTag(), Log.WARN)) {
            Log.w(getLogTag(), text);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected boolean isActivityDestroyed() {
        FragmentActivity activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity == null || activity.isDestroyed();
        } else {
            return activity == null || ((TeeterSignInSignUpActivity) activity).isDestroyed();
        }
    }
}
