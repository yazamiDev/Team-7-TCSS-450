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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageListViewModel extends AndroidViewModel {

    private MutableLiveData<List<>> mMessageList;

    public MessageListViewModel(@NonNull Application application){
        super(application);
        mMessageList = new MutableLiveData<>();
        mMessageList.setValue(new ArrayList<>());
    }

    public void addMessageListObserver(@NonNull LifecycleOwner owner,
                                       @NonNull Observer<? super List<>> observer) {
        mMessageList.observe(owner, observer);
    }

    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
    /**
     * Parse a json object to get all the chatrooms?
     */
//    private void handleResult(final JSONObject result) {
//        IntFunction<String> getString =
//                getApplication().getResources()::getString;
//        try {
//            JSONObject root = result;
//            if (root.has(getString.apply(R.string.keys_json_blogs_response))) {
//                JSONObject response =
//                        root.getJSONObject(getString.apply(
//                                R.string.keys_json_blogs_response));
//                if (response.has(getString.apply(R.string.keys_json_blogs_data))) {
//                    JSONArray data = response.getJSONArray(
//                            getString.apply(R.string.keys_json_blogs_data));
//                    for(int i = 0; i < data.length(); i++) {
//                        JSONObject jsonBlog = data.getJSONObject(i);
//                        mBlogList.getValue().add(new BlogPost.Builder(
//                                jsonBlog.getString(
//                                        getString.apply(
//                                                R.string.keys_json_blogs_pubdate)),
//                                jsonBlog.getString(
//                                        getString.apply(
//                                                R.string.keys_json_blogs_title)))
//                                .addTeaser(jsonBlog.getString(
//                                        getString.apply(
//                                                R.string.keys_json_blogs_teaser)))
//                                .addUrl(jsonBlog.getString(
//                                        getString.apply(
//                                                R.string.keys_json_blogs_url)))
//                                .build());
//                    }
//                } else {
//                    Log.e("ERROR!", "No data array");
//                }
//            } else {
//                Log.e("ERROR!", "No response");
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("ERROR!", e.getMessage());
//        }
//        mBlogList.setValue(mBlogList.getValue());
//    }

    public void connectGet() {
        //need a endpoint
        String url =
                "";
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
