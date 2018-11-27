/*
 * Created by Ofek Pintok on 11/27/18 12:35 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/19/18 2:25 PM
 */

package com.example.ofek.prolog.database.converters;

import android.arch.persistence.room.TypeConverter;

import com.example.ofek.prolog.database.entity.ExerciseItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class ExerciseConverter {



    @TypeConverter
    public static List<ExerciseItem> stringToWorkline(String data) {
        Gson gson = new Gson();

        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ExerciseItem>>() {}.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String worklineToString(List<ExerciseItem> someObjects) {
        Gson gson = new Gson();
        return gson.toJson(someObjects);
    }
}