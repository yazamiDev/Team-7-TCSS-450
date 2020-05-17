package edu.uw.team7project.ui.weather;

import android.media.Image;

public class Weather {

    private final String mCondition;
    private final double mTemp;
    private final double mMinTemp;
    private final double mMaxTemp;
    private final double mHumidity;
    private final String mIcon;

    public Weather(String condition, double temp, double minTemp, double maxTemp,
                   double humidity, String icon){
        mCondition = condition;
        mTemp = temp;
        mMinTemp = minTemp;
        mMaxTemp = maxTemp;
        mHumidity = humidity;
        mIcon  = icon;
    }

    public String getCondition(){ return mCondition; }

    public double getTemp() { return mTemp; }

    public double getMinTemp() { return mMinTemp; }

    public double getMaxTemp() { return mMaxTemp; }

    public double getHumidity() { return mHumidity; }

}
