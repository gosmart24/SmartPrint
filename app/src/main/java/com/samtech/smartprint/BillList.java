package com.samtech.smartprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BillList extends AppCompatActivity {

    static EditText edamount;
    static TextView display;
    static Spinner billSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);
        edamount = (EditText) findViewById(R.id.et_bill_amount);
        display = (TextView) findViewById(R.id.tv_bill);
        billSpinner = (Spinner) findViewById(R.id.spinner3);
    }

    public void payBills(View view) {
        Bundle data = new Bundle();
        final String amount = edamount.getText().toString();
        if (!amount.isEmpty()) {
            String account = functions.getPref(BillList.this, "accountno");
            String pin = functions.getPref(BillList.this, "pin");
            String item = billSpinner.getSelectedItem().toString();
            data.putString("account", account);
            data.putString("amount", amount);
            data.putString("pin", pin);
            data.putString("item", item);
            Intent fingerPrint = new Intent(BillList.this, FingerprintAuthen.class);
            fingerPrint.putExtra("key", "bill");
            fingerPrint.putExtra("data", data);
            Intent pinIntent = new Intent(BillList.this, TransectionPin.class);
            pinIntent.putExtra("key", "bill");
            pinIntent.putExtra("amount", Integer.parseInt(amount));
            pinIntent.putExtra("data", data);
            functions.checkFingerPrint(BillList.this, pinIntent, fingerPrint);
            finish();
        } else {
            display.setText(R.string.requiredfields);
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
            functions.deleteCache(BillList.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
}
