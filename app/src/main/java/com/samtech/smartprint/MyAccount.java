package com.samtech.smartprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MyAccount extends AppCompatActivity {

    TextView edsurname, edfname, edaccount, edusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        edsurname = (TextView) findViewById(R.id.tv_surname_profile);
        edfname = (TextView) findViewById(R.id.tv_fName_profile);
        edaccount = (TextView) findViewById(R.id.tv_AccountNo_profile);
        edusername = (TextView) findViewById(R.id.tv_username_profile);
       setData();
    }

    // setting Data to the fields.
    public void setData() {
        String fname = functions.getPref(MyAccount.this, "fname") ;
        edfname.setText(fname);
        String surname = functions.getPref(MyAccount.this, "surname");
        edsurname.setText(surname);
        String account = functions.getPref(MyAccount.this, "accountno");
        edaccount.setText(account);
        String username = functions.getPref(MyAccount.this, "username");
        edusername.setText(username);
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
            functions.deleteCache(MyAccount.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

}
