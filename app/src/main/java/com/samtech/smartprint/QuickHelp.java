package com.samtech.smartprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class QuickHelp extends AppCompatActivity implements  AdapterView.OnItemClickListener {

    ListView baseView;
    List<String> list = new ArrayList<>(10);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_help);
        baseView = (ListView) findViewById(R.id.baseHelp);
        list.add("Get Started");
        list.add("Creating a new bank Account ");
        list.add("KingsMobile Registration");
        list.add("Activating KingsMobile");
        list.add("how to Login into KingsMobile");
        list.add("How to pay for Bills");
        list.add("How to make Transfers");
        list.add("How to buy Airtime");
        baseView.setAdapter(new QuickHelpAdapter(QuickHelp.this, list));
        baseView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(QuickHelp.this, HelpGetstarted.class));
                break;
            case 1:
                startActivity(new Intent(QuickHelp.this, HelpCreateAccount.class));
                break;
            case 2:
                startActivity(new Intent(QuickHelp.this, HelpRegister.class));
                break;
            case 3:
                startActivity(new Intent(QuickHelp.this, HelpActivate.class));
                break;
            case 4:
                startActivity(new Intent(QuickHelp.this, HelpLogin.class));
                break;
            case 5:
                startActivity(new Intent(QuickHelp.this, HelpBills.class));
                break;
            case 6:
                startActivity(new Intent(QuickHelp.this, HelpTransfer.class));
                break;
            case 7:
                startActivity(new Intent(QuickHelp.this, HelpAirtime.class));
                break;

        }
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
            functions.deleteCache(QuickHelp.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

}
