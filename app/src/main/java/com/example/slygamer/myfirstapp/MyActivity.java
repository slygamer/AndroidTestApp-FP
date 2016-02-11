package com.example.slygamer.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.pass.Spass;
import com.samsung.android.sdk.pass.SpassFingerprint;
import com.samsung.android.sdk.pass.SpassInvalidStateException;

public class MyActivity extends AppCompatActivity {

    // Key for message that is input to textbox
    public final static String EXTRA_MESSAGE = "com.example.slygamer.myfirstapp.MESSAGE";

    // Tag for log messages showing which Application/Activity it came from
    private final static String TAG = "MyFirstApp/MyActivity: ";

    public Button sendButton;

    // Variables needed for Samsung Galaxy Fingerprint
    private Spass sPass;
    private SpassFingerprint sPassFingerprint;
    //private Context context;

    private SpassFingerprint.IdentifyListener listener = new SpassFingerprint.IdentifyListener()
    {
        @Override
        public void onFinished(int i)
        {
            Log.d(TAG, "Fingerprint sensor has finished identifying user");
            if(i == SpassFingerprint.STATUS_AUTHENTIFICATION_SUCCESS)
            {
                sendButton = (Button) findViewById(R.id.send);
                sendButton.setEnabled(true);
                Log.d(TAG, "Authentication Successful!");
            }
            else
            {
                Log.d(TAG, "Authentication Failed");
            }
        }

        @Override
        public void onReady()
        {
            Log.d(TAG, "Fingerprint sensor ready");
        }

        @Override
        public void onStarted()
        {
            Log.d(TAG, "Fingerprint sensor started");
        }

        @Override
        public void onCompleted()
        {

        }
    };

    // Booleans for checking the Fingerprint features being enabled on phone
    private boolean regFinger= false;
    private boolean hasFingerprintEnabled = false;

    // onCreate method is called as soon as Activity is called (aka. as soon as Application starts)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Disable Send button for message at first
        sendButton = (Button) findViewById(R.id.send);
        sendButton.setEnabled(false);

        // Initialize the Samsung Galaxy Fingerprint stuff
        //context = this;
        sPass = new Spass();
        try{
            sPass.initialize(MyActivity.this);
        } catch(SsdkUnsupportedException e){
            Log.d(TAG, "Exception thrown " + e);
        } catch(UnsupportedOperationException e){
            Log.d(TAG, "Fingerprint Service not supported on device");
        }

        hasFingerprintEnabled = sPass.isFeatureEnabled(Spass.DEVICE_FINGERPRINT);

        if(hasFingerprintEnabled){
            sPassFingerprint = new SpassFingerprint(MyActivity.this);
            regFinger = sPassFingerprint.hasRegisteredFinger();

            // If there is a registered finger, then pull up prompt
            if(regFinger)
            {
                sPassFingerprint.startIdentifyWithDialog(MyActivity.this, listener, false);
            }
            else
            {
                Log.d(TAG, "No registered fingerprints detected");
            }
        }
        else
        {
            Log.d(TAG, "Fingerprint service not supported");
        }
    }

    public void onPause()
    {
        super.onPause();
    }

    public void onResume()
    {
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Called when user clicks the Send button
    public void sendMessage(View view){
        // Do something in response to button

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
