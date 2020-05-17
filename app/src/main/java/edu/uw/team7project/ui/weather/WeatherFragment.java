package edu.uw.team7project.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentWeatherBinding;
import edu.uw.team7project.model.UserInfoViewModel;

/**
 * Subclass for the home fragment.
 *
 * @author Bradlee Laird
 */
public class WeatherFragment extends Fragment {

    private static final String HARD_CODED_CITY= "Tacoma";

    private UserInfoViewModel mUserModel;
    private WeatherViewModel mWeatherModel;
    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());

        mUserModel = provider.get(UserInfoViewModel.class);
        mWeatherModel =  provider.get(WeatherViewModel.class);
        mWeatherModel.connectGetCurrent(HARD_CODED_CITY, mUserModel.getJwt());
    }

    /**
     * Inflates the container for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    /**
     * Creates an instance of the fragment for when the user returns to it.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentWeatherBinding binding = FragmentWeatherBinding.bind(getView());

        Weather currWeather = mWeatherModel.getCurrentWeather();

        mWeatherModel.addMessageListObserver(getViewLifecycleOwner(), weather -> {
            String location = "Location: " + HARD_CODED_CITY;
            binding.textLocation.setText(location);

            String currTemp = "Current Temp: " + currWeather.getTemp();
            binding.textCurrentTemp.setText(currTemp);

            String minTemp = "Minimum Temp: " + currWeather.getMinTemp();
            binding.textMinTemp.setText(minTemp);

            String maxTemp = "MaximumTemp: " + currWeather.getMaxTemp();
            binding.textMaxTemp.setText(maxTemp);

            String humidity = "Humidity: " + weather.getHumidity();
            binding.textHumidity.setText(humidity);
        });
    }
}
