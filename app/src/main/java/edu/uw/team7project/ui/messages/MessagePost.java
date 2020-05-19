package edu.uw.team7project.ui.messages;

import java.io.Serializable;

public class MessagePost implements Serializable {

    private final String mMessageName;

    private final int mChatID;

    public MessagePost(String messageName, int chatID){

        mMessageName = messageName;
        mChatID = chatID;
    }

    public String getMessageName() { return mMessageName; }

    public int getChatID() { return mChatID; }

}
