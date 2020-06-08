package edu.uw.team7project.ui.messages;

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
import edu.uw.team7project.databinding.FragmentMessageListBinding;
import edu.uw.team7project.model.UserInfoViewModel;

public class DeleteChatDialog extends DialogFragment {
    private UserInfoViewModel mUserModel;
    private MessageListViewModel mMessageModel;
    private final MessagesRecyclerViewAdapter.MessageViewHolder mUpdater;
    private final FragmentManager mFragMan;
    private final int mChatID;


    /**
     * Constructro for the accept dialog
     *
     * @param name A String representing a contacts name
     * @param memberID an integer representing the contact ID
     */
    public DeleteChatDialog(int chatID, FragmentManager fm,  MessagesRecyclerViewAdapter.MessageViewHolder updater){
        mChatID = chatID;
        mUpdater = updater;
        mFragMan = fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserModel = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mMessageModel = new ViewModelProvider(getActivity()).get(MessageListViewModel.class);
    }

    /**
     * The view created for  Accept dialog.
     *
     * @param view the view
     * @param savedInstanceState the saved instance state.
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());

        mMessageModel.addMessageListObserver(getViewLifecycleOwner(), messageList -> {
            //if (!contactList.isEmpty()) {
            binding.listRoot.setAdapter(
                    new MessagesRecyclerViewAdapter(messageList, mFragMan)
            );
            binding.layoutWait.setVisibility(View.GONE);
            //}
        });
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_chat_dialog, null);
        builder.setView(view)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mMessageModel.connectDelete(mUserModel.getJwt(), mChatID,
                                mUserModel.getEmail());
                        mUpdater.deleteRequest();
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
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
