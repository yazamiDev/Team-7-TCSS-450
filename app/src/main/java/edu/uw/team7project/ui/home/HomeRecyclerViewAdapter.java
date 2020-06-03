package edu.uw.team7project.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.team7project.R;
import edu.uw.team7project.databinding.FragmentWeatherCardBinding;
import edu.uw.team7project.ui.weather.Weather;

/**
 * A recycler view for weather.
 *
 * @author Trevor Nichols
 */
public class HomeRecyclerViewAdapter extends
        RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeViewHolder> {

    private final List<HomeFragment> mHome;

    /**
     * Constructor for weather recycler
     * @param item
     */
    public HomeRecyclerViewAdapter(List<HomeFragment> item){
        this.mHome = item;
    }


    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_weather_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.setWeather(mHome.get(position));
    }

    public int getItemCount() {
        return 1;
    }

    /**
     * A view Holder for weather.
     */
    public class HomeViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public FragmentWeatherCardBinding binding;

        public HomeViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentWeatherCardBinding.bind(view);

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
        void setWeather(final HomeFragment weather) {
            binding.textDate.setText(weather.getDay());
            binding.textWeatherInfo.setText(weather.toString());
        }
    }
}
