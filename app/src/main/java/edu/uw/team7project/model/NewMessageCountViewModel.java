package edu.uw.team7project.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * A Message Count View Model used to count messages
 */
public class NewMessageCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewMessageCount;

    /**
     * A Constructor for Message Count View Model.
     */
    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
    }

    /**
     * Adds  an obserever to the Message count.
     *
     * @param owner the owner
     * @param observer the observer.
     */
    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    /**
     * An incrementor for Message count.
     */
    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }

    /**
     * Resets the message count.
     */
    public void reset() {
        mNewMessageCount.setValue(0);
    }
}
