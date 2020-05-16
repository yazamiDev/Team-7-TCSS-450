package edu.uw.team7project.ui.weather;

public class Weather {

    private final String mCondition;
    private final int mTemp;

    public Weather(String condition, int temp){
        mCondition = condition;
        mTemp = temp;
    }

    public String getCondition(){ return mCondition; }

    public int getTemp() { return mTemp; }

}
