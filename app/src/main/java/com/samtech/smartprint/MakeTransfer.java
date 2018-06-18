package com.samtech.smartprint;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class MakeTransfer extends AppCompatActivity {

    private static String raccountno;
    private static String amount;
    EditText edraccountno, edamount;
    TextView display;
    Spinner banks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_transfer);
        edraccountno = (EditText) findViewById(R.id.et_accountNO_mTransfer);
        edamount = (EditText) findViewById(R.id.et_amount_mTransfer);
        display = (TextView) findViewById(R.id.display_mtransfer);
        banks = (Spinner) findViewById(R.id.spinner2);
        edraccountno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
    }

    public void onTransfer(View view) {
        if (!edraccountno.getText().toString().isEmpty() && !edamount.getText().toString().isEmpty()) {
            amount = edamount.getText().toString();
            raccountno = edraccountno.getText().toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(MakeTransfer.this);
            builder.setNegativeButton("NO", null);
            DialogInterface.OnClickListener onyes = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle data = new Bundle();
                    data.putString("raccountno", raccountno);
                    data.putString("amount", amount);
                    Intent pinIntent = new Intent(MakeTransfer.this, TransectionPin.class);
                    pinIntent.putExtra("key", "transfer");
                    pinIntent.putExtra("data", data);
                    Intent fingerPrint = new Intent(MakeTransfer.this, FingerprintAuthen.class);
                    fingerPrint.putExtra("key", "transfer");
                    fingerPrint.putExtra("data", data);
                    functions.checkFingerPrint(MakeTransfer.this, pinIntent, fingerPrint);
                    finish();
                }
            };
            builder.setPositiveButton("Yes", onyes);
            builder.setTitle("Transaction Alert!");
            builder.setMessage("You are about to Transfer \n Amount : " + amount + "\n To Recipient : "
                    + raccountno + "\n Bank: " + banks.getSelectedItem().toString() + "\n  Do you want to continue?");
            builder.setCancelable(false);
            builder.show();
        } else {
            display.setText(R.string.requiredfields);
            resetFields();
        }
    }

    private void resetFields() {
        edamount.setText("");
        edraccountno.setText("");
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
            functions.deleteCache(MakeTransfer.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

}
