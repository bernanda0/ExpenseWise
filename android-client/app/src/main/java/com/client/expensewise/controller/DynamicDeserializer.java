package com.client.expensewise.controller;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class DynamicDeserializer<T> implements JsonDeserializer<ArrayList<T>> {

    private Class<T> targetType;

    public DynamicDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    @Override
    public ArrayList<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ArrayList<T> resultList = new ArrayList<>();

        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                T item = context.deserialize(element, targetType);
                resultList.add(item);
            }
        } else if (json.isJsonObject()) {
            T item = context.deserialize(json, targetType);
            resultList.add(item);
        }

        return resultList;
    }
}
