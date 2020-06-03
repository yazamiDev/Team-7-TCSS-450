package edu.uw.team7project.ui.weather;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentWeatherBinding;
import edu.uw.team7project.model.UserInfoViewModel;

import static android.graphics.Color.BLACK;

/**
 * Subclass for the home fragment.
 *
 * @author Bradlee Laird
 *
 * Old code that Trevor threw together for sprint 2.
 */
//public class WeatherFragment extends Fragment {
//
//    private static final String HARD_CODED_CITY= "Tacoma";
//
//    private UserInfoViewModel mUserModel;
//    private WeatherViewModel mWeatherModel;
//
//    public WeatherFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        ViewModelProvider provider = new ViewModelProvider(getActivity());
//
//        mUserModel = provider.get(UserInfoViewModel.class);
//        mWeatherModel =  provider.get(WeatherViewModel.class);
//        mWeatherModel.connectGetCurrent(HARD_CODED_CITY, mUserModel.getJwt());
////        mWeatherModel.connectFiveDayWeather(HARD_CODED_CITY, mUserModel.getJwt());
//    }
//
//    /**
//     * Inflates the container for the fragment.
//     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_weather, container, false);
//    }
//
//    /**
//     * Creates an instance of the fragment for when the user returns to it.
//     */
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
//        //used here.
//        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());
//        mWeatherModel.addWeatherObserver(getViewLifecycleOwner(), weatherList -> {
//            //if (!messageList.isEmpty()) {
//            binding.listRoot.setAdapter(
//                    new WeatherRecyclerViewAdapter(weatherList)
//            );
//            binding.layoutWait.setVisibility(View.GONE);
//            //}else navigate to a no messages fragment.
//        });
//    }
//}

/**
 * Subclass for the home fragment.
 *
 * @author Bradlee Laird
 */
public class WeatherFragment extends Fragment {

    private  WeatherViewModel mWeatherModelCurrent;
    private  WeatherViewModel mWeatherModelMultiple;
    private FragmentWeatherBinding binding;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherModelCurrent = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
        mWeatherModelMultiple = new ViewModelProvider(getActivity()).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWeatherModelCurrent.connectToWeatherCurrent();
        mWeatherModelMultiple.connectToWeatherMultiple();
        mWeatherModelCurrent.addResponseObserver(getViewLifecycleOwner(),
                this::observeWeatherCurrent);
        mWeatherModelMultiple.addResponseObserver(getViewLifecycleOwner(),
                this::observeWeatherMultiple);

        binding.weatherMainFab.setOnClickListener(button -> Navigation.findNavController(getView()).
                navigate(WeatherFragmentDirections.actionNavigationWeatherToLocationFragment()));

//        binding.layoutWait.setVisibility(View.GONE);
    }

    /**
     * Gets the weather information for the current conditions.
     * @param response the json
     */
    private void observeWeatherCurrent(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.weatherTemperature.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    getCurrentWeather(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * Gets the weather conditions for both the 5 day and hourly conditions.
     * @param response the json
     */
    private void observeWeatherMultiple(JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.weatherTemperature.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    getMultipleWeather(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /**
     * Gets the current weather and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getCurrentWeather(JSONObject response) throws JSONException{
        //To get the message json
        JSONObject jsonMessage = new JSONObject(response.getString("WeatherInfo"));
        Log.i("TESTING", jsonMessage.toString());

        //binding.cityName.setText(response.getString("message"));
        binding.weatherCityCountry.setText(jsonMessage.getString("name"));

        //to get the main json, then the temp
        JSONObject jsonMain = new JSONObject(jsonMessage.getString("main"));
        Float kelvin = Float.parseFloat(jsonMain.getString("temp"));
        Log.i("json", jsonMessage.toString());
        int temperature = (int)kelvinToFar(kelvin);
        binding.weatherTemperature.setText(String.valueOf(temperature + "°"));

        //gets the array held in the 'weather' section
        JSONArray jsonWeatherArray = new JSONArray(jsonMessage
                .getString("weather"));

        //converts the above array into a string to be cast into JSONObject
        String str = jsonWeatherArray.getString(0);

        //Casts into JSONObject and extract the description for the fragment
        String jsonWeather = new JSONObject(str).getString("description");
        binding.weatherConditonDescription.setText(jsonWeather);
    }

    /**
     * Gets the 5 day and hourly forecasts and formats it.
     * @param response the json
     * @throws JSONException json
     */
    public void getMultipleWeather(JSONObject response) throws JSONException{
        ArrayList<Integer> temps = new ArrayList<Integer>();
        ArrayList<String> descriptions = new ArrayList<String>();
        JSONObject jsonMessage = new JSONObject(response.getString("message"));
        Log.i("DAILY", jsonMessage.getString("daily"));
        JSONArray jsonDaily = jsonMessage.getJSONArray("daily");
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
        binding.weatherHour6.setText(secondFormat.format(hour1) + hourTemps.get(0) + "°, "
                + hourDescriptions.get(5));
        binding.weatherHour7.setText(secondFormat.format(hour2) + hourTemps.get(1) + "°, "
                + hourDescriptions.get(6));
        binding.weatherHour8.setText(secondFormat.format(hour3) + hourTemps.get(2) + "°, "
                + hourDescriptions.get(7));
        binding.weatherHour9.setText(secondFormat.format(hour4) + hourTemps.get(3) + "°, "
                + hourDescriptions.get(8));
        binding.weatherHour10.setText(secondFormat.format(hour5) + hourTemps.get(4) + "°, "
                + hourDescriptions.get(9));
        binding.weatherHour11.setText(secondFormat.format(hour1) + hourTemps.get(0) + "°, "
                + hourDescriptions.get(10));
        binding.weatherHour12.setText(secondFormat.format(hour2) + hourTemps.get(1) + "°, "
                + hourDescriptions.get(11));
        binding.weatherHour13.setText(secondFormat.format(hour3) + hourTemps.get(2) + "°, "
                + hourDescriptions.get(12));
        binding.weatherHour14.setText(secondFormat.format(hour4) + hourTemps.get(3) + "°, "
                + hourDescriptions.get(13));
        binding.weatherHour15.setText(secondFormat.format(hour5) + hourTemps.get(4) + "°, "
                + hourDescriptions.get(14));
        binding.weatherHour16.setText(secondFormat.format(hour1) + hourTemps.get(0) + "°, "
                + hourDescriptions.get(15));
        binding.weatherHour17.setText(secondFormat.format(hour2) + hourTemps.get(1) + "°, "
                + hourDescriptions.get(16));
        binding.weatherHour18.setText(secondFormat.format(hour3) + hourTemps.get(2) + "°, "
                + hourDescriptions.get(17));
        binding.weatherHour19.setText(secondFormat.format(hour4) + hourTemps.get(3) + "°, "
                + hourDescriptions.get(18));
        binding.weatherHour20.setText(secondFormat.format(hour5) + hourTemps.get(4) + "°, "
                + hourDescriptions.get(19));
        binding.weatherHour21.setText(secondFormat.format(hour1) + hourTemps.get(0) + "°, "
                + hourDescriptions.get(20));
        binding.weatherHour22.setText(secondFormat.format(hour2) + hourTemps.get(1) + "°, "
                + hourDescriptions.get(21));
        binding.weatherHour23.setText(secondFormat.format(hour3) + hourTemps.get(2) + "°, "
                + hourDescriptions.get(22));
        binding.weatherHour24.setText(secondFormat.format(hour4) + hourTemps.get(3) + "°, "
                + hourDescriptions.get(23));



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

