package com.example.sunnyweather.util;

import android.util.Log;
import com.example.sunnyweather.WeatherBean;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherUtil {
    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    private static final String ADCODE_URL = "https://uapis.cn/api/v1/misc/district";

    private static final String WEATHER_URL = "https://uapis.cn/api/v1/misc/weather";

    public interface WeatherCallback {
        void onSuccess(WeatherBean weatherBean);
        void onFailure(String errorMsg);
    }
    private interface AdcodeCallback {
        void onSuccess(String adcode);
        void onFailure(String errorMsg);
    }
//获取城市名
    public static void getWeather(String city, WeatherCallback callback) {
        if (city == null || city.trim().isEmpty()) {
            callback.onFailure("城市名不能为空");
            return;
        }

        getAdcodeByCity(city.trim(), new AdcodeCallback() {
            @Override
            public void onSuccess(String adcode) {

                getWeatherByAdcode(adcode, callback);
            }

            @Override
            public void onFailure(String errorMsg) {
                callback.onFailure(errorMsg);
            }
        });
    }

//获取城市Adcode
    private static void getAdcodeByCity(String city, AdcodeCallback callback) {
        HttpUrl url = HttpUrl.parse(ADCODE_URL)
                .newBuilder()
                .addQueryParameter("keywords", city)
                .addQueryParameter("limit", "1")
                .build();
//用城市名请求天气api接口获取城市adcode
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WeatherUtil", "获取adcode失败", e);
                callback.onFailure("获取城市编码失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("获取城市编码失败：响应码" + response.code());
                    return;
                }

                String json = response.body().string();
                Log.d("WeatherUtil", "行政区划接口返回：" + json);

                try {

                    JsonObject root = JsonParser.parseString(json).getAsJsonObject();
                    JsonArray results = root.getAsJsonArray("results");
                    if (results == null || results.size() == 0) {
                        callback.onFailure("未查询到" + city + "的行政区划信息");
                        return;
                    }

                    JsonObject resultObj = results.get(0).getAsJsonObject();
                    String adcode = resultObj.get("adcode").getAsString();
                    callback.onSuccess(adcode);
                } catch (Exception e) {
                    Log.e("WeatherUtil", "解析adcode失败", e);
                    callback.onFailure("解析城市编码失败：" + e.getMessage());
                }
            }
        });
    }

//根据Adcode获取天气
    private static void getWeatherByAdcode(String adcode, WeatherCallback callback) {
        HttpUrl url = HttpUrl.parse(WEATHER_URL)
                .newBuilder()
                .addQueryParameter("adcode", adcode)
                .addQueryParameter("forecast", "true")
                .addQueryParameter("lang", "zh")
                .build();
    //用城市adcode请求天气api接口获取天气数据
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("WeatherUtil", "请求天气失败", e);
                callback.onFailure("请求天气失败：" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("请求天气失败：响应码" + response.code());
                    return;
                }

                String json = response.body().string();
                Log.d("WeatherUtil", "天气接口返回：" + json);
                try {

                    WeatherBean weatherBean = gson.fromJson(json, WeatherBean.class);

                    if (weatherBean.getCity() == null && weatherBean.getProvince() == null) {
                        callback.onFailure("未查询到adcode=" + adcode + "的天气数据");
                        return;
                    }
                    callback.onSuccess(weatherBean);
                } catch (Exception e) {
                    Log.e("WeatherUtil", "解析天气数据失败", e);
                    callback.onFailure("解析天气数据失败：" + e.getMessage());
                }
            }
        });
    }



}