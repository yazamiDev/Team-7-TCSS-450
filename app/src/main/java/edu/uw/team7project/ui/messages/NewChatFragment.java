package edu.uw.team7project.ui.messages;

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
import java.util.List;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentNewChatBinding;
import edu.uw.team7project.model.UserInfoViewModel;
import edu.uw.team7project.ui.contacts.ContactListViewModel;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author Trevor Nichols
 */
public class NewChatFragment extends Fragment {

    private ContactListViewModel mContactModel;
    private NewChatRecyclerViewAdapter mAdapter;
    private NewChatViewModel mChatModel;
    private UserInfoViewModel mUserInfoModel;
    private FragmentNewChatBinding binding;
    private int mNewChatID;

    public NewChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContactModel = new ViewModelProvider(getActivity()).get(ContactListViewModel.class);
        mChatModel = new ViewModelProvider(getActivity()).get(NewChatViewModel.class);
        mUserInfoModel = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);

        mContactModel.connectGet(mUserInfoModel.getJwt());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        binding = FragmentNewChatBinding.bind(getView());

        mContactModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            //if (!contactList.isEmpty()) {
            mAdapter = new NewChatRecyclerViewAdapter(contactList);
            binding.listRoot.setAdapter(mAdapter);
            //binding.layoutWait.setVisibility(View.GONE);
            //}
        });

        mChatModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        binding.buttonCancel.setOnClickListener(button -> Navigation.findNavController(getView()).
                navigate(NewChatFragmentDirections.actionNewChatFragmentToNavigationMessages()));

        binding.buttonCreate.setOnClickListener(button -> handleCreateChatRoom());
    }

    /**
     * HAndles creating a chat room
     */
    private void handleCreateChatRoom(){
        String name = binding.editChatName.getText().toString().trim();
        if(name.length() < 1){
            binding.editChatName.setError("Please enter a valid chat room name");
        }else{
            mChatModel.createChat(mUserInfoModel.getJwt(), name);
        }
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
            System.out.println(temp[i]);
        }
        //selectedContacts.add(mUserInfoModel.getMemberID());
        mChatModel.putMembers(mUserInfoModel.getJwt(), temp, mNewChatID);
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
                    mNewChatID = response.getInt("chatID");
                    System.out.println("moving on to populate chat room");
                    handleAddContacts();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
