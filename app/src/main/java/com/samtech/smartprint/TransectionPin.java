package com.samtech.smartprint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TransectionPin extends AppCompatActivity {

    EditText edpin;
    TextView display;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transection_pin);
        edpin = (EditText) findViewById(R.id.et_trasectionPin);
        display = (TextView) findViewById(R.id.tv_error_TrasectionPin);
        edpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        count = 0;
    }

    public void ConfirmTransaction(View view) {
        String pin = functions.codeMD5(edpin.getText().toString());
        String savedPin = functions.getPref(TransectionPin.this, "pin");
        if (!pin.isEmpty()) {
            if (!savedPin.isEmpty()) {
                if (pin.equals(savedPin)) {
                    // successful login...
                    if (getIntent().getStringExtra("key").equals("bill")) {
                        if (getIntent().getIntExtra("amount", 0) <= 4999) {
                            functions.payments(TransectionPin.this, getIntent().getBundleExtra("data"));
                        } else {
                            functions.sendOTPGen(TransectionPin.this, getIntent().getBundleExtra("data"), getIntent().getStringExtra("key"));
                        }
                    } else if (getIntent().getStringExtra("key").equals("transfer")) {
                        functions.sendOTPGen(TransectionPin.this, getIntent().getBundleExtra("data"), getIntent().getStringExtra("key"));
                    } else if (getIntent().getStringExtra("key").equals("airtime")) {
                        if (getIntent().getIntExtra("amount", 0) >= 4999) {
                            functions.sendOTPGen(TransectionPin.this, getIntent().getBundleExtra("data"), getIntent().getStringExtra("key"));
                        } else {
                            functions.buyAirtime(TransectionPin.this, getIntent().getBundleExtra("data"));
                        }
                    }
                } else {
                    count++;
                    display.setText(R.string.invalidPin);
                    if (count >= 3) {
                        count = 0;
                        //failed login...
                        Toast.makeText(TransectionPin.this, "login failed!", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                display.setText(R.string.notregister);
            }
        } else {
            display.setText(R.string.requiredfields);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        functions.deleteCache(TransectionPin.this);
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
            functions.deleteCache(TransectionPin.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

}
