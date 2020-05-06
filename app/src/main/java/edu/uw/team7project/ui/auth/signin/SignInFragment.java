package edu.uw.team7project.ui.auth.signin;
import static edu.uw.team7project.util.PasswordValidator.*;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.team7project.databinding.FragmentSignInBinding;
import edu.uw.team7project.util.PasswordValidator;


/**
 * A fragment representing the login process.
 *
 * @author Trevor Nichols
 */
public class SignInFragment extends Fragment {

    //Binding for this fragment.
    private FragmentSignInBinding binding;

    //A sign in view model.
    private SignInViewModel mSignInModel;

    //An email validator
    private PasswordValidator mEMailValidator = checkPwdMinLength(6)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"))
            .and(checkPwdSpecialChar("."))
            .and(checkPwdMaxLength(32));

    //A password validator.
    private PasswordValidator mPasswordValidator =checkPwdMinLength(0)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdMaxLength(32));

    /**
     * An empty constructor.
     */
    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSignInModel = new ViewModelProvider(getActivity()).get(SignInViewModel.class);
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

        binding.buttonSignIn.setOnClickListener(button -> handleSignIn());

        mSignInModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());
    }

    /**
     * Handle the sign in process for login.
     */
    private void handleSignIn() {
        mEMailValidator.processResult(mEMailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editEmail.setError("Please enter a valid email address."));
    }

    /**
     * Validates the login password.
     */
    private void validatePassword() {
        mPasswordValidator.processResult(
                mPasswordValidator.apply(binding.editPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editPassword.setError("Please enter a valid Password."));
    }

    /**
     * Verify the authentication with server.
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.editEmail.getText().toString(),
                binding.editPassword.getText().toString());
    }

    /**
     * naviagtes to home on successful login.
     *
     * @param email the users email.
     * @param jwt the users jwt.
     */
    private void navigateToHome(String email, String jwt){
        //Update this to pass proper arguments to main activity.
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections.actionSignInFragmentToMainActivity(email, jwt));
    }

    /**
     * Observes the response from the server.
     *
     * @param response the response.
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    //update with proper information for navigating to home frag.
                    navigateToHome(
                            binding.editEmail.getText().toString(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }
}
