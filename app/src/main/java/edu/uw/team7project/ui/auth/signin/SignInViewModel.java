package edu.uw.team7project.ui.auth.signin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

/**
 * A ViewModel used to store information related to the sign in Process.
 */
public class SignInViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponse;

    /**
     * A constructor for creating a Sign in ViewModel.
     *
     * @param application An application.
     */
    public SignInViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }
}
