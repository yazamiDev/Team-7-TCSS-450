package edu.uw.team7project.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Shared Preference class that saves the state of the theme.
 *
 * @author yousif
 */
public class SharedPref {

    SharedPreferences mySharedPref;

    public SharedPref(Context context) {
        mySharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    // Method to save Night Mode state.
    public void setNightModeState(Boolean state) {
        SharedPreferences.Editor editor = mySharedPref.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    //method to load the Night Mode state.
    public Boolean loadNightModeState() {
        boolean state = mySharedPref.getBoolean("NightMode", false);
        return state;
    }


}
