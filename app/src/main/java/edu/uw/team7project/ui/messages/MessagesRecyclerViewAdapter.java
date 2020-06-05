package edu.uw.team7project.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentMessageCardBinding;

/**
 * A message post recycler view class
 *
 * @author Trevor Nichols.
 */
public class MessagesRecyclerViewAdapter extends
        RecyclerView.Adapter<MessagesRecyclerViewAdapter.MessageViewHolder> {

    private final List<MessagePost> mMessages;
    private final FragmentManager mFragMan;

    /**
     * A constructor for the message recycler view.
     *
     * @param items a list of message posts.
     */
    public MessagesRecyclerViewAdapter(List<MessagePost> items, FragmentManager fm) {
        this.mMessages = items;
        mFragMan = fm;
    }

    /**
     * Creates a message view holder.
     *
     * @param parent the parent
     * @param viewType the view type.
     *
     * @return a message view holder
     */
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_message_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(mMessages.get(position));
    }

    /**
     * Get the cont of messages.
     *
     * @return the number of messages.
     */
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * A message view holder class
     */
    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentMessageCardBinding binding;
        public MessagePost mPost;

        /**
         * contructs a message view holder.
         *
         * @param view the view.
         */
        public MessageViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentMessageCardBinding.bind(view);

            //may need to change
            view.setOnClickListener(v -> {
                navigateToChat();
            });
        }

        /**
         * navigates to a chat room associated with the message post.
         */
        private void navigateToChat(){
            MessageListFragmentDirections.ActionNavigationMessagesToChatFragment directions =
                    MessageListFragmentDirections.
                            actionNavigationMessagesToChatFragment(mPost.getChatID(),
                                    mPost.getMessageName());

            Navigation.findNavController(mView).navigate(directions);
        }

        /**
         * Sets the message for the view holder.
         *
         * @param message the message.
         */
        void setMessage(final MessagePost message) {
            binding.textMessageName.setText(message.getMessageName());
            mPost= message;
            binding.buttonDelete.setOnClickListener(button -> handleDelete());
        }

        public void handleDelete(){
            DeleteChatDialog dialog = new DeleteChatDialog(mPost.getChatID(), mFragMan, this);
            dialog.show(mFragMan, "maybe?");
        }

        public void deleteRequest(){
            mMessages.remove(mPost);
            notifyDataSetChanged();
        }
    }
}
