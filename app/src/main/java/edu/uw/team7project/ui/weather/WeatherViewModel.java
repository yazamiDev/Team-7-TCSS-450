package edu.uw.team7project.ui.weather;

import android.app.Application;
import android.util.Log;
import android.view.View;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.uw.team7project.databinding.FragmentWeatherBinding;
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

    private MutableLiveData<JSONObject> mResponse;
    private MutableLiveData<JSONObject> mResponseFiveDay;
    private MutableLiveData<ArrayList<Weather>> mWeather;
    private View mView;


    public WeatherViewModel(@NonNull Application application) {
        super(application);
//        Log.i("Current Lat/Long", city);
        mResponse = new MutableLiveData<>();
        mResponse.setValue(new JSONObject());
        mResponseFiveDay = new MutableLiveData<>();
        mResponseFiveDay.setValue(new JSONObject());
        mWeather = new MutableLiveData<>();
    }

    public void setView(View view) {
        mView = view;
    }

    public void addResponseObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super JSONObject> observer) {
        mResponse.observe(owner, observer);
        mResponseFiveDay.observe(owner, observer);
    }

    private void handleErrorCurrent(final VolleyError error) {
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
            String data = new String(error.networkResponse.data, Charset.defaultCharset());
            try {
                mResponse.setValue(new JSONObject("{" +
                        "code:" + error.networkResponse.statusCode +
                        ", data:" + data +
                        "}"));
            } catch (JSONException e) {
                Log.e("JSON PARSE", "JSON Parse Error in handleError");
            }
        }
    }

    public void connectToWeatherCurrent(String city) {
        try {
            Log.i("Made it:", "HERE ONE");

            Log.i("Current Lat/Long", city);

            String append1 = city.replace("(", "");
            String append2 = append1.replace(")", "");
            String append3 = append2.replace(",", "&");
            String append4 = append3.replace(" ", "");
            String append5 = append4.replace("lat/lng:", "");

            Log.i("Appended Lat/Long", append5);

            String urlCurrentDay = "https://mobile-app-spring-2020.herokuapp.com/weather/gps/" + append5;

            JSONObject body = new JSONObject();
            try {
                body.put("name", "daily");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Request request = new JsonObjectRequest(
                    Request.Method.GET,
                    urlCurrentDay,
                    body, //no body for this get request
                    mResponse::setValue,
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

    private void handleSuccessMultiple(JSONObject jsonObject) {

    }

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

    public void connectToWeatherMultiple(String city) {
        try {
            Log.i("Made it:", "HERE TWO");

            String append1 = city.replace("(", "");
            String append2 = append1.replace(")", "");
            String append3 = append2.replace(",", "");
            String append4 = append3.replace(" ", "");
            String append5 = append4.replace("lat/lng:", "");

            String urlFiveDay = "https://mobile-app-spring-2020.herokuapp.com/weather/gps/" + append5;

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
                    this::handleSuccessMultiple,
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

    private void handleSuccessCurrent(JSONObject response) {

//        Log.i("Weather", "Made it to handle result");
//        ArrayList<Weather> temp = new ArrayList<>();
//        try {
//
//            JSONObject jsonMessage = new JSONObject(response.getString("WeatherInfo"));
//            String city = jsonMessage.getString("name");
//            Log.i("TESTING", city);
//
//            //binding.cityName.setText(response.getString("message"));
////            binding.weatherCityCountry.setText(jsonMessage.getString("name"));
//
//            JSONObject coord = response.getJSONObject("coord");
//            double lon = coord.getDouble("lon");
//            double lat = coord.getDouble("lat");
//
//            JSONObject weatherData = response.getJSONObject("weatherData");
//            JSONArray weather = weatherData.getJSONArray("weather");
//
//            JSONObject currWeather = weather.getJSONObject(0);
//            String description = currWeather.getString("description");
//
//            String icon = currWeather.getString("icon");
//
//            JSONObject main = weatherData.getJSONObject("main");
//            double temper = main.getDouble("temp");
//            double minTemp = main.getDouble("temp_min");
//            double maxTemp = main.getDouble("temp_max");
//            double humidity = main.getDouble("humidity");
//            double pressure = main.getDouble("pressure");
//
//
//
//            temp.add(new Weather("Today", description, temper, minTemp, maxTemp, humidity, icon, city, pressure));
//
//            mWeather.setValue(temp);

        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(mView);

        ArrayList<Integer> temps = new ArrayList<Integer>();
        ArrayList<String> descriptions = new ArrayList<String>();
        JSONObject jsonMessage = null;
        try {
            jsonMessage = new JSONObject(response.getString("message"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        //Log.i("DAILY", jsonMessage.getString("daily").toString());

        JSONArray jsonDaily = null;
        try {
            jsonDaily = jsonMessage.getJSONArray("daily");

        for(int i = 0; i < 5; i++) {
            JSONObject jsonDay = new JSONObject(jsonDaily.getString(i));
            JSONObject jsonTemp = new JSONObject(jsonDay.getString("temp"));
            Float kelvin = Float.parseFloat(jsonTemp.getString("day"));
            int temperature = (int)kelvinToFar(kelvin);
            temps.add(temperature);
        }

        for(int i = 0; i < 5; i++) {
            JSONObject jsonDay = new JSONObject(jsonDaily.getString(i));
            JSONArray jsonWeather = new JSONArray(jsonDay.getString("weather"));
            JSONObject jsonInsideWeather = new JSONObject(jsonWeather.getString(0));
            String desc = jsonInsideWeather.getString("description");

            descriptions.add(desc);
        }

        Log.i("TEMPS", temps.toString());
        Log.i("DESCS", descriptions.toString());

        DateFormat df = new SimpleDateFormat("EE MMM dd:   ");
        Calendar cal = Calendar.getInstance();
        //String tod = df.format(today);

        //Manually add the 5 day forecast
        binding.weatherDay1.setText(df.format(cal.getTime()) + temps.get(0) + "°, "
                + descriptions.get(0));

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.weatherDay2.setText(df.format(cal.getTime()) + temps.get(1) + "°, "
                + descriptions.get(1));

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.weatherDay3.setText(df.format(cal.getTime()) + temps.get(2) + "°, "
                + descriptions.get(2));

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.weatherDay4.setText(df.format(cal.getTime()) + temps.get(3) + "°, "
                + descriptions.get(3));

        cal.add(Calendar.DAY_OF_YEAR, 1);
        binding.weatherDay5.setText(df.format(cal.getTime()) + temps.get(4) + "°, "
                + descriptions.get(4));

        //HOURLY
        ArrayList<Integer> hourTemps = new ArrayList<Integer>();
        ArrayList<String> unixTime = new ArrayList<String>();
        ArrayList<String> hourDescriptions = new ArrayList<String>();
        JSONArray jsonHourly = jsonMessage.getJSONArray("hourly");

        //The temps
        for(int i = 0; i < 24; i++) {
            JSONObject jsonInnerHourly = new JSONObject(jsonHourly.getString(i));
            //get unix time
            unixTime.add(jsonInnerHourly.getString("dt"));
            //rest
            String thisTemp = jsonInnerHourly.getString("temp");
            Float kelvin = Float.parseFloat(thisTemp);
            int temperature = (int)kelvinToFar(kelvin);
            hourTemps.add(temperature);
        }
        Log.i("HOURLY TEMPS", hourTemps.toString());

        //The Descriptions
        for(int i = 0; i < 24; i++) {
            JSONObject jsonInnerHourly = new JSONObject(jsonHourly.getString(i));
            JSONArray descArr = new JSONArray(jsonInnerHourly.getString("weather"));
            JSONObject hourWeather = new JSONObject(descArr.getString(0));
            hourDescriptions.add(hourWeather.getString("description"));
        }
        Log.i("HOUR DESCRIPTIONS", hourDescriptions.toString());
        Log.i("UNIX TIMES", unixTime.toString());
        Date expiry = new Date(Long.parseLong(unixTime.get(0)) * 1000);
        String exp = expiry.toString();
        //Log.i("UNIX TIME!!!!!", exp);
        DateFormat secondFormat = new SimpleDateFormat("HH:mm:ss -    ");
        Log.i("UNIX TIME!!!!!", secondFormat.format(expiry));

        //time of day in hour:min:sec
        Date hour1 = new Date(Long.parseLong(unixTime.get(0)) * 1000);
        Date hour2 = new Date(Long.parseLong(unixTime.get(1)) * 1000);
        Date hour3 = new Date(Long.parseLong(unixTime.get(2)) * 1000);
        Date hour4 = new Date(Long.parseLong(unixTime.get(3)) * 1000);
        Date hour5 = new Date(Long.parseLong(unixTime.get(4)) * 1000);
        Date hour6 = new Date(Long.parseLong(unixTime.get(5)) * 1000);
        Date hour7 = new Date(Long.parseLong(unixTime.get(6)) * 1000);
        Date hour8 = new Date(Long.parseLong(unixTime.get(7)) * 1000);
        Date hour9 = new Date(Long.parseLong(unixTime.get(8)) * 1000);
        Date hour10 = new Date(Long.parseLong(unixTime.get(9)) * 1000);
        Date hour11 = new Date(Long.parseLong(unixTime.get(10)) * 1000);
        Date hour12 = new Date(Long.parseLong(unixTime.get(11)) * 1000);
        Date hour13 = new Date(Long.parseLong(unixTime.get(12)) * 1000);
        Date hour14 = new Date(Long.parseLong(unixTime.get(13)) * 1000);
        Date hour15 = new Date(Long.parseLong(unixTime.get(14)) * 1000);
        Date hour16 = new Date(Long.parseLong(unixTime.get(15)) * 1000);
        Date hour17 = new Date(Long.parseLong(unixTime.get(16)) * 1000);
        Date hour18 = new Date(Long.parseLong(unixTime.get(17)) * 1000);
        Date hour19 = new Date(Long.parseLong(unixTime.get(18)) * 1000);
        Date hour20 = new Date(Long.parseLong(unixTime.get(19)) * 1000);
        Date hour21 = new Date(Long.parseLong(unixTime.get(20)) * 1000);
        Date hour22 = new Date(Long.parseLong(unixTime.get(21)) * 1000);
        Date hour23 = new Date(Long.parseLong(unixTime.get(22)) * 1000);
        Date hour24 = new Date(Long.parseLong(unixTime.get(23)) * 1000);


        binding.weatherHour1.setText(secondFormat.format(hour1) + hourTemps.get(0) + "°, "
                + hourDescriptions.get(0));
        binding.weatherHour2.setText(secondFormat.format(hour2) + hourTemps.get(1) + "°, "
                + hourDescriptions.get(1));
        binding.weatherHour3.setText(secondFormat.format(hour3) + hourTemps.get(2) + "°, "
                + hourDescriptions.get(2));
        binding.weatherHour4.setText(secondFormat.format(hour4) + hourTemps.get(3) + "°, "
                + hourDescriptions.get(3));
        binding.weatherHour5.setText(secondFormat.format(hour5) + hourTemps.get(4) + "°, "
                + hourDescriptions.get(4));
        binding.weatherHour6.setText(secondFormat.format(hour6) + hourTemps.get(5) + "°, "
                + hourDescriptions.get(5));
        binding.weatherHour7.setText(secondFormat.format(hour7) + hourTemps.get(6) + "°, "
                + hourDescriptions.get(6));
        binding.weatherHour8.setText(secondFormat.format(hour8) + hourTemps.get(7) + "°, "
                + hourDescriptions.get(7));
        binding.weatherHour9.setText(secondFormat.format(hour9) + hourTemps.get(8) + "°, "
                + hourDescriptions.get(8));
        binding.weatherHour10.setText(secondFormat.format(hour10) + hourTemps.get(9) + "°, "
                + hourDescriptions.get(9));
        binding.weatherHour11.setText(secondFormat.format(hour11) + hourTemps.get(10) + "°, "
                + hourDescriptions.get(10));
        binding.weatherHour12.setText(secondFormat.format(hour12) + hourTemps.get(11) + "°, "
                + hourDescriptions.get(11));
        binding.weatherHour13.setText(secondFormat.format(hour13) + hourTemps.get(12) + "°, "
                + hourDescriptions.get(12));
        binding.weatherHour14.setText(secondFormat.format(hour14) + hourTemps.get(13) + "°, "
                + hourDescriptions.get(13));
        binding.weatherHour15.setText(secondFormat.format(hour15) + hourTemps.get(14) + "°, "
                + hourDescriptions.get(14));
        String weather16 = secondFormat.format(hour16) + hourTemps.get(15) + "°, " + hourDescriptions.get(15);
        binding.weatherHour16.setText(weather16);
        binding.weatherHour17.setText(secondFormat.format(hour17) + hourTemps.get(16) + "°, "
                + hourDescriptions.get(16));
        binding.weatherHour18.setText(secondFormat.format(hour18) + hourTemps.get(17) + "°, "
                + hourDescriptions.get(17));
        binding.weatherHour19.setText(secondFormat.format(hour19) + hourTemps.get(18) + "°, "
                + hourDescriptions.get(18));
        binding.weatherHour20.setText(secondFormat.format(hour20) + hourTemps.get(19) + "°, "
                + hourDescriptions.get(19));
        binding.weatherHour21.setText(secondFormat.format(hour21) + hourTemps.get(20) + "°, "
                + hourDescriptions.get(20));
        binding.weatherHour22.setText(secondFormat.format(hour22) + hourTemps.get(21) + "°, "
                + hourDescriptions.get(21));
        binding.weatherHour23.setText(secondFormat.format(hour23) + hourTemps.get(22) + "°, "
                + hourDescriptions.get(22));
        binding.weatherHour24.setText(secondFormat.format(hour24) + hourTemps.get(23) + "°, "
                + hourDescriptions.get(23));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Converts Kelvin to Farenheit
     * @param theInt
     * @return
     */
    public long kelvinToFar(Float theInt) {
        //If 9.0 and 5.0 not stated as doubles then they will use int division and result in 1
        double temp = (theInt * (9.0/5.0)) - 459.67;
        return Math.round(temp);
    }
}
