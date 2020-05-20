package edu.uw.team7project.ui.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.uw.team7project.MainActivity;
import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentHomeBinding;
import edu.uw.team7project.databinding.FragmentSettingsBinding;
import edu.uw.team7project.model.UserInfoViewModel;
import edu.uw.team7project.ui.auth.register.RegisterFragmentDirections;
import edu.uw.team7project.ui.home.HomeFragment;

/**
 * Subclass for the home fragment.
 *
 * @author Bradlee Laird
 */
public class SettingsFragment extends Fragment {

    private Object MainActivity;
    private MenuItem Menu;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Inflates the container for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    /**
     * Creates an instance of the fragment for when the user returns to it.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle("Settings");

        FragmentSettingsBinding binding = FragmentSettingsBinding.bind(getView());

        binding.changePassword.setOnClickListener(button -> {
//            Menu.getActionView();
//            activity.onOptionsItemSelected(Menu);
//            activity.setContentView(R.layout.fragment_change_password);

        });
    }



}