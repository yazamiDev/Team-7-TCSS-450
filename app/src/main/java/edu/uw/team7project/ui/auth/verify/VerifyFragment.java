package edu.uw.team7project.ui.auth.verify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentVerifyBinding;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyFragment extends Fragment {

    public VerifyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verify, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        VerifyFragmentArgs args = VerifyFragmentArgs.fromBundle(getArguments());

        FragmentVerifyBinding binding = FragmentVerifyBinding.bind(getView());
        binding.editPersonalGreeting.setText("Hello " + args.getFirstName() + "! Use the " +
                "link below to verify your email and start enjoying app name");

        binding.buttonVerify.setOnClickListener(button -> Navigation.findNavController(getView())
                .navigate(VerifyFragmentDirections.actionVerifyFragmentToSignInFragment()));
    }
}
