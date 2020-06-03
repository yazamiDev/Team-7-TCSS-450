package edu.uw.team7project.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentHomeBinding;
import edu.uw.team7project.model.UserInfoViewModel;
import edu.uw.team7project.ui.contacts.ContactRequestListFragment;
import edu.uw.team7project.ui.weather.WeatherFragment;

/**
 * Subclass for the home fragment.
 *
 * @author Bradlee Laird
 */
public class HomeFragment extends Fragment {

    private static final String HARD_CODED_CITY= "Tacoma";
    private UserInfoViewModel mUserModel;
    private HomeViewModel mHomeModel;

    private String mCondition;
    private double mTemp;
    private double mMinTemp;
    private double mMaxTemp;
    private double mHumidity;
    private String mIcon;
    private String mDay;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewModelProvider provider = new ViewModelProvider(getActivity());

        mUserModel = provider.get(UserInfoViewModel.class);

        mHomeModel =  provider.get(HomeViewModel.class);
        mHomeModel.connectGetCurrent(HARD_CODED_CITY, mUserModel.getJwt());

        FragmentManager manager = getChildFragmentManager();
//        WeatherFragment weatherFragment = new WeatherFragment();
//        manager.beginTransaction().replace(R.id.weather_container, weatherFragment,
//                weatherFragment.getTag()).commit();

        ContactRequestListFragment contactRequestListFragment = new ContactRequestListFragment();
        manager.beginTransaction().replace(R.id.contact_request_container, contactRequestListFragment,
                contactRequestListFragment.getTag()).commit();

    }

    /**
     * Inflates the container for the fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * Creates an instance of the fragment for when the user returns to it.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentHomeBinding binding = FragmentHomeBinding.bind(getView());
        mHomeModel.addHomeObserver(getViewLifecycleOwner(), weatherList -> {
            //if (!messageList.isEmpty()) {
            binding.listRoot.setAdapter(
                    new HomeRecyclerViewAdapter(weatherList)
            );
            binding.layoutWait.setVisibility(View.GONE);
            //}else navigate to a no messages fragment.
        });

    }


    /**
     * A contructor for Home fragment
     *
     * @param condition the condition
     * @param temp the temp
     * @param minTemp the min temp
     * @param maxTemp the max temp
     * @param humidity the humidity
     * @param icon an icon
     */
    public HomeFragment(String day, String condition, double temp, double minTemp, double maxTemp,
                        double humidity, String icon){
        mDay = day;
        mCondition = condition;
        mTemp = temp;
        mMinTemp = minTemp;
        mMaxTemp = maxTemp;
        mHumidity = humidity;
        mIcon  = icon;
    }

    /**
     * Get the condition
     *
     * @return the condition
     */
    public String getCondition(){ return mCondition; }

    /**
     * Get the current day.
     *
     * @return a string representing the day.
     */
    public String getDay() { return mDay; }

    /**
     * Get the temp
     *
     * @return the temp
     */
    public double getTemp() { return mTemp; }

    /**
     * Get the min temp
     *
     * @return the min temp
     */
    public double getMinTemp() { return mMinTemp; }

    /**
     * Get the max temp
     *
     * @return the max temp
     */
    public double getMaxTemp() { return mMaxTemp; }

    /**
     * Get the humidity
     *
     * @return the humidity.
     */
    public double getHumidity() { return mHumidity; }

    @NonNull
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Condition: " + mCondition + "\n");
        result.append("Temperature: " + mTemp + "\n");
        result.append("Min Temp: " + mMinTemp + "\n");
        result.append("Max Temp: " + mMaxTemp + "\n");
        result.append("Humidity: " + mHumidity + "\n");
        return result.toString();
    }


}