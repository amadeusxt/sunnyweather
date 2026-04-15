package com.example.sunnyweather;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherBean {
    @SerializedName("province")
    private String province;
    @SerializedName("city")
    private String city;
   @SerializedName("district")
   private String district;
    @SerializedName("weather")
    private String weather;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @SerializedName("temperature")
    private Double temperature;
    @SerializedName("wind_direction")
    private String windDirection;
    @SerializedName("wind_power")
    private String windPower;
    @SerializedName("humidity")
    private int humidity;
    @SerializedName("report_time")
    private String reportTime;
    @SerializedName("temp_max")
    private int tempMax;
    @SerializedName("temp_min")
    private int tempMin;
    @SerializedName("forecast")
    private List<ForecastBean> forecast;




    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindPower() {
        return windPower;
    }

    public void setWindPower(String windPower) {
        this.windPower = windPower;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
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

    public List<ForecastBean> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastBean> forecast) {
        this.forecast = forecast;
    }

    @Override
    public String toString() {
        return "WeatherBean{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", weather='" + weather + '\'' +
                ", temperature=" + temperature +
                ", windDirection='" + windDirection + '\'' +
                ", windPower='" + windPower + '\'' +
                ", humidity=" + humidity +
                ", reportTime='" + reportTime + '\'' +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", forecast=" + forecast +
                '}';
    }
}