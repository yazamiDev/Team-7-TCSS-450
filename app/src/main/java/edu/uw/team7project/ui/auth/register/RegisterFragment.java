package edu.uw.team7project.ui.auth.register;
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

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentRegisterBinding;
import edu.uw.team7project.util.PasswordValidator;

/**
 * A fragment representing the registration process for the application.
 *
 * @author Trevor Nichols
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mPasswordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editPassword2.getText().toString()))
            .and(checkPwdMinLength(4))
            .and(checkPwdSpecialChar())
            .and(checkExcludeWhiteSpace())
            .and(checkPwdDigit())
            .and(checkPwdLowerCase()
            .and(checkPwdUpperCase()))
            .and(checkPwdMaxLength(32));

    private PasswordValidator mFNameValidator = checkPwdMinLength(1)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdMaxLength(32));

    private PasswordValidator mLNameValidator = checkPwdMinLength(1)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdMaxLength(32));

    private PasswordValidator mNicknameValidator = checkPwdMinLength(1)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdMaxLength(16));

    private PasswordValidator mEMailValidator = checkPwdMinLength(6)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"))
            .and(checkPwdSpecialChar("."))
            .and(checkPwdMaxLength(32));


    /**
     * An empty constructor.
     */
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
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
        binding.buttonRegister2.setOnClickListener(button -> handleRegister());

        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void handleRegister() {
        validateFirstName();
    }

    private void validateFirstName() {
        mFNameValidator.processResult(
                mFNameValidator.apply(binding.editFirst.getText().toString().trim()),
                this::validateLastName,
                this::handleFirstNameError);
    }

    private void validateLastName() {
        mLNameValidator.processResult(
                mLNameValidator.apply(binding.editLast.getText().toString().trim()),
                this::validateNickname,
                this::handleLastNameError);
    }

    private void validateNickname() {
        mNicknameValidator.processResult(
                mNicknameValidator.apply(binding.editNick.getText().toString().trim()),
                this::validateEmail,
                this::handleNicknameError);
    }

    private void validateEmail(){
        mEMailValidator.processResult(mEMailValidator.apply(binding.editEmail.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editEmail.setError("Please enter a valid email address."));
    }

    private void validatePassword() {
        mPasswordValidator.processResult(
                mPasswordValidator.apply(binding.editPassword1.getText().toString()),
                this::verifyAuthWithServer,
                this::handlePasswordError);
    }

    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editFirst.getText().toString(),
                binding.editLast.getText().toString(),
                binding.editEmail.getText().toString(),
                binding.editNick.getText().toString(),
                binding.editPassword1.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }


    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
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
                navigateToVerify();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    private void handleFirstNameError(ValidationResult result) {
        String msg = "Error";
        switch (result){
            case PWD_INVALID_LENGTH_MIN:
                msg = "First name must be at least 2 characters";
                break;
            case PWD_INVALID_LENGTH_MAX:
                msg = "First name cannot be more than 32 characters";
                break;
            case PWD_INCLUDES_WHITESPACE:
                msg = "First name cannot contain any spaces";
                break;
            default:
                throw new IllegalStateException("Missing CASE in switch");
        }
        binding.editFirst.setError(msg);
    }

    private void navigateToVerify(){
        Navigation.findNavController(getView())
        .navigate(RegisterFragmentDirections
                .actionRegisterFragmentToVerifyFragment(binding.editFirst.getText().toString(),
                        binding.editLast.getText().toString(),
                        binding.editEmail.getText().toString(),
                        binding.editNick.getText().toString(),
                        binding.editPassword1.getText().toString()));
    }

    private void handleLastNameError(ValidationResult result) {
        String msg = "Error";
        switch (result){
            case PWD_INVALID_LENGTH_MIN:
                msg = "Last name must be at least 2 characters";
                break;
            case PWD_INVALID_LENGTH_MAX:
                msg = "Last name cannot be more than 32 characters";
                break;
            case PWD_INCLUDES_WHITESPACE:
                msg = "Last name cannot contain any spaces";
                break;
            default:
                throw new IllegalStateException("Missing CASE in switch");
        }
        binding.editLast.setError(msg);
    }

    private void handleNicknameError(ValidationResult result){
        String msg = "Error";
        switch (result){
            case PWD_INVALID_LENGTH_MIN:
                msg = "Nickname must be at least 2 characters";
                break;
            case PWD_INVALID_LENGTH_MAX:
                msg = "Nickname cannot be more than 16 characters";
                break;
            case PWD_INCLUDES_WHITESPACE:
                msg = "Nickname cannot contain any spaces";
                break;
            default:
                throw new IllegalStateException("Missing CASE in switch");
        }
        binding.editNick.setError(msg);
    }

    private void handlePasswordError(ValidationResult result){
        String msg = "Error";
        switch (result){
            case PWD_CLIENT_ERROR:
                msg = "Passwords must match.";
                break;
            case PWD_MISSING_DIGIT:
                msg = "Password must include a digit.";
                break;
            case PWD_INVALID_LENGTH_MIN:
                msg = "Password must be at least 5 characters";
                break;
            case PWD_MISSING_SPECIAL:
                msg = "Password must include a special character";
                break;
            case PWD_MISSING_LOWER:
                msg = "Password must include a lowercase letter";
                break;
            case PWD_MISSING_UPPER:
                msg = "Password must include a  uppercase letter";
                break;
            case PWD_INCLUDES_WHITESPACE:
                msg = "Password cannot include whitespace";
                break;
            case PWD_INVALID_LENGTH_MAX:
                msg = "Password much be less than or equal to 32 characters";
                break;
            default:
                throw new IllegalStateException("Missing CASE in switch");
        }
        binding.editPassword1.setError(msg);
    }
}
