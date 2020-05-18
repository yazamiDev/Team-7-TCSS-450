package edu.uw.team7project.ui.contacts;

public class Contact {
    private final String mEmail;
    private final String mFirstName;
    private final String mLastName;
    private final String mUserName;
    private final int mMemberID;

    public Contact(String email, String fName, String lName, String uName, int id) {
        this.mEmail = email;
        this.mFirstName = fName;
        this.mLastName = lName;
        this.mUserName = uName;
        this.mMemberID = id;
    }

    public String getContactEmail(){ return mEmail; }

    public String getContactFirstName(){ return mFirstName; }

    public String getContactLastName(){ return mLastName; }

    public String getContactUsername(){ return mUserName; }

    public int getContactMemberID(){ return mMemberID; }

}
