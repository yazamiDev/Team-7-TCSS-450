package edu.uw.team7project.ui.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentContactCardBinding;

/**
 * A recycler view for the contact list.
 */
public class ContactRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactViewHolder> {

    private final List<Contact> mContacts;

    /**
     * A constructor for teh contact recycler view.
     *
     * @param items a list of contacts.
     */
    public ContactRecyclerViewAdapter (List<Contact> items) {
        this.mContacts = items;
    }

    /**
     * Cerates a view holder.
     *
     * @param parent the parent.
     * @param viewType the view type
     *
     * @return a contact view holder.
     */
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }


    /**
     * An inner class which hold the view for a contact.
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentContactCardBinding binding;

        /**
         * Constructore for teh contact view holder.
         *
         * @param view the view.
         */
        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentContactCardBinding.bind(view);

            //may need to change
            view.setOnClickListener(v -> {
                navigateToContact();
            });
        }

        /**
         * navigates to a contacts profile.
         */
        private void navigateToContact(){

        }

        /**
         * Sets the contact.
         *
         * @param contact the contact
         */
        void setContact(final Contact contact) {
            binding.textContactUsername.setText(contact.getContactUsername());
            String contactName = contact.getContactFirstName() + " " + contact.getContactLastName();
            binding.textContactName.setText(contactName);
        }

    }

}
