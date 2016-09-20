package com.congnt.androidbasecomponent.utility;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Recreated by congnt on 9/20/16.
 */

/**
 * Util to sign in and sign out Google account
 */
public class GoogleConnector implements GoogleApiClient.OnConnectionFailedListener {
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount mGoogleAccount;
    private String mEmail;

    /**
     * Builder some information to sign in Google
     *
     * @param activity
     */
    public GoogleConnector(FragmentActivity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    /**
     * Sign in by Google account
     *
     * @param activity
     */
    public void signIn(Activity activity) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    /**
     * Handler the intent and result after login Google
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @return
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean isSignInSuccess = false;
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                mGoogleAccount = result.getSignInAccount();
                mEmail = mGoogleAccount.getEmail();
                isSignInSuccess = true;
                LogUtil.d("Google sign in success, email: ", mEmail);
            } else {
                // Google Sign In failed, update UI appropriately
                LogUtil.e("Google sign in failure");
            }
        }
        return isSignInSuccess;
    }

    /**
     * Get Google email when sign in success
     *
     * @return Google email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Get Google account when sign in success
     *
     * @return Google account
     */
    public GoogleSignInAccount getGoogleAccount() {
        return mGoogleAccount;
    }

    /**
     * Sign out Google account at Sign in activity
     */
    public void signOut() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}