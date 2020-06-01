package edu.uw.team7project.ui.settings;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import edu.uw.team7project.R;

import static android.widget.Toast.LENGTH_LONG;

/**
 * Change password dialog to input email, current password, and new password
 *
 * @author Yousif Azami
 */
public class ChangePasswordDialog extends DialogFragment {

    //A binding for this fragment.
    private EditText mEmail;
    private EditText mCurrentPass;
    private EditText mNewPass;

    ConstraintLayout ChangePassDialog;

//    private FragmentSettingsBinding binding;


    //A forgot password view model.
    private ChangePasswordViewModel mChangePasswordViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChangePasswordViewModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);

//        ChangePassDialog = binding.settingsLayout;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mChangePasswordViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_pass_dailog, null);
        builder.setView(view)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String email = mEmail.getText().toString();
                        String currentPass = mCurrentPass.getText().toString();
                        String newPass = mNewPass.getText().toString();
                        System.out.println(email);
                        System.out.println(currentPass);
                        System.out.println(newPass);
                        mChangePasswordViewModel.connect(mEmail.getText().toString(),
                                mCurrentPass.getText().toString(),
                                mNewPass.getText().toString());


                        Toast.makeText(getActivity(), "Processing change process request" , LENGTH_LONG).show();
                        System.out.println("IN HERE");
//                        listener.applyTexts(email);
                    }
                });
        mEmail = view.findViewById(R.id.edit_email);
        mCurrentPass = view.findViewById(R.id.edit_current_pass);
        mNewPass = view.findViewById(R.id.edit_new_pass);
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
