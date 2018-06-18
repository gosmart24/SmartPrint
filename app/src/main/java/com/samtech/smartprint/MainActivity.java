package com.samtech.smartprint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    ProgressBar loadingProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadingProgress = (ProgressBar) findViewById(R.id.progressBar);
        loadingProgress.setIndeterminate(true);
        functions.checkConnection(MainActivity.this);
        Thread welcomethread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                    // start welcome Activity and finish(); the splash screen.
                    startActivity(new Intent(MainActivity.this, Welcome.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    finish();
                }
            }
        };
        welcomethread.start();
    }
}
