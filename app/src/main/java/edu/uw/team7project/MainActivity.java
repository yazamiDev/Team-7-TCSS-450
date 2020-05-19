package edu.uw.team7project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.team7project.ui.auth.verify.VerifyFragmentArgs;
import edu.uw.team7project.ui.settings.SettingsFragment;

/**
 * An activity representing the main process of the application.
 *
 * @author Bradlee Laird
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_settings:
//                displayToast(getString(R.string.action_settings_message));
//                return true;
                Log.d("Settings", "Clicked");
//                Intent intent = new Intent(this, SettingsActivity.class);
//                startActivity(intent);
                setContentView(R.layout.fragment_settings);
                return true;

            case R.id.action_signOut:
                displayToast(getString(R.string.action_signOut_message));
                return true;


        }

        return super.onOptionsItemSelected(item);
    }



//    protected void openSettings() {
//        Intent intent = new Intent(this, SettingsActivity.class);
//        startActivity(intent);
//    }

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
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}