package com.samtech.smartprint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity implements View.OnClickListener {

    SharePrefs sharePrefs;
    //static SharedPreferences sp;
    EditText edaccount, edusername, edpass, edconfirmpass, edemail, edphone, edpin, edconfirmpin;
    ConnectionRequest request;
    TextView tverror_msg;
    ProgressDialog progressDialog;
    String accountNO, username, pass, conPass, email, phoneNO, pin, conPin, loginPass, msg, devieSerial;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharePrefs = new SharePrefs(this);
        edaccount = (EditText) findViewById(R.id.et_accountNO_reg);
        edusername = (EditText) findViewById(R.id.et_username_reg);
        edpass = (EditText) findViewById(R.id.et_Pass_reg);
        edconfirmpass = (EditText) findViewById(R.id.et_Confirmpass_reg);
        edemail = (EditText) findViewById(R.id.et_email_reg);
        edphone = (EditText) findViewById(R.id.et_phoneNO_reg);
        edpin = (EditText) findViewById(R.id.et_transec_pin_reg);
        edconfirmpin = (EditText) findViewById(R.id.et_ConTransecPin_reg);
        tverror_msg = (TextView) findViewById(R.id.tv_error_reg);
        // sp = PreferenceManager.getDefaultSharedPreferences(Register.this);
        devieSerial = Build.ID;
        Log.i("MYTAG", "SERIAL : " + devieSerial);
        edaccount.setOnClickListener(this);
        edphone.setOnClickListener(this);
        edusername.setOnClickListener(this);
        edpass.setOnClickListener(this);
        edpin.setOnClickListener(this);
        edconfirmpass.setOnClickListener(this);
        edconfirmpin.setOnClickListener(this);
        key = getIntent().getStringExtra("key");
        if (key.equals("new")) {
            edaccount.setText(accountNO);
            edphone.setText(phoneNO);
        }
    }

    public void startConnection() {
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("MYTAG", response);
                // progressDialog.dismiss();
                try {
                    JSONObject responObject = new JSONObject(response);
                    boolean success = responObject.getBoolean("success");
                    Log.i("MYTAG", response);
                    msg = responObject.getString("message");
                    if (success) {
                        Log.i("MYTAG", "successfully registered");
                        Bundle data = new Bundle();
                        data.putString("phoneNO", phoneNO);
                        //  String fname = responObject.getString("firstname");
//                        String surname = responObject.getString("surname");
//                        data.putString(" pin", pin);
//                        data.putString("fname", fname);
//                        data.putString("surname", surname);
                        // data.putString("pass", pass);
                        data.putString("username", username);
                        data.putString("accountNO", accountNO);
                        functions.sendOTP(Register.this, data);
                        progressDialog.dismiss();
                        // finish();

                    } else {
                        Log.i("MYTAG", msg);
                        progressDialog.dismiss();
                        DialogInterface.OnCancelListener onerror = new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                startActivity(new Intent(Register.this, Welcome.class));
                                finish();
                            }
                        };
                        functions.showmsg(Register.this, "Registration Status", msg, onerror);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        DialogInterface.OnCancelListener oncansel = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                functions.showmsg(Register.this, "Registration Status", "Registration interupted!", null);
            }
        };

        progressDialog = ProgressDialog.show(Register.this, null, "Registration in progress please wait... ", true, true, oncansel);
        request = new ConnectionRequest(accountNO, username, email, phoneNO, functions.codeMD5(pin), functions.codeMD5(loginPass), devieSerial, listener);
        RequestQueue queue = Volley.newRequestQueue(Register.this);
        queue.add(request);

    }

    public void openActivation(View view) {
        accountNO = edaccount.getText().toString();
        username = edusername.getText().toString();
        pass = edpass.getText().toString();
        conPass = edconfirmpass.getText().toString();
        loginPass = edconfirmpass.getText().toString();
        email = edemail.getText().toString();
        phoneNO = edphone.getText().toString();
        pin = edpin.getText().toString();
        conPin = edconfirmpin.getText().toString();

        if (isValidinput(accountNO, username, pass, conPass, email, phoneNO, pin, conPin)) {
            startConnection();
        } else {
            tverror_msg.setText(msg);
            tverror_msg.setVisibility(View.VISIBLE);
        }

    }

    //checking if the fields are empty or not.
    public boolean isValidinput(String accountNO, String username, String pass, String conPass, String email, String phoneNO, String pin, String conPin) {
        if (!accountNO.isEmpty() && !username.isEmpty() && !pass.isEmpty() && !phoneNO.isEmpty() && !pin.isEmpty() && !conPass.isEmpty() && !conPin.isEmpty()) {
            if (pass.contentEquals(conPass)) {
                if (pin.contentEquals(conPin)) {
                    return true;
                } else {
                    msg = "Pin does not Match!";
                    resetFeilds(edpin, edconfirmpin);

                    return false;
                }
            } else {
                msg = "Password does not Match! ";
                resetFeilds(edpass, edconfirmpass);
                return false;
            }
        } else {
            msg = "Reguire field cannot be Empty";
            //resetFeilds();
            return false;
        }
    }

    private void resetFeilds(EditText ed1, EditText ed2) {
        ed1.setText("");
        ed2.setText("");

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
            functions.deleteCache(Register.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        tverror_msg.setText("");
    }
}