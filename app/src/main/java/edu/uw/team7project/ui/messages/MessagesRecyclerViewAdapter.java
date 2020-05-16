package edu.uw.team7project.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentMessageCardBinding;

public class MessagesRecyclerViewAdapter extends
        RecyclerView.Adapter<MessagesRecyclerViewAdapter.MessageViewHolder> {

    private final Map<Integer, MessagePost> mMessages;

    public MessagesRecyclerViewAdapter(Map<Integer, MessagePost> items) {
        this.mMessages = items;
    }

    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_message_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(mMessages.get(position));
        holder.setKey(position);
    }

    public int getItemCount() {
        return mMessages.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentMessageCardBinding binding;
        public int mKey;

        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentMessageCardBinding.bind(view);

            //may need to change
            view.setOnClickListener(v -> {
                navigateToChat();
            });
        }

        private void navigateToChat(){
            MessageListFragmentDirections.ActionNavigationMessagesToChatFragment directions =
                    MessageListFragmentDirections.actionNavigationMessagesToChatFragment(mKey);

            Navigation.findNavController(mView).navigate(directions);
        }

        void setMessage(final MessagePost message) {
            binding.textMessageName.setText(message.getMessageName());
        }

        void setKey(int key){
            mKey = key;
        }

    }
}
