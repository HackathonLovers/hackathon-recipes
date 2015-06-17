package com.h4ckademy.lextrend.lextrendcamera;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


public class Mjpeg_Activity extends ActionBarActivity {


    private SurfaceFragment surfaceFragment;
    private AlertDialog.Builder dialog;
    private Button changeableButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mjpeg_);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        surfaceFragment = SurfaceFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, surfaceFragment)
                .commit();

        createDialog();
        changeableButton = (Button) findViewById(R.id.changeableButton);

        Intent serverService = new Intent(this, NotificationService.class);
        startService(serverService);

    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean isTextStart = "Start".equals(changeableButton.getText());
        if (!isTextStart) {
            surfaceFragment.start(this);
        }
    }

    public void startStop(View v){
        if ( isConnection() ) {
            changeButton();
        }
        else {
            dialog.show();
        }
    }

    public void screenshot(View v){
        Bitmap picture = surfaceFragment.takeScreenshot();
        if (picture != null) {
            ScreenShot screenShotClass = new ScreenShot(findViewById(R.id.container), this, picture);
            screenShotClass.obtainScreenshot();
        }
        else {
            Toast toast = Toast.makeText (this, "There is no image to save", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mjpeg_, menu);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.container);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            frameLayout.getLayoutParams().height = -1;
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){

            frameLayout.getLayoutParams().height = 0;
        }
    }

    public boolean isConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public void createDialog(){
        dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Unable to connect");
        dialog.setMessage("Please check your Internet connection");
        final Intent settings = new Intent(Settings.ACTION_SETTINGS);
        dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(settings);
            }
        });

    }

    public void changeButton() {
        boolean isTextStart = "Start".equals(changeableButton.getText());

        if(isTextStart) {
            changeableButton.setText("Stop");
            surfaceFragment.start(this);
        }
        else {
            changeableButton.setText("Start");
            surfaceFragment.onStop();
        }
    }


}






