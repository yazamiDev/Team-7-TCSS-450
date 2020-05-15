package edu.uw.team7project;

import androidx.appcompat.app.AppCompatActivity;

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
    private Switch mSwitch;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);

        if(sharedPref.loadNightModeState()==true) {
            setTheme(R.style.DarkTheme);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        mSwitch = (Switch)findViewById(R.id.mySwitch);

        if(sharedPref.loadNightModeState()==true) {
            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
