package com.example.sunnyweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sunnyweather.util.JsonUtil;
import com.example.sunnyweather.util.SPUtil;
import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {
    private List<ForecastBean> mForecastList;
    private static final String RANDOM_IMAGE_API = "https://uapis.cn/api/v1/random/image?category=acg";
    private List<String> mCacheImgUrls;
    private final Context mContext;
    public ForecastAdapter(Context context, List<ForecastBean> forecastList) {
        mContext = context;
        mForecastList = forecastList;
        initCacheImgUrls();
    }
//初始化缓存的图片的URl
    private void initCacheImgUrls() {
        String cacheJson = SPUtil.getForecastRandomImgUrls(mContext);
        mCacheImgUrls = JsonUtil.jsonToList(cacheJson);
        if (mCacheImgUrls == null) {
            mCacheImgUrls = new ArrayList<>();
        }
        if (mForecastList != null && !mForecastList.isEmpty()) {
            while (mCacheImgUrls.size() < mForecastList.size()) {
                mCacheImgUrls.add(null);
            }
        }
    }
//更新数据
    public void updateData(List<ForecastBean> newList) {
        mForecastList = newList;
        initCacheImgUrls();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        if (mForecastList == null || position >= mForecastList.size()) {
            return;
        }
        ForecastBean forecast = mForecastList.get(position);
        holder.tvDate.setText(forecast.getDate());
        holder.tvWeek.setText(forecast.getWeek());
        holder.tvWeatherDayNight.setText("白天:"+forecast.getWeatherDay()+" / 夜间:"+forecast.getWeatherNight());
        holder.tvTempMaxMin.setText(forecast.getTempMax() + "℃ / " + forecast.getTempMin() + "℃");
        holder.tvWindInfo.setText(forecast.getWindDirDay() + forecast.getWindScaleDay());
        holder.tvSunriseSunset.setText("日出:"+forecast.getSunrise()+" 日落:"+forecast.getSunset());
        //加载随机图片(ai)
        String imgUrl;
        if (isNetworkAvailable(mContext)) {
            imgUrl = RANDOM_IMAGE_API + "&time=" + System.currentTimeMillis() + position;

            if (mCacheImgUrls.size() <= position) {
                mCacheImgUrls.add(imgUrl);
            } else {
                mCacheImgUrls.set(position, imgUrl);
            }
            SPUtil.saveForecastRandomImgUrls(mContext, JsonUtil.listToJson(mCacheImgUrls));
        } else {
            if (mCacheImgUrls != null && mCacheImgUrls.size() > position) {
                imgUrl = mCacheImgUrls.get(position);
            } else {
                imgUrl = null;
            }
        }
        if (imgUrl == null) {
            holder.ivRandom.setImageResource(R.drawable.image1);
        } else {
            Glide.with(holder.itemView.getContext())
                    .load(imgUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(holder.ivRandom);
        }
    }
//判断网络是否可用(ai)
    private boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        Network activeNetwork = cm.getActiveNetwork();
        if (activeNetwork == null) return false;
        NetworkCapabilities capabilities = cm.getNetworkCapabilities(activeNetwork);
        return capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        );
    }
    @Override
    public int getItemCount() {
        return mForecastList == null ? 0 : mForecastList.size();
    }
    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        TextView tvWeek;
        TextView tvWeatherDayNight;
        TextView tvTempMaxMin;
        TextView tvWindInfo;
        TextView tvSunriseSunset;
        ImageView ivRandom;
        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvWeek = itemView.findViewById(R.id.tv_week);
            tvWeatherDayNight = itemView.findViewById(R.id.tv_weather_day_night);
            tvTempMaxMin = itemView.findViewById(R.id.tv_temp_max_min);
            tvWindInfo = itemView.findViewById(R.id.tv_wind_info);
            tvSunriseSunset = itemView.findViewById(R.id.tv_sunrise_sunset);
            ivRandom = itemView.findViewById(R.id.iv_random);
        }
    }
}