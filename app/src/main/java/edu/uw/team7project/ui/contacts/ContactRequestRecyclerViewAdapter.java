package edu.uw.team7project.ui.contacts;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team7project.MainActivity;
import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentContactRequestCardBinding;

public class ContactRequestRecyclerViewAdapter extends
    RecyclerView.Adapter<ContactRequestRecyclerViewAdapter.ContactRequestViewHolder>{

        private final List<Contact> mContactRequests;
        private final FragmentManager mFragmMan;

        /**
         * A constructor for teh contact recycler view.
         *
         * @param items a list of contacts.
         */
    public ContactRequestRecyclerViewAdapter(List < Contact > items, FragmentManager fm) {
        this.mContactRequests = items;
        this.mFragmMan = fm;
    }

        /**
         * Cerates a view holder.
         *
         * @param parent the parent.
         * @param viewType the view type
         *
         * @return a contact view holder.
         */
        public ContactRequestViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType){
        return new ContactRequestViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_request_card, parent, false));
    }

        @Override
        public void onBindViewHolder (@NonNull ContactRequestViewHolder holder,int position){
        holder.setContact(mContactRequests.get(position));
    }

        @Override
        public int getItemCount () {
        return mContactRequests.size();
    }


        /**
         * An inner class which hold the view for a contact.
         */
        public class ContactRequestViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public FragmentContactRequestCardBinding binding;
            public Contact mContact;

            /**
             * Constructore for teh contact view holder.
             *
             * @param view the view.
             */
            public ContactRequestViewHolder(View view) {
                super(view);
                mView = view;
                binding = FragmentContactRequestCardBinding.bind(view);
            }

            /**
             * navigates to a contacts profile.
             */
            private void openDialog() {
                String name = mContact.getContactFirstName() + " " + mContact.getContactLastName();
                AcceptContactDialog dialog = new AcceptContactDialog(name,
                        mContact.getContactMemberID());
                dialog.show(mFragmMan, "maybe?");
            }

            /**
             * Sets the contact.
             *
             * @param contact the contact
             */
            void setContact(final Contact contact) {
                binding.textUsername.setText(contact.getContactUsername());
                mContact = contact;
                binding.buttonAccept.setOnClickListener( button -> openDialog());
            }

        }
}

