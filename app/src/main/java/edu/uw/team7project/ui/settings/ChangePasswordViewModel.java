package edu.uw.team7project.ui.settings;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Objects;

/**
 * A forgot password view model for resetting user password.
 *
 * @author Yousif Azami
 */
public class ChangePasswordViewModel extends AndroidViewModel {

    //Mutable live data for registration.
    private MutableLiveData<JSONObject> mResponse;

    /**
     * Constructor for the ForgotPasswordView Model.
     *
     * @param application an application.
     */
    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
    }

    /**
     * Adds a response observer.
     *
     * @param owner the owner
     * @param observer the observer
     */
    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
    }

    /**
     * Handles errors for connecting to a WebService endpoint.
     * @param error
     */
    private void handleError(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponse.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset())
                    .replace('\"', '\'');
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:\"" + data +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    /**
     * Connects to end point to send forgot password link to email.
     *
     * @param email registered email
     * @param oldPass current password
     * @param newPass new password
     */
    public void connect(final String email,
                        final String oldPass,
                        final String newPass) {
        String url = "https://mobile-app-spring-2020.herokuapp.com/changepassword";
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("oldpassword", oldPass);
            body.put("newpassword", newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Request request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                mResponse::setValue,
                this::handleError);
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }
}
