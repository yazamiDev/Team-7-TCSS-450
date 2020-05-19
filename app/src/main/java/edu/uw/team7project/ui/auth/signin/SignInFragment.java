package edu.uw.team7project.ui.auth.signin;
import static edu.uw.team7project.util.PasswordValidator.*;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentSignInBinding;
import edu.uw.team7project.model.PushyTokenViewModel;
import edu.uw.team7project.model.UserInfoSignInViewModel;
import edu.uw.team7project.model.UserInfoViewModel;
import edu.uw.team7project.util.PasswordValidator;


/**
 * A fragment representing the login process.
 *
 * @author Yousif Azami
 * @author Trevor Nichols
 */
public class SignInFragment extends Fragment {

    //Binding for this fragment.
    private FragmentSignInBinding binding;

    //A sign in view model.
    private SignInViewModel mSignInModel;

    // forgot password button
    private Button mForgotPasswordButton;

    private TextView mInputEmail;

    private PushyTokenViewModel mPushyTokenViewModel;

    private UserInfoSignInViewModel mUserViewModel;

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

        mPushyTokenViewModel = new ViewModelProvider(getActivity())
        .get(PushyTokenViewModel.class);

    }

    /**
     * Opens a dialog to enter a valid email.
     */
    private void openDialog() {
        ForgotPasswordDialog forgotPasswordDialog = new ForgotPasswordDialog();
        forgotPasswordDialog.show(getActivity().getSupportFragmentManager(), "example dialog");

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

        //don't allow sign in until pushy token retrieved
        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                binding.buttonSignIn.setEnabled(!token.isEmpty()));


        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);

        SignInFragmentArgs args = SignInFragmentArgs.fromBundle(getArguments());
        binding.editEmail.setText(args.getEmail().equals("default") ? "" : args.getEmail());
        binding.editPassword.setText(args.getPassword().equals("default") ? "" : args.getPassword());

        binding.forgotPassword.setOnClickListener(mForgotPasswordButton -> openDialog());
    }

//    @Override
//    public void applyTexts(String email) {
//        mInputEmail.setText(email);
//    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getJwt());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
        //this error cannot be fixed by the user changing credentials...
                binding.editEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                navigateToHome(
                        binding.editEmail.getText().toString(),
                        mUserViewModel.getJwt()
                );
            } } }

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
     * naviagtes to home on successful login and save the JWT to Shared Preferences on
     * successful sign in.
     *
     * @param email the users email.
     * @param jwt the users jwt.
     */
    private void navigateToHome(String email, String jwt){
        if (binding.switchSignin.isChecked()) {
            SharedPreferences prefs =
                    getActivity().getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            //Store the credentials in SharedPrefs
            prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        }

        //Update this to pass proper arguments to main activity.
        Navigation.findNavController(getView())
                .navigate(SignInFragmentDirections.actionSignInFragmentToMainActivity(email, jwt));

        //Remove THIS activity from the Task list. Pops off the backstack
        getActivity().finish();
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
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoSignInViewModel.UserInfoSignInViewModelFactory(
                                    binding.editEmail.getText().toString(),
                                    response.getString("token")
                            )).get(UserInfoSignInViewModel.class);
                    sendPushyToken();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }

    }

    /**
     * When starting application, get JWT and see if it is valid. If valid then
     * navigate to home.
     */
    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if (!jwt.isExpired(0)) {
//                String email = jwt.getClaim("email").asString();
//                navigateToHome(email, token);
//                return;
            }
        }
    }
}
