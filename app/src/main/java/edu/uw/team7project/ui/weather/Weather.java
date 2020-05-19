package edu.uw.team7project.ui.weather;

import android.media.Image;

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
    public Weather(String condition, double temp, double minTemp, double maxTemp,
                   double humidity, String icon){
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

}
