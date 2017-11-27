package com.example.seok.alone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

/**
 * Created by Seok on 2016-02-13.
 */
public class FirstActivity extends Activity {

    OAuthLogin mOAuthLoginModule;
    OAuthLoginButton mOAuthLoginButton;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        mContext = this;

        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(FirstActivity.this, "CEbefw3w8j5S2QQeiF8S", "J4z9dt2wTt", "Alone");

        mOAuthLoginButton = (OAuthLoginButton)findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);


    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(mContext);
                String refreshToken = mOAuthLoginModule.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginModule.getExpiresAt(mContext);
                String tokenType = mOAuthLoginModule.getTokenType(mContext);
                Log.i("NAVER", accessToken + "/" + refreshToken + "/" + expiresAt + "/" + tokenType + "");

                /*Handler mainChangeHandler = new Handler();
                mainChangeHandler.postDelayed(new Runnable() {
                    public void run() {*/
                        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                        intent.putExtra("token", tokenType+" "+accessToken);
                        startActivity(intent);

                        overridePendingTransition(R.anim.fade, R.anim.hold);

                        finish();/*
                    }
                }, 2000);*/

            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };
}