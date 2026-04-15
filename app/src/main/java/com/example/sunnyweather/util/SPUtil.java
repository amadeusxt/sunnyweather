package com.example.sunnyweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.sunnyweather.WeatherBean;
import com.google.gson.Gson;

public class SPUtil {

    private static final String SP_NAME = "weather_sp";

    private static final String KEY_WEATHER_DATA = "key_weather_data";

    private static final String KEY_BING_IMAGE_URL = "key_bing_image_url";

    private static final String KEY_FORECAST_RANDOM_IMGS = "key_forecast_random_imgs";

    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }


    public static void saveWeatherData(Context context, WeatherBean weatherBean) {
        if (weatherBean == null) return;
        Gson gson = new Gson();
        String weatherJson = gson.toJson(weatherBean);
        getSP(context).edit().putString(KEY_WEATHER_DATA, weatherJson).apply();
    }

    public static WeatherBean getWeatherData(Context context) {
        String weatherJson = getSP(context).getString(KEY_WEATHER_DATA, null);
        if (weatherJson == null || weatherJson.isEmpty()) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(weatherJson, WeatherBean.class);
    }


    public static void clearWeatherData(Context context) {
        getSP(context).edit().remove(KEY_WEATHER_DATA).apply();
    }


    public static void saveBingImageUrl(Context context, String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) return;
        getSP(context).edit().putString(KEY_BING_IMAGE_URL, imageUrl).apply();
    }


    public static String getBingImageUrl(Context context) {
        return getSP(context).getString(KEY_BING_IMAGE_URL, null);
    }


    public static void saveForecastRandomImgUrls(Context context, String imgUrlsJson) {
        if (imgUrlsJson == null || imgUrlsJson.isEmpty()) return;
        getSP(context).edit().putString(KEY_FORECAST_RANDOM_IMGS, imgUrlsJson).apply();
    }


    public static String getForecastRandomImgUrls(Context context) {
        return getSP(context).getString(KEY_FORECAST_RANDOM_IMGS, null);
    }


    public static void clearImageCache(Context context) {
        getSP(context).edit()
                .remove(KEY_BING_IMAGE_URL)
                .remove(KEY_FORECAST_RANDOM_IMGS)
                .apply();
    }
}