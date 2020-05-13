package edu.uw.team7project.ui.messages;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team7project.databinding.FragmentMessageCardBinding;

public class MessagesRecyclerViewAdapter extends
        RecyclerView.Adapter<MessagesRecyclerViewAdapter.MessageViewHolder> {

    private final List<MessagePost> mMessages;

    public MessagesRecyclerViewAdapter(List<MessagePost> items) {
        this.mMessages = items;
    }

    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {

    }

    public int getItemCount() {
        return mMessages.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentMessageCardBinding binding;

        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentMessageCardBinding.bind(view);
            binding.cardRoot.setOnClickListener(this::handleClick);
        }

        private void handleClick(View button) {

        }

        void setMessage(final MessagePost message) {

        }

    }
}
