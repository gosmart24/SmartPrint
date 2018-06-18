package com.samtech.smartprint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckBalance extends AppCompatActivity {

    TextView edsurname, edaccounno, edbalance, eddate;
    ConnectionRequest request;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_balance);
        edsurname = (TextView) findViewById(R.id.tv_surname_checkbal);
        edaccounno = (TextView) findViewById(R.id.tv_accountNo_checkBal);
        edbalance = (TextView) findViewById(R.id.balance);
        eddate = (TextView) findViewById(R.id.tv_dateTime_checkBal);
        edsurname.setText("Mr. " + functions.getPref(CheckBalance.this, "surname"));
        edaccounno.setText("Balance on Account No : " + functions.getPref(CheckBalance.this, "accountno"));
        boolean isNetworkOn = functions.checkConnection(CheckBalance.this);
        if (isNetworkOn) {
            getBalance();
        } else {
            functions.NetworkAlert(CheckBalance.this);
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
        inflater.inflate(R.menu.balancerefresh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.balanceRefresh) {
            boolean isNetworkOn = functions.checkConnection(CheckBalance.this);
            if (isNetworkOn) {
                getBalance();
            } else {
                Toast.makeText(CheckBalance.this, "No network Connectivity", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.exitApp) {
            functions.deleteCache(CheckBalance.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getBalance() {
        DialogInterface.OnCancelListener oncancel = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                functions.showmsg(CheckBalance.this, "Transaction Interrupted!", null);
            }
        };
        progressDialog = ProgressDialog.show(CheckBalance.this, null, "Retrieving Balance please wait...", true, true, oncancel);
        Response.Listener<String> listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String balance = jsonObject.getString("balance");
                    String date = jsonObject.getString("currentdate");
                    if (success) {
                        eddate.setText(date);
                        edbalance.setText(balance);
                    } else {
                        edbalance.setText("Balance Unavailable Please try again later!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        // request.setRetryPolicy(new DefaultRetryPolicy(30000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        String accountNo = functions.getPref(CheckBalance.this, "accountno");
        request = new ConnectionRequest(accountNo, listener);
        RequestQueue queue = Volley.newRequestQueue(CheckBalance.this);
        queue.add(request);
    }


}
