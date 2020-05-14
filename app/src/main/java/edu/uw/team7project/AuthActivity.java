package edu.uw.team7project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import edu.uw.team7project.util.SharedPref;


/**
 * An activity representing the authentication process for the application.
 *
 * @author Yousif Azami
 * @author Trevor Nichols
 */
public class AuthActivity extends AppCompatActivity {

    private Switch myswitch;

    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);

        if(sharedPref.loadNightModeState()==true) {
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        myswitch = (Switch)findViewById(R.id.mySwitch);
        if(sharedPref.loadNightModeState()==true) {
            myswitch.setChecked(true);
        }
        myswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPref.setNightModeState(true);
                    recreate();
                }
                else {
                    sharedPref.setNightModeState(false);
                    recreate();
                }
            }
        });


    }

//    public void restartApp() {
//        Intent i = new Intent(getApplicationContext(), AuthActivity.class);
//        startActivity(i);
//        finish();
//    }
}
