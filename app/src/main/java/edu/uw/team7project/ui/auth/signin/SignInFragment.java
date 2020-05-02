package edu.uw.team7project.ui.auth.signin;
import static edu.uw.team7project.util.PasswordValidator.*;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team7project.databinding.FragmentSignInBinding;
import edu.uw.team7project.util.PasswordValidator;


/**
 * A fragment representing the login process.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel mLoginModel;
    private PasswordValidator mEMailValidator = checkPwdLength(2);

    private PasswordValidator pWordValidator = checkPwdLength(1);

    /**
     * An empty constructor.
     */
    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignInBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister1.setOnClickListener(button -> Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections.actionSignInFragmentToRegisterFragment()));

    }
}
