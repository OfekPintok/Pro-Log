/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/21/18 12:19 AM
 */

package com.example.ofek.prolog.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.ofek.prolog.database.converters.WorklineConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Every exercise has a name and at least one {@link WorklineItem}.
 *
 */

public class ExerciseItem {

    @NonNull
    @ColumnInfo(name = "Name")
    private String mExerciseName;

    @TypeConverters(WorklineConverter.class)
    private List<WorklineItem> mWorklineItemsList = new ArrayList<>();

    public ExerciseItem(@NonNull String exerciseName){
        this.mExerciseName = exerciseName;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public List<WorklineItem> getWorklineItemsList() {
        return mWorklineItemsList;
    }

    public void setWorklineItemsList(List<WorklineItem> WorklineItemsList) {
        this.mWorklineItemsList = WorklineItemsList;
    }

}
