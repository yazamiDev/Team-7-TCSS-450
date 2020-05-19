package edu.uw.team7project.ui.messages;

import java.io.Serializable;

/**
 * Represents a single message post.
 *
 * @author Trevor Nichols
 */
public class MessagePost implements Serializable {

    private final String mMessageName;

    private final int mChatID;

    /**
     * A cpnstrutore for a message post
     *
     * @param messageName the message post name
     * @param chatID the message post id
     */
    public MessagePost(String messageName, int chatID){

        mMessageName = messageName;
        mChatID = chatID;
    }

    /**
     * get the message post name.
     *
     * @return the message post name.
     */
    public String getMessageName() { return mMessageName; }

    /**
     * Get the messgae post id.
     *
     * @return the message post id.
     */
    public int getChatID() { return mChatID; }

}
