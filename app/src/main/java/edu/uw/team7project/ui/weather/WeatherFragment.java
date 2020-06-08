package edu.uw.team7project.ui.weather;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private UserInfoViewModel mUserModel;
    private WeatherViewModel mWeatherModelCurrent;
    private WeatherViewModel mWeatherModelMultiple;
    private FragmentWeatherBinding binding;
    public static String initial_city = "47.608013&-122.335167";

    /**
     * char for degree symbol
     */
    private static final char DEGREE = (char) 0x00B0;

    /**
     * tags for weather for log
     */
    private static final String TAG = "WEATHER_FRAG";

    //current weather
    private Weather mWeather;

    //10d weather
    private Weather[] mWeathers5d;

    //24h weather
    private Weather[] mWeathers24h;

    //the fragment view
    private View mView;

    public WeatherFragment() {
        // Required empty public constructor
    }

//    private void initialization(@NonNull View view) {
//
//        WeatherFragmentArgs args = null;
//        if (getArguments() != null) {
//            args = WeatherFragmentArgs.fromBundle(getArguments());
//        }
//        if (args != null) {
//            if (mWeather == null) {
//                mEmail = args.getEmail();
//                mJwToken = args.getJwt();
//                mWeather = args.getWeather();
//                mWeathers10d = args.getWeathers10d();
//                mWeathers24h = args.getWeathers24h();
//            }
//        }
//        mView = view;
//        if (!mRowsUpdated) {
//            getRowsWeather();
//        }
//    }
//
//    /**
//     * handling the get rows weather
//     * @param result the given result
//     */
//    private void endOfGetRowsWeather(final String result) {
//        try {
//            Log.d(TAG, result);
//            JSONObject root = new JSONObject(result);
//
//            //the location count
//            int mLocationsCount = root.getInt(getString(keys_json_count));
//            if (!mRowsUpdated) {
//                mTempLocationsCount = mLocationsCount;
//                mRowsUpdated = true;
//            } else {
//                if (mLocationsCount == mTempLocationsCount + 1) {
//                    alert("Save successful", getContext());
//                    mTempLocationsCount++;
//                } else if (mLocationsCount == mTempLocationsCount) {
//                    alert("That location has already been saved", getContext());
//                } else if (mTempLocationsCount != 0) {
//                    alert("Unknown if location is saved. Problem occurred\n" +
//                            "Past location count is " + mTempLocationsCount +
//                            " and new location count is " + mLocationsCount, getContext());
//                }
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

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
        mWeatherModelCurrent.connectToWeatherCurrent(initial_city);
        mWeatherModelMultiple.connectToWeatherMultiple(initial_city);

        mWeatherModelCurrent.addResponseObserver(getViewLifecycleOwner(),
                this::observeWeatherCurrent);
        mWeatherModelMultiple.addResponseObserver(getViewLifecycleOwner(),
                this::observeWeatherMultiple);

        binding.weatherMainFab.setOnClickListener(button -> Navigation.findNavController(getView()).
                navigate(WeatherFragmentDirections.actionNavigationWeatherToLocationFragment()));

        binding.weatherTemperatureSwitch.setOnCheckedChangeListener(
                ((buttonView, isChecked) -> setTemperature()));

        binding.weatherForecastSwitch.setOnCheckedChangeListener(
                ((buttonView, isChecked) -> switchForecast()));

        setDays();
//        setTemperature();


