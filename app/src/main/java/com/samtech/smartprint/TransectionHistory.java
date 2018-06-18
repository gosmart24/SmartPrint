package com.samtech.smartprint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TransectionHistory extends AppCompatActivity {

    ArrayList<HistoryModel> historyList;
    ListView lsView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transection_history);
        lsView = (ListView) findViewById(R.id.listview);

        setup();
    }

    public void setup() {
        boolean isNetworkOn = functions.checkConnection(TransectionHistory.this);
        if (isNetworkOn) {
            DialogInterface.OnCancelListener oncancelpro = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    startActivity(new Intent(TransectionHistory.this, Home.class));
                }
            };
            String accountno = functions.getPref(TransectionHistory.this, "accountno");
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        boolean success = object.getBoolean("success");
                        if (success) {
                            JSONArray jsonArray = object.getJSONArray("history");
                            historyList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                HistoryModel model = new HistoryModel();
                                model.setHistoryAmount(jsonObject.getString("amount"));
                                model.setHistoryDate(jsonObject.getString("transaction_date"));
                                model.setHistoryType(jsonObject.getString("transaction_type"));
                                historyList.add(model);
                            }
                            lsView.setAdapter(new HistoryAdapter(TransectionHistory.this, historyList));
                        } else {
                            String msg = object.getString("message");
                            DialogInterface.OnCancelListener onCancel = new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    functions.showmsg(TransectionHistory.this, "Request Status", "Request was interrupted!", null);
                                }
                            };
                            functions.showmsg(TransectionHistory.this, "Request Status", msg, onCancel);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            progressDialog = ProgressDialog.show(TransectionHistory.this, null, "Loading Transaction Histories please wait...", true, true, oncancelpro);
            HistoryConnection connection = new HistoryConnection(accountno, listener);
            RequestQueue queue = Volley.newRequestQueue(TransectionHistory.this);
            queue.add(connection);

        } else {
            functions.NetworkAlert(TransectionHistory.this);
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
        inflater.inflate(R.menu.loginexit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exitApp) {
            functions.deleteCache(TransectionHistory.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


}

