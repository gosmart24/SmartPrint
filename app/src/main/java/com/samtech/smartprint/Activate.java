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

public class Activate extends AppCompatActivity {
    //SharedPreferences sp;
    SharePrefs sharePrefs;
    EditText edotp;
    TextView display;
    long timeUP = 1000 * 60 * 10; // 10 minutes wait for otp.
    Thread clearOTP = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(timeUP);
                sharePrefs.remove("otp");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                clearOTP.interrupt();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        sharePrefs = new SharePrefs(this);
        edotp = (EditText) findViewById(R.id.et_activationCode_activate);
        display = (TextView) findViewById(R.id.tv_error_Activation);
        //String phoneNo = functions.getPref(Activate.this, "phoneno");
        edotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        if (!clearOTP.isAlive()) {
            clearOTP.start();
        } else {
            clearOTP.interrupt();
            clearOTP.start();
        }
    }

    public void openHomepage(View view) {
        String otp = functions.getPref(Activate.this, "otp");
        Bundle data = getIntent().getBundleExtra("data");
        // otp = data.getString("otp");
        if (!otp.isEmpty()) {
            String input = edotp.getText().toString();
            if (!input.isEmpty()) {
                if (otp.equals(input)) {
//                    String accountNO = data.getString("accountNO", "");
//                    String username = data.getString("username", "");
//                    String pass = data.getString("pass", "");
//                    String phoneNO = data.getString("phoneNO", "");
//                    String pin = data.getString(" pin", "");
//                    String fname = data.getString("fname", "");
//                    String surname = data.getString("surname", "");
                    //boolean inserted = functions.insert(accountNO, username, pass, phoneNO, pin, fname, surname);
//                    if (inserted) {
//                        DialogInterface.OnCancelListener oncancel = new DialogInterface.OnCancelListener() {
//                            @Override
//                            public void onCancel(DialogInterface dialog) {
//
//                            }
//                        };
//                        functions.showmsg(Activate.this, "Status", "Activation Successful. \n Please login ", oncancel);
//
//                    } else {
//                        functions.showmsg(Activate.this, "Status", "Registration failed please register again!.", null);
//                    }
                    startActivity(new Intent(Activate.this, Login.class));
                    finish();
                } else {
                    display.setText(R.string.incorrectcode);
                }
            } else {
                display.setText(R.string.requiredfields);
            }
        } else {
            functions.sendOTP(Activate.this, data);
        }
    }

    public void onResendCode(View view) {
        String otp = functions.getPref(Activate.this, "otp");
        if (!otp.isEmpty()) {
            functions.removePrefs(Activate.this, "otp");
            functions.sendOTP(Activate.this, getIntent().getBundleExtra("data"));
            if (!clearOTP.isAlive()) {
                clearOTP.start();
            } else {
                clearOTP.interrupt();
                clearOTP.start();
            }
        } else {
            functions.sendOTP(Activate.this, getIntent().getBundleExtra("data"));
            if (!clearOTP.isAlive()) {
                clearOTP.start();
            } else {
                clearOTP.interrupt();
                clearOTP.start();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Home.class));
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
            functions.deleteCache(Activate.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
}