//        binding.layoutWait.setVisibility(View.GONE);
    }

    /**
     * set the what temperature unit of the weathers
     */
    private void setTemperature() {


        if (binding.weatherTemperatureSwitch.isChecked()) {
            binding.weatherTemperature.setText(tempFromKelvinToFahrenheitString(mWeather.getTemp()));
            binding.weatherTemperature.setTextColor(BLACK);

            setTempDaysTextToFahrenheit();
            setTempHoursTextToFahrenheit();
        } else {

            binding.weatherTemperature.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
            binding.weatherTemperature.setTextColor(BLACK);

            setTempDaysTextToCelsius();
            setTempHoursTextToCelsius();

        }

//        TextView temp = mView.findViewById(weather_temperature);
//        if (((Switch) mView.findViewById(weather_temperatureSwitch)).isChecked()) {
//            temp.setText(tempFromKelvinToFahrenheitString(mWeather.getTemp()));
//            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), redviolet));
//
//            setTempDaysTextToFahrenheit();
//            setTempHoursTextToFahrenheit();
//
//        } else {
//            temp.setText(tempFromKelvinToCelsiusString(mWeather.getTemp()));
//            temp.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), uwPurple));
//
//            setTempDaysTextToCelsius();
//            setTempHoursTextToCelsius();
//        }
    }

    //set hours to celsius
    private void setTempHoursTextToCelsius() {

        binding.weatherHour1Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[0].getTemp()));
        binding.weatherHour2Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[1].getTemp()));
        binding.weatherHour3Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[2].getTemp()));
        binding.weatherHour4Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[3].getTemp()));
        binding.weatherHour5Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[4].getTemp()));
        binding.weatherHour6Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[5].getTemp()));
        binding.weatherHour7Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[6].getTemp()));
        binding.weatherHour8Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[7].getTemp()));
        binding.weatherHour9Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[8].getTemp()));
        binding.weatherHour10Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[9].getTemp()));
        binding.weatherHour11Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[10].getTemp()));
        binding.weatherHour12Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[11].getTemp()));
        binding.weatherHour13Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[12].getTemp()));
        binding.weatherHour14Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[13].getTemp()));
        binding.weatherHour15Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[14].getTemp()));
        binding.weatherHour16Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[15].getTemp()));
        binding.weatherHour17Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[16].getTemp()));
        binding.weatherHour18Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[17].getTemp()));
        binding.weatherHour19Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[18].getTemp()));
        binding.weatherHour20Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[19].getTemp()));
        binding.weatherHour21Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[20].getTemp()));
        binding.weatherHour22Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[21].getTemp()));
        binding.weatherHour23Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[22].getTemp()));
        binding.weatherHour24Temp.setText(tempFromKelvinToCelsiusString(mWeathers24h[23].getTemp()));

    }

    //set days to celsius
    private void setTempDaysTextToCelsius() {

        binding.weatherDay1.setText(tempFromKelvinToCelsiusString(mWeathers5d[0].getTemp()));
        binding.weatherDay2.setText(tempFromKelvinToCelsiusString(mWeathers5d[1].getTemp()));
        binding.weatherDay3.setText(tempFromKelvinToCelsiusString(mWeathers5d[2].getTemp()));
        binding.weatherDay4.setText(tempFromKelvinToCelsiusString(mWeathers5d[3].getTemp()));
        binding.weatherDay5.setText(tempFromKelvinToCelsiusString(mWeathers5d[4].getTemp()));

    }

    /**
     * set the hours to fahrenheit
     */
    private void setTempHoursTextToFahrenheit() {

        binding.weatherHour1Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[0].getTemp()));
        binding.weatherHour2Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[1].getTemp()));
        binding.weatherHour3Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[2].getTemp()));
        binding.weatherHour4Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[3].getTemp()));
        binding.weatherHour5Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[4].getTemp()));
        binding.weatherHour6Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[5].getTemp()));
        binding.weatherHour7Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[6].getTemp()));
        binding.weatherHour8Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[7].getTemp()));
        binding.weatherHour9Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[8].getTemp()));
        binding.weatherHour10Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[9].getTemp()));
        binding.weatherHour11Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[10].getTemp()));
        binding.weatherHour12Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[11].getTemp()));
        binding.weatherHour13Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[12].getTemp()));
        binding.weatherHour14Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[13].getTemp()));
        binding.weatherHour15Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[14].getTemp()));
        binding.weatherHour16Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[15].getTemp()));
        binding.weatherHour17Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[16].getTemp()));
        binding.weatherHour18Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[17].getTemp()));
        binding.weatherHour19Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[18].getTemp()));
        binding.weatherHour20Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[19].getTemp()));
        binding.weatherHour21Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[20].getTemp()));
        binding.weatherHour22Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[21].getTemp()));
        binding.weatherHour23Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[22].getTemp()));
        binding.weatherHour24Temp.setText(tempFromKelvinToFahrenheitString(mWeathers24h[23].getTemp()));
    }

    // set the days to fahrenheit
    private void setTempDaysTextToFahrenheit() {

        binding.weatherDay1.setText(tempFromKelvinToFahrenheitString(mWeathers5d[0].getTemp()));
        binding.weatherDay2.setText(tempFromKelvinToFahrenheitString(mWeathers5d[1].getTemp()));
        binding.weatherDay3.setText(tempFromKelvinToFahrenheitString(mWeathers5d[2].getTemp()));
        binding.weatherDay4.setText(tempFromKelvinToFahrenheitString(mWeathers5d[3].getTemp()));
        binding.weatherDay5.setText(tempFromKelvinToFahrenheitString(mWeathers5d[4].getTemp()));

    }

    /**
     * set days components
     */
    private void setDays() {
        // EEE MM/dd
//        TimeZone utc = TimeZone.getTimeZone("UTC");
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
//                = new SimpleDateFormat("MM/dd");
//        Calendar calendar = getNewTimzone(utc);
//        calendar.setTimeZone();

//        TimeZone timeZone = TimeZone.getTimeZone(mWeather.getTimezoneID());
        TimeZone timeZone = TimeZone.getTimeZone("America/Los_Angeles");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf
                = new SimpleDateFormat("MM/dd");
        sdf.setTimeZone(timeZone);
        Calendar calendar = Calendar.getInstance(timeZone);
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus2 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus3 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date todayPlus4 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        binding.weatherDay1.setText(sdf.format(today));
        binding.weatherDay2.setText(sdf.format(todayPlus1));
        binding.weatherDay3.setText(sdf.format(todayPlus2));
        binding.weatherDay4.setText(sdf.format(todayPlus3));
        binding.weatherDay5.setText(sdf.format(todayPlus4));

    }


    /**
     * @param tIcon open weather map and dark sky icon
     * @return the weather bit icon except fog(openweathermap)
     */
    public static String getNewIcon(String tIcon){
        switch (tIcon) {
            case "01d":
            case "clear-day":
                return "c01d";
            case "01n":
            case "clear-night":
                return "c01n";
            case "02d":
            case "partly-cloudy-day":
            case "03d":
                return "c02d";
            case "02n":
            case "partly-cloudy-night":
            case "03n":
                return "c02n";
            case "04d":
                return "c03d";
            case "04n":
                return "c03n";
            case "09d":
                return "r05d";
            case "09n":
                return "r05n";
            case "10d":
            case "rain":
            case "10n":
                return "r02d";
            case "thunderstorm":
            case "11d":
                return "t04d";
            case "11n":
                return "t04n";
            case "13d":
            case "snow":
            case "hail":
                return "s02d";
            case "13n":
                return "s02n";
            case "50d":
                return "a01d";
            case "50n":
                return "a01n";
            case "sleet":
                /* || tIcon == "wind"*/

                return "s05d";
            case "fog":
                return "50d";
//            case "fogd":
//                return "a05d";
//            case "fogn":
//                return "a05n";
//                return "50d";
        }
        return "c04d"; //sleet, cloudy, wind     would just be here
    }

    /**
     * @param tTemp kelvin to be converted to string Fahrenheit
     * @return the Fahrenheit temp
     */
    public static String tempFromKelvinToFahrenheitString(double tTemp) {
        return String.valueOf(Math.round(((tTemp - 273.15) * 9 / 5) + 32)) +
                DEGREE + "F";
    }

    /**
     *
     * @param tTemp kelvin to be converted to string Celsius
     * @return the Celsius temp
     */
    public static String tempFromKelvinToCelsiusString(double tTemp) {
        return String.valueOf(Math.round(tTemp - 273.15)) +
                DEGREE + "C";
    }

    /**
     *
     * @param tIcon the icon weatherbit except fog(openweathermap)
     * @return the url
     */
    public static String getImgUrl(String tIcon) {
        if (tIcon.equals("50d")) {
            return "http://openweathermap.org/img/wn/" + tIcon + "@2x.png";
        } else {
            return "https://www.weatherbit.io/static/img/icons/" + tIcon + ".png";
        }
    }

    /**
     * switch forecast from 5 days to 24 hours
     */
    private void switchForecast() {

        if (binding.weatherForecastSwitch.isChecked()) {

            binding.weatherConditonDescription.setText("Test");

//            binding.weather5DayForecast.setText("D");
            binding.weather5DayForecast.setTypeface(Typeface.DEFAULT_BOLD);
            binding.weather24HourForecast.setTypeface(Typeface.DEFAULT);

            binding.weather5DayForecast.setTextColor(getResources().getColor(R.color.uwPurple));
            binding.weather24HourForecast.setTextColor(BLACK);

            binding.weatherLayoutHours1.setVisibility(View.GONE);
            binding.weatherLayoutHours2.setVisibility(View.GONE);
            binding.weatherLayoutHours3.setVisibility(View.GONE);
            binding.weatherLayoutHours4.setVisibility(View.GONE);

            binding.weatherLayoutDays1.setVisibility(View.VISIBLE);
            binding.weatherLayoutDays2.setVisibility(View.VISIBLE);
        } else {

            binding.weather5DayForecast.setTypeface(Typeface.DEFAULT);
            binding.weather24HourForecast.setTypeface(Typeface.DEFAULT_BOLD);

            binding.weather5DayForecast.setTextColor(BLACK);
            binding.weather24HourForecast.setTextColor(getResources().getColor(R.color.uwPurple));

            binding.weatherLayoutHours1.setVisibility(View.VISIBLE);
            binding.weatherLayoutHours2.setVisibility(View.VISIBLE);
            binding.weatherLayoutHours3.setVisibility(View.VISIBLE);
            binding.weatherLayoutHours4.setVisibility(View.VISIBLE);

            binding.weatherLayoutDays1.setVisibility(View.GONE);
            binding.weatherLayoutDays2.setVisibility(View.GONE);
        }
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
                    setTemperature();

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
        if (response.length() >= 0) {
            Log.i("Reach", "This is called");


            if (response.has("code")) {
                try {
//                    Log.d("Reach", "This is called");
                    getMultipleWeather(response);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    binding.weatherTemperature.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
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
//        JSONObject jsonMessage = new JSONObject(response.getString("WeatherInfo"));
//        Log.i("TESTING", jsonMessage.toString());
//
//        //binding.cityName.setText(response.getString("message"));
//        binding.weatherCityCountry.setText(jsonMessage.getString("name"));
//        Log.i("City name please", jsonMessage.getString("name"));
//
//        //to get the main json, then the temp
//        JSONObject jsonMain = new JSONObject(jsonMessage.getString("main"));
//        Float kelvin = Float.parseFloat(jsonMain.getString("temp"));
//        Log.i("json", jsonMessage.toString());
//        int temperature = (int)kelvinToFar(kelvin);
//        binding.weatherTemperature.setText(String.valueOf(temperature + "°"));
//
//        //gets the array held in the 'weather' section
//        JSONArray jsonWeatherArray = new JSONArray(jsonMessage
//                .getString("weather"));
//
//        //converts the above array into a string to be cast into JSONObject
//        String str = jsonWeatherArray.getString(0);
//
//        //Casts into JSONObject and extract the description for the fragment
//        String jsonWeather = new JSONObject(str).getString("description");
//        binding.weatherConditonDescription.setText(jsonWeather);

        Log.i("Weather", "Made it to handle result");
        ArrayList<Weather> temp = new ArrayList<>();
        try {

            JSONObject jsonMessage = new JSONObject(response.getString("WeatherInfo"));
            String city = jsonMessage.getString("name");
            Log.i("TESTING", city);

            //binding.cityName.setText(response.getString("message"));
            binding.weatherCityCountry.setText(jsonMessage.getString("name"));

            JSONObject coord = response.getJSONObject("coord");
            double lon = coord.getDouble("lon");
            double lat = coord.getDouble("lat");

            JSONObject weatherData = response.getJSONObject("weatherInfo");
            JSONArray weather = weatherData.getJSONArray("weather");

            JSONObject currWeather = weather.getJSONObject(0);
            String description = currWeather.getString("description");

            String icon = currWeather.getString("icon");

            JSONObject timezone = weatherData.getJSONObject("timezone");

            JSONObject main = weatherData.getJSONObject("main");
            JSONObject weather2 = weatherData.getJSONObject("Weather");
            double temper = main.getDouble("temp");
            double minTemp = main.getDouble("temp_min");
            double maxTemp = main.getDouble("temp_max");
            double humidity = main.getDouble("humidity");
            double pressure = main.getDouble("pressure");
            String tIcon = weather2.getString("icon");
            long tTimezone = timezone.getLong("timezone");

//            binding.weatherPressureLabel.setText("Test");


            mWeather = new Weather("Today", description, temper, minTemp, maxTemp, humidity, icon, city, pressure, icon, tTimezone);
//            temp.add(new Weather("Today", description, temper, minTemp, maxTemp, humidity, icon, city, pressure, icon, tTimezone));

//            mWeather.setValue(temp);
        } catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in handle current weather");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
    }

    /**
     * Gets the 5 day and hourly forecasts and formats it.
     * @param response the json
     * @throws JSONException json
     */
    @SuppressLint("SetTextI18n")
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
        binding.weatherHour16.setText(secondFormat.format(hour16) + hourTemps.get(15) + "°, "
                + hourDescriptions.get(15));
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

