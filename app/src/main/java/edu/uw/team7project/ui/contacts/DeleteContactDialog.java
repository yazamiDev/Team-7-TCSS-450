package edu.uw.team7project.ui.contacts;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentContactListBinding;
import edu.uw.team7project.model.UserInfoViewModel;

/**
 * Dialog for deleting a existing contact.
 */
public class DeleteContactDialog extends DialogFragment {

    private UserInfoViewModel mUserModel;
    private ContactListViewModel mContactModel;
    private final int mMemberID;
    private final FragmentManager mFragMan;

    /**
     * DElete contact dialog contructor given a in and a Fragment manger
     * @param memberID the int representing member id
     * @param fm the fragment manager
     */
    public DeleteContactDialog(int memberID, FragmentManager fm) {
        this.mMemberID = memberID;
        this.mFragMan = fm;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mContactModel = new ViewModelProvider(getActivity())
                .get(ContactListViewModel.class);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(getView());

        mContactModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            //if (!contactList.isEmpty()) {
            binding.listRoot.setAdapter(
                    new ContactRecyclerViewAdapter(contactList, mFragMan)
            );
            binding.layoutWait.setVisibility(View.GONE);
            //}
        });
    }

    /**
     * Created the dialog window
     * @param savedInstanceState the saved instance state.
     * @return the Dialog.
     */
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_contact_dialog, null);
        TextView name = (TextView)view.findViewById(R.id.textContactName);
//        name.setText(mContactName);
        builder.setView(view)
                .setNegativeButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mContactModel.deleteContact(mUserModel.getJwt(), mMemberID);
                    }
                })
                .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        listener.applyTexts(email);
                    }
                });
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
                System.out.println("Failed to add contact");
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
