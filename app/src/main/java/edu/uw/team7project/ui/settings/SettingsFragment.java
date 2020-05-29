package edu.uw.team7project.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.auth0.android.jwt.JWT;
import com.google.android.material.snackbar.Snackbar;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentSettingsBinding;
import edu.uw.team7project.databinding.FragmentSignInBinding;
import edu.uw.team7project.model.UserInfoViewModel;
import edu.uw.team7project.ui.auth.verify.VerifyFragmentArgs;
import edu.uw.team7project.databinding.FragmentVerifyBinding;
import edu.uw.team7project.util.SharedPref;

/**
 * Settings fragment to display users information and ability
 * to change password.
 *
 * @author Yousif Azami
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    private UserInfoViewModel mUserModel;

    // fields for theme
    private Switch mSwitch;
    SharedPref sharedPref;

    /**
     * required empty constructor
     */
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
        binding = FragmentSettingsBinding.inflate(inflater);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    /**
     * Creates an instance of the fragment for when the user returns to it.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();

//        VerifyFragmentArgs args = VerifyFragmentArgs.fromBundle(getArguments());
//        SettingsFragmentArgs args = SettingsFragmentArgs.fromBundle(getArguments());

        FragmentSettingsBinding binding = FragmentSettingsBinding.bind(getView());


        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        JWT jwt = new JWT( mUserModel.getJwt());
        System.out.println("Getting JWT::");

        System.out.println("Email::" + jwt.getClaim("email").asString());
        System.out.println("First::" + jwt.getClaim("firstname").asString());
        System.out.println("Last::" + jwt.getClaim("lastname").asString());
        System.out.println("Username::" + jwt.getClaim("username").asString());

        binding.textName.setText(jwt.getClaim("firstname").asString() + " " +
                jwt.getClaim("lastname").asString());
        binding.editEmail.setText(jwt.getClaim("email").asString());
        binding.textUsername.setText(jwt.getClaim("username").asString());

        binding.changePassword.setOnClickListener(mForgotPasswordButton -> openDialog());

        mSwitch = (Switch) view.findViewById(R.id.mySwitch);

        System.out.println("Settings onCreate");
        // start of theme implementation
        sharedPref = new SharedPref(getActivity());

        if(sharedPref.loadNightModeState()==true) {
            getLayoutInflater().getContext().setTheme(R.style.DarkTheme);
//            setTheme(R.style.DarkTheme);
        } else getLayoutInflater().getContext().setTheme(R.style.AppTheme);


        if(sharedPref.loadNightModeState()==true) {
            mSwitch.setChecked(true);
//            mSwitch.setChecked(true);
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Changed the switch" );
                if (isChecked) {
                    sharedPref.setNightModeState(true);
                    getActivity().recreate();
//                    recreate();
                }
                else {
                    sharedPref.setNightModeState(false);
                    getActivity().recreate();
//                    recreate();
                }
            }
        });

    }

    /**
     * Opens a dialog to enter a valid email,
     * current password, and new password.
     */
    private void openDialog() {
        ChangePasswordDialog forgotPasswordDialog = new ChangePasswordDialog();
        forgotPasswordDialog.show(getActivity().getSupportFragmentManager(), "change password dialog");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        System.out.println("Settings onCreate");
//        // start of theme implementation
//        sharedPref = new SharedPref(getActivity());
//
//        if(sharedPref.loadNightModeState()==true) {
//            getLayoutInflater().getContext().setTheme(R.style.DarkTheme);
////            setTheme(R.style.DarkTheme);
//        } else getLayoutInflater().getContext().setTheme(R.style.AppTheme);
//
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_auth);
////        mSwitch = binding.mySwitch;
//
//
//        if(sharedPref.loadNightModeState()==true) {
//            binding.mySwitch.setChecked(true);
////            mSwitch.setChecked(true);
//        }
//
//        binding.mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                System.out.println("Changed the switch" );
//                if (isChecked) {
//                    sharedPref.setNightModeState(true);
////                    recreate();
//                }
//                else {
//                    sharedPref.setNightModeState(false);
////                    recreate();
//                }
//            }
//        });

    }
}