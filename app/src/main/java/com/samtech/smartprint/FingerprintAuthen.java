package com.samtech.smartprint;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;

public class FingerprintAuthen extends AppCompatActivity implements FingerPrintAuthCallback {
    FingerPrintAuthHelper authHelper;
    ImageView icon;
    TextView tv_messge;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        tv_messge = (TextView) findViewById(R.id.tv_msg_fingerprint);
        icon = (ImageView) findViewById(R.id.testicon);
        authHelper = FingerPrintAuthHelper.getHelper(this, this);
        count = 0;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FingerprintAuthen.this, Home.class));
    }

    @Override
    public void onNoFingerPrintHardwareFound() {
        Toast.makeText(FingerprintAuthen.this, "No FingerPrint Hardware Found!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoFingerPrintRegistered() {
        Toast.makeText(FingerprintAuthen.this, "Please Register a fingerprint!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Settings.ACTION_SECURITY_SETTINGS));
    }

    @Override
    public void onBelowMarshmallow() {
        Toast.makeText(FingerprintAuthen.this, "Your device is below FingerPrint Compatibility!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {
       // Toast.makeText(FingerprintAuthen.this, "Success! you are welcome.", Toast.LENGTH_LONG).show();
        if (getIntent().getStringExtra("key").equals("bill")) {
            functions.payments(FingerprintAuthen.this, getIntent().getBundleExtra("data"));
        } else if (getIntent().getStringExtra("key").equals("transfer")) {
            functions.startConnection(FingerprintAuthen.this, getIntent().getBundleExtra("data"));
        } else if (getIntent().getStringExtra("key").equals("airtime")) {
            functions.buyAirtime(FingerprintAuthen.this, getIntent().getBundleExtra("data"));
        }
    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {
        Toast.makeText(FingerprintAuthen.this, "authentication failed!", Toast.LENGTH_SHORT).show();
        icon.setImageResource(R.mipmap.fingerprintfailed);
        tv_messge.setText(R.string.failed);
        count++;
        if (count >= 3) {
            startActivity(new Intent(FingerprintAuthen.this, Home.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        authHelper.startAuth();
    }

    @Override
    protected void onPause() {
        super.onPause();
        authHelper.stopAuth();
    }

}