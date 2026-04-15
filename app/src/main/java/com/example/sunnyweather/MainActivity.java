package com.example.sunnyweather;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunnyweather.util.BingImageUtil;
import com.example.sunnyweather.util.SPUtil;
import com.example.sunnyweather.util.WeatherUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    private TextView tvProvince;
    private TextView tvCity;
    private TextView tvWeather;
    private TextView tvTemperature;
    private TextView tvWindDirection;
    private TextView tvWindPower;
    private TextView tvHumidity;
    private TextView tvReportTime;
    private TextView tvTempMaxMin;
    private EditText etCity;
    private TextView tvDistrict;
    private ImageView ivBingBg;
    private RecyclerView rvForecast;
    private ForecastAdapter forecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        tvProvince = findViewById(R.id.tv_province);
        tvCity = findViewById(R.id.tv_city);
        tvWeather = findViewById(R.id.tv_weather);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvWindDirection = findViewById(R.id.tv_wind_direction);
        tvWindPower = findViewById(R.id.tv_wind_power);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvReportTime = findViewById(R.id.tv_report_time);
        tvTempMaxMin = findViewById(R.id.tv_temp_max_min);
        etCity = findViewById(R.id.et_city);
        rvForecast = findViewById(R.id.rv_forecast);
        tvDistrict = findViewById(R.id.tv_district);
        rvForecast.setLayoutManager(new LinearLayoutManager(this));
        forecastAdapter = new ForecastAdapter(this, null);
        rvForecast.setAdapter(forecastAdapter);
        ivBingBg = findViewById(R.id.iv_bing_bg);


        BingImageUtil.loadBingDailyImage(MainActivity.this, ivBingBg);
        WeatherBean cacheWeather = SPUtil.getWeatherData(this);
        if (cacheWeather != null) {
            fillWeatherData(cacheWeather);
            fillForecastData(cacheWeather.getForecast());
            Toast.makeText(this, "已加载离线天气数据", Toast.LENGTH_SHORT).show();
        }
    }


    public void getWeather(View view) {
        String city = etCity.getText().toString().trim();
        if (city.isEmpty()) {
            Toast.makeText(this, "请输入城市名！", Toast.LENGTH_SHORT).show();
            return;
        }



        WeatherUtil.getWeather(city, new WeatherUtil.WeatherCallback() {
            @Override
            public void onSuccess(WeatherBean weatherBean) {

                runOnUiThread(() -> {
                    fillWeatherData(weatherBean);
                    fillForecastData(weatherBean.getForecast());
                    SPUtil.saveWeatherData(MainActivity.this, weatherBean);
                    Log.d("MainActivity", weatherBean.toString());
                });
            }

            @Override
            public void onFailure(String errorMsg) {

                runOnUiThread(() -> {

                    WeatherBean cacheWeather = SPUtil.getWeatherData(MainActivity.this);
                    if (cacheWeather != null) {
                        fillWeatherData(cacheWeather);
                        fillForecastData(cacheWeather.getForecast());
                    } else {

                        clearWeatherViews();
                        forecastAdapter.updateData(null);
                        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void fillWeatherData(WeatherBean weatherBean) {
        String province = weatherBean.getProvince() == null ? "未知" : weatherBean.getProvince();
        String city = weatherBean.getCity() == null ? "未知" : weatherBean.getCity();
        String district = weatherBean.getDistrict() == null ? city : weatherBean.getDistrict();
        String weather = weatherBean.getWeather() == null ? "未知" : weatherBean.getWeather();
        Double temperature = weatherBean.getTemperature();
        String temperatureStr = temperature == null ? "0.0" : String.format("%.1f", temperature);
        String windDirection = weatherBean.getWindDirection() == null ? "未知" : weatherBean.getWindDirection();
        String windPower = weatherBean.getWindPower() == null ? "未知" : weatherBean.getWindPower();
        int humidity = weatherBean.getHumidity();
        String reportTime = weatherBean.getReportTime() == null ? "未知" : weatherBean.getReportTime();
        int tempMax = weatherBean.getTempMax();
        int tempMin = weatherBean.getTempMin();
        String tempMaxMinStr = String.format("%d℃ / %d℃", tempMax, tempMin);

        tvProvince.setText("省份：" + province);
        tvCity.setText("城市：" + city);
        tvDistrict.setText(district);
        tvWeather.setText("天气：" + weather);
        tvTemperature.setText("温度：" + temperatureStr + "℃");
        tvWindDirection.setText("风向：" + windDirection);
        tvWindPower.setText("风力：" + windPower);
        tvHumidity.setText("湿度：" + humidity + "%");
        tvTempMaxMin.setText("当日最高/最低温：" + tempMaxMinStr);
        tvReportTime.setText("更新时间：" + reportTime);
    }
    private void fillForecastData(List<ForecastBean> forecastList) {
      if(forecastList==null||forecastList.isEmpty()){
          Toast.makeText(this, "暂无七天预报数据", Toast.LENGTH_SHORT).show();
          forecastAdapter.updateData(null);
          return;
      }
      forecastAdapter.updateData(forecastList);
    }

    private void clearWeatherViews() {
        tvProvince.setText("省份：");
        tvCity.setText("城市：");
        tvWeather.setText("天气：");
        tvTemperature.setText("温度：");
        tvWindDirection.setText("风向：");
        tvWindPower.setText("风力：");
        tvHumidity.setText("湿度：");
        tvDistrict.setText("");
        tvTempMaxMin.setText("当日最高/最低温：");
        tvReportTime.setText("更新时间：");
        SPUtil.clearImageCache(this);
    }
}