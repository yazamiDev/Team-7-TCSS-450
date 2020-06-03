package edu.uw.team7project.ui.contacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FindContactCardBinding;

public class FindContactRecyclerViewAdapter extends
        RecyclerView.Adapter<FindContactRecyclerViewAdapter.ContactViewHolder> {
    private final List<Contact> mContacts;
    private final String mJwt;
    private final FindContactViewModel mVM;


    /**
     * A constructor for teh contact recycler view.
     *
     * @param items a list of contacts.
     */
    public FindContactRecyclerViewAdapter (List<Contact> items, String jwt, FindContactViewModel vm) {
        this.mContacts = items;
        this.mJwt = jwt;
        this.mVM = vm;
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
                .inflate(R.layout.find_contact_card, parent, false));
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
        public FindContactCardBinding binding;
        public Contact mContact;

        /**
         * Constructore for teh contact view holder.
         *
         * @param view the view.
         */
        public ContactViewHolder(View view) {
            super(view);
            mView = view;
            binding = FindContactCardBinding.bind(view);
        }

        public void addUser(){
            binding.buttonAdd.setEnabled(false);
            binding.buttonAdd.setText("Added");
            mVM.addContact(mJwt, mContact.getContactMemberID());
        }

        private void handleError(VolleyError volleyError) {
        }

        /**
         * Sets the contact.
         *
         * @param contact the contact
         */
        void setContact(final Contact contact) {
            mContact = contact;
            String contactName = contact.getContactFirstName() + " " + contact.getContactLastName();
            binding.textContactName.setText(contactName);
            binding.buttonAdd.setOnClickListener(button -> addUser());
        }
    }
}
