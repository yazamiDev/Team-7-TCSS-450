package edu.uw.team7project.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentRegisterBinding;

/**
 * A fragment representing the registration process for the application.
 *
 * @author Trevor Nichols
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    /**
     * An empty constructor.
     */
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        binding.buttonRegister2.setOnClickListener(button -> Navigation.findNavController(getView())
       .navigate(RegisterFragmentDirections
               .actionRegisterFragmentToVerifyFragment(binding.editFirst.getText().toString(),
                       binding.editLast.getText().toString(),
                       binding.editEmail.getText().toString(),
                       binding.editNick.getText().toString(),
                       binding.editPassword1.getText().toString())));
    }
}
