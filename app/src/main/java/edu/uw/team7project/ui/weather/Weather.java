package edu.uw.team7project.ui.weather;

import android.media.Image;

import androidx.annotation.NonNull;

/**
 * A class representing weather.
 *
 * @author  Trevor Nichols
 */
public class Weather {

    private final String mCondition;
    private final double mTemp;
    private final double mMinTemp;
    private final double mMaxTemp;
    private final double mHumidity;
    private final String mIcon;
    private final String mDay;

    /**
     * A contructor for weather
     *
     * @param condition the condition
     * @param temp the temp
     * @param minTemp the min temp
     * @param maxTemp the max temp
     * @param humidity the humidity
     * @param icon an icon
     */
    public Weather(String day, String condition, double temp, double minTemp, double maxTemp,
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
