package edu.uw.team7project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import edu.uw.team7project.util.SharedPref;


import edu.uw.team7project.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

/**
 * An activity representing the authentication process for the application.
 *
 * @author Yousif Azami
 * @author Trevor Nichols
 */
public class AuthActivity extends AppCompatActivity {
//    private Switch mSwitch;
//    SharedPref sharedPref;
    private final int READ_WRITE_PERMISSION_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

//        sharedPref = new SharedPref(this);
//
//        if(sharedPref.loadNightModeState()==true) {
//            setTheme(R.style.DarkTheme);
//        } else setTheme(R.style.AppTheme);
//
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_auth);
//        mSwitch = (Switch)findViewById(R.id.mySwitch);
//
//        if(sharedPref.loadNightModeState()==true) {
//            mSwitch.setChecked(true);
//        }
//
//        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    sharedPref.setNightModeState(true);
//                    recreate();
//                }
//                else {
//                    sharedPref.setNightModeState(false);
//                    recreate();
//                }
//            }
//        });



        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);
        // Check whether the user has granted us the READ/WRITE_EXTERNAL_STORAGE permissions
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
            // Pushy SDK will be able to persist the device token in the external storage
            ActivityCompat.requestPermissions(this,
                    new String[]
                            { Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    READ_WRITE_PERMISSION_CODE);
        } else {
            //User has already granted permission, go retrieve the token.
            initiatePushyTokenRequest();
        }
    }

    /**
     * initiates the pushy token requests
     */
    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_WRITE_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! We may continue...
                // go retrieve the token.
                initiatePushyTokenRequest();
            } else {
                // permission denied, boo!
                // app requires this for Pushy related tasks.
                // end app
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This App requires External Read Write permissions to function")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Close the application, user denied required permissions
                                finishAndRemoveTask();
                            }
                        }).create().show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

//    public void restartApp() {
//        Intent i = new Intent(getApplicationContext(), AuthActivity.class);
//        startActivity(i);
//        finish();
//    }
}
