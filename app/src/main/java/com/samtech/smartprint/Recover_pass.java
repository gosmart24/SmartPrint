package com.samtech.smartprint;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Recover_pass extends AppCompatActivity {
    EditText edaccounNo;
    TextView display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pass);
        edaccounNo =(EditText)findViewById(R.id.et_acc_recover_pass);
        display = (TextView)findViewById(R.id.tv_error_recover_pass);
    }

    public void openpassReset(View view) {
        String input = edaccounNo.getText().toString();
        String accountno = functions.getPref(Recover_pass.this,"accountno");
        if (!input.isEmpty()){
            if (accountno.equals(input)){
                startActivity(new Intent(Recover_pass.this,Resetpassword.class));
            }else {
                display.setText("Invalide Account Number!");
            }
        }else {
            display.setText("Require field cannot be empty!");
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
            functions.deleteCache(Recover_pass.this);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }

}
