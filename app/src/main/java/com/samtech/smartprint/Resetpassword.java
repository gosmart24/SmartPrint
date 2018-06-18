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

public class Resetpassword extends AppCompatActivity {

    EditText edNewpass, edconfirmPass;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        edNewpass = (EditText) findViewById(R.id.et_newpass_Reset);
        edconfirmPass = (EditText) findViewById(R.id.et_confirmpass_Reset);
        display = (TextView) findViewById(R.id.tv_error_msg_Reset);
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
            functions.deleteCache(Resetpassword.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


    public void onResetpass(View view) {
        String newpass = edNewpass.getText().toString();
        String confirmPass = edconfirmPass.getText().toString();
        if (!newpass.isEmpty() && !confirmPass.isEmpty()) {
            if (newpass.equals(confirmPass)) {
                functions.removePrefs(Resetpassword.this,"password");
                functions.insert(Resetpassword.this,"password",confirmPass);
                functions.showmsg(Resetpassword.this,"Reset Status","Password successfully Change",null);
                startActivity(new Intent(Resetpassword.this,Login.class));
            } else {
                display.setText("password did not match!");
            }
        } else {
            display.setText("Required field cannot be empty!");
        }
    }
}
