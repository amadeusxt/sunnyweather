package com.example.sunnyweather;

import com.google.gson.annotations.SerializedName;

public class ForecastBean {
    @SerializedName("date")
    private String date;
    @SerializedName("week")
    private String week;
    @SerializedName("temp_max")
    private int tempMax;
    @SerializedName("temp_min")
    private int tempMin;
    @SerializedName("weather_day")
    private String weatherDay;
    @SerializedName("weather_night")
    private String weatherNight;
    @SerializedName("wind_dir_day")
    private String windDirDay;
    @SerializedName("wind_dir_night")
    private String windDirNight;
    @SerializedName("wind_scale_day")
    private String windScaleDay;
    @SerializedName("wind_scale_night")
    private String windScaleNight;
    @SerializedName("wind_speed_day")
    private double windSpeedDay;
    @SerializedName("sunrise")
    private String sunrise;
    @SerializedName("sunset")
    private String sunset;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public String getWeatherDay() {
        return weatherDay;
    }

    public void setWeatherDay(String weatherDay) {
        this.weatherDay = weatherDay;
    }

    public String getWeatherNight() {
        return weatherNight;
    }

    public void setWeatherNight(String weatherNight) {
        this.weatherNight = weatherNight;
    }

    public String getWindDirDay() {
        return windDirDay;
    }

    public void setWindDirDay(String windDirDay) {
        this.windDirDay = windDirDay;
    }

    public String getWindDirNight() {
        return windDirNight;
    }

    public void setWindDirNight(String windDirNight) {
        this.windDirNight = windDirNight;
    }

    public String getWindScaleDay() {
        return windScaleDay;
    }

    public void setWindScaleDay(String windScaleDay) {
        this.windScaleDay = windScaleDay;
    }

    public String getWindScaleNight() {
        return windScaleNight;
    }

    public void setWindScaleNight(String windScaleNight) {
        this.windScaleNight = windScaleNight;
    }

    public double getWindSpeedDay() {
        return windSpeedDay;
    }

    public void setWindSpeedDay(double windSpeedDay) {
        this.windSpeedDay = windSpeedDay;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    @Override
    public String toString() {
        return "ForecastBean{" +
                "date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", weatherDay='" + weatherDay + '\'' +
                ", weatherNight='" + weatherNight + '\'' +
                ", windDirDay='" + windDirDay + '\'' +
                ", windDirNight='" + windDirNight + '\'' +
                ", windScaleDay='" + windScaleDay + '\'' +
                ", windScaleNight='" + windScaleNight + '\'' +
                ", windSpeedDay=" + windSpeedDay +
                ", sunrise='" + sunrise + '\'' +
                ", sunset='" + sunset + '\'' +
                '}';
    }
}