package com.samtech.smartprint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class Login extends AppCompatActivity {
    EditText edusername, edpass;
    TextView display;
    String accountNO, fname, surname, phoneNO, pin;
    //SharedPreferences sp;
    SharePrefs sharePrefs;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharePrefs = new SharePrefs(this);
        edusername = (EditText) findViewById(R.id.et_username_login);
        edpass = (EditText) findViewById(R.id.et_pin_login);
        display = (TextView) findViewById(R.id.tv_error_msg_login);
        edusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        //sp = PreferenceManager.getDefaultSharedPreferences(this);
        edusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
        edpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                display.setText("");
            }
        });
    }

    private void resetFields() {
        edusername.setText("");
        edpass.setText("");
    }

    public void openHome(View view) {
        String username_input = edusername.getText().toString().trim();
        String passe_input = edpass.getText().toString().trim();
        if (!username_input.isEmpty() && !passe_input.isEmpty()) {
            progressDialog = ProgressDialog.show(Login.this, null, "Login in progress...please wait", false, true);
            login(username_input, passe_input);
            resetFields();
        } else {
            display.setText(R.string.notregistered);
            resetFields();
        }
    }

    private void login(final String username, String password) {
        Log.i("MYTAG", "in Login");
        Response.Listener<String> listerner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        Log.i("MYTAG", "Successful response" + response);
                        accountNO = jsonObject.getString("accountno");
                        phoneNO = jsonObject.getString("phoneno");
                        fname = jsonObject.getString("fname");
                        surname = jsonObject.getString("surname");
                        pin = jsonObject.getString("pin");
                        if (insert(accountNO, username, phoneNO, fname, surname, pin)) {
                            startActivity(new Intent(Login.this, Home.class));
                            Toast.makeText(Login.this, "Welcome to Kingsbank", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            display.setText(R.string.unknownerror);
                        }

                    } else {
                        display.setText(jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.i("MYTAG", "logs : errors msg : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
        NewAccountConnection loginConnection = new NewAccountConnection(username, functions.codeMD5(password), listerner);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(loginConnection);
    }

    public boolean insert(String accountNO, String username, String phoneNO, String fname, String surname, String pin) {

        try {

            sharePrefs.putString("accountno", accountNO);
            sharePrefs.putString("username", username);
            sharePrefs.putString("phoneno", phoneNO);
            sharePrefs.putString("fname", fname);
            sharePrefs.putString("surname", surname);
            sharePrefs.putString("pin", pin);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeData() {
        sharePrefs.clear();
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
            functions.deleteCache(Login.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(Login.this, "Thank you for banking with Us...", Toast.LENGTH_SHORT).show();
        System.exit(0);
    }


    public void forgetPin(View view) {
        startActivity(new Intent(Login.this, Recover_pass.class));
    }
}
