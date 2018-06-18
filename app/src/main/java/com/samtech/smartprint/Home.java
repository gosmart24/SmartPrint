package com.samtech.smartprint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Home extends Activity {
    SharePrefs sharePrefs;

    //private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharePrefs = new SharePrefs(this);
    }

    public void openMyAccount(View view) {
        startActivity(new Intent(Home.this, MyAccount.class));
        finish();
    }

    public void openCheckBalance(View view) {
        startActivity(new Intent(Home.this, CheckBalance.class));
        finish();
    }

    public void openBuyAirtime(View view) {
        startActivity(new Intent(Home.this, BuyAirtime.class));
        finish();
    }

    public void openMakeTransfer(View view) {
        startActivity(new Intent(Home.this, MakeTransfer.class));
        finish();
    }

    public void openTransectionH(View view) {
        startActivity(new Intent(Home.this, TransectionHistory.class));
        finish();
    }

    public void openBillList(View view) {
        startActivity(new Intent(Home.this, BillList.class));
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.homemenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            functions.deleteCache(Home.this);
            removeData();
            startActivity(new Intent(Home.this, Login.class));
            finish();
        } else if (item.getItemId() == R.id.exitApp) {
            functions.deleteCache(Home.this);
            removeData();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public void removeData() {
        sharePrefs.clear();

    }
}
