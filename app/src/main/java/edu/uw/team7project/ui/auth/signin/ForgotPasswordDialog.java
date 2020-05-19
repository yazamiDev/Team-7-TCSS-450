package edu.uw.team7project.ui.auth.signin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.team7project.R;

public class ForgotPasswordDialog extends DialogFragment {

    //A binding or this fragment.
    private EditText mEmail;

    //A forgot password view model.
    private ForgotPasswordViewModel mForgotPasswordModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mForgotPasswordModel = new ViewModelProvider(getActivity())
                .get(ForgotPasswordViewModel.class);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mForgotPasswordModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_pass_dialog, null);
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String email = mEmail.getText().toString();
                        System.out.println(email);
                        mForgotPasswordModel.connect(mEmail.getText().toString());

                        System.out.println("IN HEREEE:::");
//                        listener.applyTexts(email);
                    }
                });
        mEmail = view.findViewById(R.id.edit_email);
        return builder.create();
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
                    System.out.println("Failed to send email");
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        try {
//            listener = (ExampleDialogListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString() +
//                    "must implement ExampleDialogListener");
//        }
//    }
//    public interface ExampleDialogListener {
//        void applyTexts(String email);
//    }
}
