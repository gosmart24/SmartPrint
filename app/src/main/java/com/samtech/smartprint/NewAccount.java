package com.samtech.smartprint;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class NewAccount extends AppCompatActivity implements View.OnClickListener {

    EditText etSurname, etFname, etOthername, etDOB, etAddress, etPhone;
    TextView display;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        etSurname = (EditText) findViewById(R.id.et_newSurname);
        etFname = (EditText) findViewById(R.id.et_newFname);
        etOthername = (EditText) findViewById(R.id.et_newOthername);
        etDOB = (EditText) findViewById(R.id.et_newDOB);
        etAddress = (EditText) findViewById(R.id.et_newAddress);
        display = (TextView) findViewById(R.id.tv_newDisplay);
        etPhone = (EditText) findViewById(R.id.et_newPhone);
        etSurname.setOnClickListener(this);
        etAddress.setOnClickListener(this);
        etDOB.setOnClickListener(this);
        etFname.setOnClickListener(this);

    }

    public void createAccount(View view) {
        if (!etPhone.getText().toString().isEmpty() && !etSurname.getText().toString().isEmpty() && !etFname.getText().toString().isEmpty() && !etAddress.getText().toString().isEmpty() && !etDOB.getText().toString().isEmpty()) {
            final String surName = etSurname.getText().toString();
            final String fname = etFname.getText().toString();
            String othername = etOthername.getText().toString();
            String DOB = etDOB.getText().toString();
            String Address = etAddress.getText().toString();
            final String phone = etPhone.getText().toString();
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        final String accountNo = jsonObject.getString("accountno");
                        boolean success = jsonObject.getBoolean("success");
                        String msg = "Congratulations " + fname + "! welcome to the royal family\nYour new Account number is: " + accountNo
                                + "\nDo you want to continue with Mobile banking Registration?";
                        String message = "Congratulations " + fname + " " + surName + "! welcome to the ROYAL FAMILY Your new Account number is: " + accountNo;
                        if (success) {
                            Response.Listener<String> onSMSsend = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.contains("OK")) {
                                        Toast.makeText(NewAccount.this, "SMS sent Successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(NewAccount.this, "SMS not sent", Toast.LENGTH_LONG).show();
                                    }
                                }
                            };
                            ConnectionRequest request = new ConnectionRequest(message, phone, onSMSsend);
                            RequestQueue queue = Volley.newRequestQueue(NewAccount.this);
                            queue.add(request);
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewAccount.this);
                            DialogInterface.OnClickListener onYes = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent register = new Intent(NewAccount.this, Register.class);
                                    register.putExtra("accountno", accountNo);
                                    register.putExtra("phoneno", phone);
                                    register.putExtra("key", "new");
                                    startActivity(register);
                                    finish();
                                }
                            };
                            DialogInterface.OnClickListener onNo = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(NewAccount.this, Welcome.class));
                                    finish();
                                }
                            };
                            builder.setCancelable(true)
                                    .setPositiveButton("Yes", onYes)
                                    .setMessage(msg)
                                    .setNegativeButton("No", onNo)
                                    .show();
                        } else {
                            functions.showmsg(NewAccount.this, null, jsonObject.getString("message"), null);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            DialogInterface.OnCancelListener cansel = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    functions.showClose(NewAccount.this, null, "Process interrupted ", null);
                }
            };
            progressDialog = ProgressDialog.show(NewAccount.this, null, "Registration in progress Please wait...", true, true, cansel);
            NewAccountConnection connection = new NewAccountConnection(surName, fname, othername, DOB, Address, phone, listener);
            RequestQueue queue = Volley.newRequestQueue(NewAccount.this);
            queue.add(connection);

        } else {
            display.setText(R.string.requiredfields);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        functions.deleteCache(NewAccount.this);
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
            functions.deleteCache(NewAccount.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        display.setText("");
    }
}
