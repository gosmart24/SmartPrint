package com.samtech.smartprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class BuyAirtime extends AppCompatActivity {

    EditText edphone, edamount;
    CheckBox checkBox;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_airtime);
        checkBox = (CheckBox) findViewById(R.id.checkBox_buyAirtime);
        edphone = (EditText) findViewById(R.id.et_phone_buyAirtime);
        edamount = (EditText) findViewById(R.id.et_amount_BuyAirtime);
        display = (TextView) findViewById(R.id.tv_error_buyAirtime);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    String phoneNo = functions.getPref(BuyAirtime.this, "phoneno");
                    edphone.setText(phoneNo);
                } else {
                    edphone.setText("");
                }
            }
        });
    }

    public void openHomepage(View view) {
        final String phoneNo = edphone.getText().toString();
        final String amount = edamount.getText().toString();
        String pin = functions.getPref(BuyAirtime.this, "pin");
        String accountNo = functions.getPref(BuyAirtime.this, "accountno");
        if (!phoneNo.isEmpty() && !amount.isEmpty()) {
            Bundle data = new Bundle();
            data.putString("accountNo", accountNo);
            data.putString("amount", amount);
            data.putString("pin", pin);
            data.putString("phoneNo", phoneNo);
            Intent intent = new Intent(BuyAirtime.this, TransectionPin.class);
            intent.putExtra("key", "airtime");
            intent.putExtra("data", data);
            intent.putExtra("amount", Integer.parseInt(amount));
            Intent intentFinger = new Intent(BuyAirtime.this, FingerprintAuthen.class);
            intentFinger.putExtra("key", "airtime");
            intentFinger.putExtra("data", data);
            functions.checkFingerPrint(BuyAirtime.this, intent, intentFinger);
            finish();
        } else {
            display.setText(R.string.requiredfields);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,Home.class));
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
            functions.deleteCache(BuyAirtime.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
}
