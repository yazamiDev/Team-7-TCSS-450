package edu.uw.team7project.ui.weather;

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.team7project.io.RequestQueueSingleton;
import edu.uw.team7project.ui.messages.MessagePost;

/**
 * A view model for weather.
 *
 * @author Trevor Nichols
 */
//public class WeatherViewModel extends AndroidViewModel {
//
//    private MutableLiveData<ArrayList<Weather>> mWeather;
//
//    /**
//     * A constructor for the weather view model.
//     * @param application the application
//     */
//    public WeatherViewModel(@NonNull Application application) {
//        super(application);
//        mWeather = new MutableLiveData<>();
//    }
//
//    public void addWeatherObserver(@NonNull LifecycleOwner owner,
//                                   @NonNull Observer<? super List<Weather>> observer) {
//        mWeather.observe(owner, observer);
//    }
//
//
//    /**
//     * Connects  to the webservice to get current conditions.
//     *
//     * @param city the city
//     * @param jwt a valid jwt
//     */
//    public void connectGetCurrent( String city, String jwt){
//        String url = "https://mobile-app-spring-2020.herokuapp.com/weather?name=" + city;
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null, //no body for this get request
//                this::handleCurrentResult,
//                this::handleError) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                // add headers <key,value>
//                headers.put("Authorization", jwt);
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        Volley.newRequestQueue(getApplication().getApplicationContext())
//                .add(request);
//    }
//
//    /**
//     * Handles results for current weather.
//     * @param jsonObject a JSONObject
//     */
//    private void handleCurrentResult(JSONObject jsonObject) {
//        Log.i("Weather", "Made it to handle result");
//        ArrayList<Weather> temp = new ArrayList<>();
//        try {
//            JSONObject coord = jsonObject.getJSONObject("coord");
//            double lon = coord.getDouble("lon");
//            double lat = coord.getDouble("lat");
//
//            JSONObject weatherData = jsonObject.getJSONObject("weatherData");
//            JSONArray weather = weatherData.getJSONArray("weather");
//            JSONObject currWeather = weather.getJSONObject(0);
//            String description = currWeather.getString("description");
//            String icon = currWeather.getString("icon");
//
//            JSONObject main = weatherData.getJSONObject("main");
//            double temper = main.getDouble("temp");
//            double minTemp = main.getDouble("temp_min");
//            double maxTemp = main.getDouble("temp_max");
//            double humidity = main.getDouble("humidity");
//
//
//
//            temp.add(new Weather("Today",description, temper, minTemp, maxTemp, humidity, icon));
//
//            mWeather.setValue(temp);
//        } catch (JSONException e) {
//            Log.e("JSON PARSE ERROR", "Found in handle current weather");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
//    }
//
//    public void connectFiveDayWeather(String city, String jwt) {
//        String url = "https://mobile-app-spring-2020.herokuapp.com/weather/5days?name=" + city;
//
//        Request request = new JsonObjectRequest(
//                Request.Method.GET,
//                url,
//                null, //no body for this get request
//                this::handleFiveDayResult,
//                this::handleError) {
//            @Override
//            public Map<String, String> getHeaders() {
//                Map<String, String> headers = new HashMap<>();
//                // add headers <key,value>
//                headers.put("Authorization", jwt);
//                return headers;
//            }
//        };
//        request.setRetryPolicy(new DefaultRetryPolicy(
//                10_000,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        //Instantiate the RequestQueue and add the request to the queue
//        Volley.newRequestQueue(getApplication().getApplicationContext())
//                .add(request);
//    }
//
//    private void handleFiveDayResult(JSONObject jsonObject) {
//        Log.i("Weather", "Made it to handle result");
//        ArrayList<Weather> temp = new ArrayList<>();
//        try {
//            JSONObject weatherData = jsonObject.getJSONObject("weatherData");
//            JSONArray list = weatherData.getJSONArray("list");
//            Log.i("WEATHER", String.valueOf(list.length()));
//            for(int i = 0; i < list.length(); i++){
//                JSONObject  object= list.getJSONObject(i);
//                JSONArray weather = object.getJSONArray("weather");
//                JSONObject currWeather = weather.getJSONObject(0);
//                String date = object.getString("dt_txt");
//                String description = currWeather.getString("description");
//                String icon = currWeather.getString("icon");
//
//                JSONObject main = object.getJSONObject("main");
//                double temper = main.getDouble("temp");
//                double minTemp = main.getDouble("temp_min");
//                double maxTemp = main.getDouble("temp_max");
//                double humidity = main.getDouble("humidity");
//                temp.add(new Weather(date, description, temper, minTemp, maxTemp, humidity, icon));
//            }
//
//            mWeather.setValue(temp);
//        } catch (JSONException e) {
//            Log.e("JSON PARSE ERROR", "Found in handle current weather");
//            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Handles errors with the webservice
//     * @param error the error.
//     */
//    private void handleError(final VolleyError error) {
//        Log.e("CONNECTION ERROR", error.getLocalizedMessage());
//        throw new IllegalStateException(error.getMessage());
//    }
//}

public class WeatherViewModel extends AndroidViewModel {

    private MutableLiveData<JSONObject> mResponseCurrent;
    private MutableLiveData<JSONObject> mResponseFiveDay;
    private String city = "47.608013&-122.335167";

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        mResponseCurrent = new MutableLiveData<>();
        mResponseCurrent.setValue(new JSONObject());
        mResponseFiveDay = new MutableLiveData<>();
        mResponseFiveDay.setValue(new JSONObject());
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponseCurrent.observe(owner, observer);
        mResponseFiveDay.observe(owner, observer);
    }

    private void handleErrorCurrent(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponseCurrent.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponseCurrent.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectToWeatherCurrent() {
        try {
            Log.i("Made it:", "HERE ONE");

            String urlCurrentDay = "https://mobile-app-spring-2020.herokuapp.com/weather/gps/" + city;

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    urlCurrentDay,
                    null, //no body for this get request
                    mResponseCurrent::setValue,
                    this::handleErrorCurrent);

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Instantiate the RequestQueue and add the request to the queue
            RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                    .addToRequestQueue(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleErrorMultiple(final VolleyError error) {
        if (Objects.isNull(error.networkResponse)) {
            try {
                mResponseFiveDay.setValue(new JSONObject("{" +
                        "error:\"" + error.getMessage() +
                        "\"}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
        else {
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponseFiveDay.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectToWeatherMultiple() {
        try {
            Log.i("Made it:", "HERE TWO");

            String urlFiveDay = "https://mobile-app-spring-2020.herokuapp.com/weather/gps/" + city;

            JSONObject body = new JSONObject();
            try {
                body.put("name", "5days");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    urlFiveDay,
                    body,
                    mResponseFiveDay::setValue,
                    this::handleErrorMultiple);

            request.setRetryPolicy(new DefaultRetryPolicy(
                    10_000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Instantiate the RequestQueue and add the request to the queue
            RequestQueueSingleton.getInstance(getApplication().getApplicationContext())
                    .addToRequestQueue(request);

            Log.i("5day works", "body: " + String.valueOf(body));
            Log.i("5day works", "response: " + String.valueOf(request));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
