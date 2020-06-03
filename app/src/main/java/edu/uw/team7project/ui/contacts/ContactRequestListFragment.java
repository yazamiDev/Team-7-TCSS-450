package edu.uw.team7project.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentContactRequestListBinding;
import edu.uw.team7project.model.UserInfoViewModel;


/**
 * A Fragment represeing the contact request view.
 *
 * @author Trevor Nichols
 */
public class ContactRequestListFragment extends Fragment {

    private ContactRequestListViewModel mModel;

    public ContactRequestListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(getActivity()).get(ContactRequestListViewModel.class);

        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        Log.i("CONTACT", model.getJwt());
        mModel.connectGet(model.getJwt());
    }

    /**
     * Inflates the container for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_request_list, container, false);
    }

    /**
     * Creates an instance of the fragment for when the user returns to it.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentContactRequestListBinding binding = FragmentContactRequestListBinding.bind(getView());

        mModel.addContactRequestListObserver(getViewLifecycleOwner(), contactList -> {
            //if (!contactList.isEmpty()) {
            binding.listRoot.setAdapter(
                    new ContactRequestRecyclerViewAdapter(contactList, getActivity().getSupportFragmentManager())
            );
            //binding.layoutWait.setVisibility(View.GONE);
            //}
        });
    }
}
