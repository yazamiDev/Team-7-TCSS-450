package edu.uw.team7project.ui.messages;

import android.graphics.Color;
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

import java.util.ArrayList;

import edu.uw.team7project.MainActivity;
import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentAddContactToChatBinding;
import edu.uw.team7project.databinding.FragmentNewChatBinding;
import edu.uw.team7project.model.UserInfoViewModel;
import edu.uw.team7project.ui.contacts.ContactListViewModel;


public class AddContactToChatFragment extends Fragment {
    private ContactListViewModel mContactModel;
    private NewChatRecyclerViewAdapter mAdapter;
    private NewChatViewModel mChatModel;
    private UserInfoViewModel mUserInfoModel;
    private FragmentAddContactToChatBinding binding;
    private int mChatID;
    private String mTitle;

    public AddContactToChatFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        AddContactToChatFragmentArgs args = AddContactToChatFragmentArgs.fromBundle(getArguments());
        mChatID = args.getChatID();
        mTitle = args.getName();

        ((MainActivity) getActivity())
                .setActionBarTitle(" Add members to " + mTitle);

        mContactModel = provider.get(ContactListViewModel.class);
        mUserInfoModel = provider.get(UserInfoViewModel.class);
        mChatModel = provider.get(NewChatViewModel.class);
        mContactModel.connectGet(mUserInfoModel.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact_to_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        binding = FragmentAddContactToChatBinding.bind(getView());

        mContactModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            //if (!contactList.isEmpty()) {
            mAdapter = new NewChatRecyclerViewAdapter(contactList);
            binding.listRoot.setAdapter(mAdapter);
            //binding.layoutWait.setVisibility(View.GONE);
            //}
        });

        mChatModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);


        binding.buttonCancel.setOnClickListener(button -> Navigation.findNavController(getView())
                .navigate(AddContactToChatFragmentDirections
                        .actionAddContactToChatFragmentToChatFragment(mChatID, mTitle)));

        binding.buttonAdd.setText("Add");
        binding.buttonAdd.setOnClickListener(button -> {
            try {
                handleAddContacts();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * HAndles adding a contact to the chat room
     * @throws JSONException
     */
    private void handleAddContacts() throws JSONException {
        ArrayList<Integer> selectedContacts = mAdapter.getSelectedList();
        int[] temp = new int[selectedContacts.size()];

        for(int i = 0 ; i < selectedContacts.size(); i++){
            temp[i] = selectedContacts.get(i);
        }

        mChatModel.putMembers(mUserInfoModel.getJwt(), temp, mChatID);
        mAdapter.notifyDataSetChanged();
        Navigation.findNavController(getView())
                .navigate(AddContactToChatFragmentDirections
                        .actionAddContactToChatFragmentToChatFragment(mChatID, mTitle));
    }

    /**
     * Observes the response from the server.
     *
     * @param response the response.
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                Log.e("CHAT", "Failed to create chat room");
            } else {
                try {
                    if(response.has("chatID")){
                        System.out.println("Created a chat room");
                        System.out.println("moving on to populate chat room");
                        handleAddContacts();
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
