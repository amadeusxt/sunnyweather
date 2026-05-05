package com.example.sunnyweather.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BingImageUtil {

    private static final String BING_OFFICIAL_URL = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&mkt=zh-CN";
    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    public static void loadBingDailyImage(Context context, ImageView imageView) {
        //将储存在SharedPreferences中的图片取出
        String cacheImageUrl = SPUtil.getBingImageUrl(context);
        if (cacheImageUrl != null) {
            Log.d("BingImageUtil", "缓存照片加载" + cacheImageUrl);
            loadImageWithGlide(context, imageView, cacheImageUrl);
        }

        //请求必应官网接口获取图片
        Request request = new Request.Builder().url(BING_OFFICIAL_URL).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("BingImageUtil", "请求失败：", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    try {

                        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
                        String originalUrl = root.getAsJsonArray("images")
                                .get(0).getAsJsonObject().get("url").getAsString();
                        String mobileUrl = originalUrl.replace("1920x1080", "1080x1920");
                        String imgUrl = "https://cn.bing.com" + mobileUrl;
                        Log.d("BingImageUtil", "解析到的最新图片：" + imgUrl);

                        SPUtil.saveBingImageUrl(context, imgUrl);
                        if (cacheImageUrl == null || !cacheImageUrl.equals(imgUrl)) {
                            MAIN_HANDLER.post(() -> loadImageWithGlide(context, imageView, imgUrl));
                        }
                    } catch (Exception e) {
                        Log.e("BingImageUtil", "解析JSON失败：", e);
                    }
                } else {
                    Log.e("BingImageUtil", "接口响应失败，码值：" + response.code());
                }
            }
        });
    }

//glide加载图片
    private static void loadImageWithGlide(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .centerCrop()
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target target, boolean isFirstResource) {
                        Log.e("BingImageUtil", "Glide加载失败：", e);
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);
    }
}