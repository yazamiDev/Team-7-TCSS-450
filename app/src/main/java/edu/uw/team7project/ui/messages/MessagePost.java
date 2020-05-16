package edu.uw.team7project.ui.messages;

import java.io.Serializable;

public class MessagePost implements Serializable {

    private final String mMessageName;

    public MessagePost(String messageName){
        mMessageName = messageName;
    }

    public String getMessageName() { return mMessageName; }

}
