package com.example.sunnyweather.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
//ai
public class JsonUtil {
    private static final Gson gson = new Gson();
    public static String listToJson(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return gson.toJson(list);
    }

    public static List<String> jsonToList(String json) {
        if (json == null || json.isEmpty()) return null;
        return gson.fromJson(json, new TypeToken<List<String>>() {}.getType());
    }
}