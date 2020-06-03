package edu.uw.team7project.ui.home;

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

/**
 * A view model for Home.
 *
 * @author Yousif Azami
 */
public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<HomeFragment>> mHome;

    /**
     * A constructor for the home view model.
     *
     * @param application the application
     */
    public HomeViewModel(@NonNull Application application) {
        super(application);
        mHome = new MutableLiveData<>();
    }

    public void addHomeObserver(@NonNull LifecycleOwner owner,
                                   @NonNull Observer<? super List<HomeFragment>> observer) {
        mHome.observe(owner, observer);
    }


    /**
     * Connects  to the webservice to get current conditions.
     *
     * @param city the city
     * @param jwt a valid jwt
     */
    public void connectGetCurrent( String city, String jwt){
        String url = "https://mobile-app-spring-2020.herokuapp.com/weather?name=" + city;
        Request request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, //no body for this get request
                this::handleCurrentResult,
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


    /**
     * Handles results for the current weather.
     *
     * @param jsonObject a JSONObject
     */
    private void handleCurrentResult(JSONObject jsonObject) {
        Log.i("Weather", "Made it to handle result");
        ArrayList<HomeFragment> temp = new ArrayList<>();
        try {
            JSONObject weatherData = jsonObject.getJSONObject("weatherData");
            JSONArray weather = weatherData.getJSONArray("weather");
            JSONObject currWeather = weather.getJSONObject(0);
            String description = currWeather.getString("description");
            String icon = currWeather.getString("icon");

            JSONObject main = weatherData.getJSONObject("main");
            double temper = main.getDouble("temp");
            double minTemp = main.getDouble("temp_min");
            double maxTemp = main.getDouble("temp_max");
            double humidity = main.getDouble("humidity");
            temp.add(new HomeFragment("Today",description, temper, minTemp, maxTemp, humidity, icon));

            mHome.setValue(temp);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle current weather");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }


    /**
     * Handles errors with the webservice.
     *
     * @param error the error.
     */
    private void handleError(final VolleyError error) {
        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
        throw new IllegalStateException(error.getMessage());
    }
}
