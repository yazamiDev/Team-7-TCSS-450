package edu.uw.team7project.ui.messages;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageListViewModel extends AndroidViewModel {

    private MutableLiveData<List<MessagePost>> mMessageList;

    public MessageListViewModel(@NonNull Application application){
        super(application);
        mMessageList = new MutableLiveData<>(new ArrayList<>());
    }

    public void addMessageListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<MessagePost>> observer) {
        mMessageList.observe(owner, observer);
    }


    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
    /**
     * Parse a json object to get all the chatrooms?
     */
    private void handleResult(final JSONObject result) {
        try {
            JSONArray messages = result.getJSONArray("rows");
            for (int i = 0; i < result.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                String name = message.getString("name");
                int key = message.getInt("chatID");
                MessagePost post = new MessagePost(name, key);
                mMessageList.getValue().add(post);
            }
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle Success ChatViewModel");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        mMessageList.setValue(mMessageList.getValue());
    }

    public void connectGet ( int memberID, String jwt){
        //need a endpoint
        String url = "https://mobile-app-spring-2020.herokuapp.com/chats/" + memberID;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleResult,
                this::handleError) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                // add headers <key,value>
                headers.put("Authorization", jwt);
                return headers;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(
                10_000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //Instantiate the RequestQueue and add the request to the queue
        Volley.newRequestQueue(getApplication().getApplicationContext())
                .add(request);
    }
}


