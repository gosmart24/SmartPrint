package com.samtech.smartprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //  Log.i("MYTAG","SERIAL : " + Build.SERIAL);
        //  Log.i("MYTAG","ID : " + Build.ID);
        //  Log.i("MYTAG","DEVICE : " + Build.DEVICE);
        //  Log.i("MYTAG","BOARD : " + Build.BOARD);
    }


    public void openLogin(View view) {
        startActivity(new Intent(Welcome.this, Login.class));
        finish();
    }

    public void openRegister(View view) {
        Intent register = new Intent(Welcome.this, Register.class);
        register.putExtra("key", "welcome");
        startActivity(register);
        finish();
    }

    public void openActivate(View view) {
        startActivity(new Intent(Welcome.this, Activate.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newaccount, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.newaccount) {
            startActivity(new Intent(Welcome.this, NewAccount.class));
            finish();
        } else if (item.getItemId() == R.id.exitApp) {
            functions.deleteCache(Welcome.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        functions.deleteCache(Welcome.this);
    }

    public void quickHelp(View view) {
        startActivity(new Intent(Welcome.this, QuickHelp.class));
        finish();
    }
}

// Done with Welcome class.