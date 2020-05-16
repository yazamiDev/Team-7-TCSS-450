package edu.uw.team7project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.team7project.util.SharedPref;

/**
 * An activity representing the main process of the application.
 *
 * @author Yousif Azami
 * @author Bradlee Laird
 */
public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    // fields for theme
    private Switch mSwitch;
    SharedPref sharedPref;

    /**
     * Passes in the id's of the fragments that the main activity fragment will be travelling between,
     * as well as connects the activity to the navigation controller.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_messages, R.id.navigation_contacts, R.id.navigation_weather)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // start of theme implementation
        sharedPref = new SharedPref(this);

        if(sharedPref.loadNightModeState()==true) {
            setTheme(R.style.DarkTheme);
        } else setTheme(R.style.AppTheme);

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_auth);
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

}