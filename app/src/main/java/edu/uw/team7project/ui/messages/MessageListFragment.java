package edu.uw.team7project.ui.messages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentMessageListBinding;
import edu.uw.team7project.model.UserInfoViewModel;

/**
 * Subclass for the messages fragment.
 *
 * @author Bradlee laird
 */
public class MessageListFragment extends Fragment {

    private MessageListViewModel mModel;

    public MessageListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(getActivity()).get(MessageListViewModel.class);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        //connect here but what arguments to pass?
        mModel.connectGet(model.getJwt());
    }

    /**
     * Inflates the container for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    /**
     * Creates an instance of the fragment for when the user returns to it.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentMessageListBinding binding = FragmentMessageListBinding.bind(getView());

        //may need to change this to be similar to chatfragment on view created.
        //add observer for getting messages
        mModel.addMessageListObserver(getViewLifecycleOwner(), messageList -> {
            if (!messageList.isEmpty()) {
                binding.listRoot.setAdapter(
                        new MessagesRecyclerViewAdapter(messageList)
                );
                //binding.layoutWait.setVisibility(View.GONE);
            }
        });
    }
}
