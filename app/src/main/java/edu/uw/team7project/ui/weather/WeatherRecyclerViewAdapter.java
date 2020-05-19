package edu.uw.team7project.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentWeatherBinding;

/**
 * A recycler view for weather.
 *
 * @author Trevor Nichols
 */
public class WeatherRecyclerViewAdapter extends
        RecyclerView.Adapter<WeatherRecyclerViewAdapter.WeatherViewHolder> {

    private Weather mWeather;

    /**
     * Constructor for weather recycler
     * @param item
     */
    public WeatherRecyclerViewAdapter (Weather item){
        this.mWeather = item;
    }


    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        holder.setWeather(mWeather);
    }

    public int getItemCount() {
        return 1;
    }

    /**
     * A view Holder for weather.
     */
    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentWeatherBinding binding;

        public WeatherViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherBinding.bind(view);

            //may need to change
//            view.setOnClickListener(v -> {
//                navigateToChat();
//            });
        }


//        private void navigateToChat(){
//            MessageListFragmentDirections.ActionNavigationMessagesToChatFragment directions =
//                    MessageListFragmentDirections.actionNavigationMessagesToChatFragment(mKey);
//
//            Navigation.findNavController(mView).navigate(directions);
//        }

        /**
         * SEts the weather for the view holder.
         * @param weather the weather
         */
        void setWeather(final Weather weather) {
            binding.textLocation.setText("Tacoma");

            String condition = "condition: " + weather.getCondition();
            binding.textCurrentWeather.setText(condition);

            String currTemp = "Current Temp: " + weather.getTemp();
            binding.textCurrentTemp.setText(currTemp);

            String minTemp = "Minimum Temp: " + weather.getMinTemp();
            binding.textMinTemp.setText(minTemp);

            String maxTemp = "MaximumTemp: " + weather.getMaxTemp();
            binding.textMaxTemp.setText(maxTemp);

            String humidity = "Humidity: " + weather.getHumidity();
            binding.textHumidity.setText(humidity);
        }

    }
}
