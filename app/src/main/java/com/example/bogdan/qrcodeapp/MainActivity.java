package com.example.bogdan.qrcodeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bogdan.qrcodeapp.server.facade.ServerFacade;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.zxing.Result;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends FragmentActivity implements ZXingScannerView.ResultHandler {

    @Bind(R.id.login_button)
    LoginButton loginButton;

    @Bind(R.id.info)
    TextView textView;

    @Bind(R.id.scanQrButton)
    Button qrReaderButton;

    @Bind(R.id.qrScannerLayout)
    LinearLayout qrScanneLayout;

    private ZXingScannerView mScannerView;

    private CallbackManager callbackManager;

    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        accessToken = loginResult.getAccessToken().getToken();
                        textView.setText(
                                "User ID: "
                                        + loginResult.getAccessToken().getUserId()
                                        + "\n" +
                                        "Auth Token: "
                                        + accessToken
                        );
                        qrReaderButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancel() {
                        textView.setText("Login attempt canceled.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mScannerView != null) {
            mScannerView.stopCamera();
            loginButton.setVisibility(View.VISIBLE);
            qrReaderButton.setVisibility(View.VISIBLE);
        }
    }

    public void performQrScanner(View view) {
        loginButton.setVisibility(View.GONE);
        qrScanneLayout.setVisibility(View.VISIBLE);

        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {
        // Do something with the result here
        Log.e("handler", result.getText()); // Prints scan results
        Log.e("handler", result.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        // show the scanner result into dialog box.
        mScannerView.stopCamera();
        try {
            ServerFacade.sendRegisterUserToQRCode(this, accessToken, result.getText());
            qrScanneLayout.setVisibility(View.GONE);
            setContentView(R.layout.activity_main);
            qrReaderButton.setVisibility(View.VISIBLE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("Main Activity", e.getMessage(), e);
            qrScanneLayout.setVisibility(View.GONE);
            setContentView(R.layout.activity_main);
            qrReaderButton.setVisibility(View.VISIBLE);
        }
    }
}
