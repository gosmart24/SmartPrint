package com.samtech.smartprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Otp extends AppCompatActivity {
    EditText edOTP;
    TextView display;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        edOTP = (EditText) findViewById(R.id.et_confirm_transec_otp);
        display = (TextView) findViewById(R.id.tv_error_Otp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Otp.this, Home.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.loginexit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exitApp) {
            functions.deleteCache(Otp.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


    public void onConfirmTransection(View view) {
        String otp = edOTP.getText().toString();
        String savedOTP = functions.getPref(Otp.this, "otp");
        if (!otp.isEmpty()) {
            if (!otp.equals("")) {
                if (otp.equals(savedOTP)) {
                    String key = getIntent().getStringExtra("key");
                    if (key.equals("bill")) {
                        functions.payments(Otp.this, getIntent().getBundleExtra("data"));
                    } else if (key.equals("transfer")) {
                        functions.startConnection(Otp.this, getIntent().getBundleExtra("data"));
                    } else if (key.equals("airtime")) {
                        functions.buyAirtime(Otp.this, getIntent().getBundleExtra("data"));
                    }
                } else {
                    if (count >= 3) {
                        count = 0;
                        startActivity(new Intent(Otp.this, Home.class));
                    }
                    display.setText("Invalid otp!");
                    count++;
                }
            } else {
                display.setText(R.string.otpempty);
            }
        } else {
            display.setText(R.string.requiredfields);
        }
    }

    public void onReset(View view) {
        startActivity(new Intent(Otp.this, Home.class));
        finish();
    }
}
