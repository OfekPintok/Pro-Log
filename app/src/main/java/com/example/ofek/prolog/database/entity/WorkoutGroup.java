/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/19/18 2:25 PM
 */

package com.example.ofek.prolog.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.ofek.prolog.database.converters.ExerciseConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Every workout has a group (or list) of {@link ExerciseItem} done on the same day.
 * it also has a date and a serial number a.k.a GroupPosition.
 *
 */

@Entity(tableName = "Workouts_table")
//@TypeConverters({WorklineConverter.class})
public class WorkoutGroup {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "Position")
    private int mGroupPosition;

    @NonNull
    @ColumnInfo(name = "Date")
    private String mDate;

    @TypeConverters(ExerciseConverter.class)
    private List<ExerciseItem> mExerciseList = new ArrayList<>();

    public WorkoutGroup(@NonNull int groupPosition, @NonNull String date){
        this.mGroupPosition = groupPosition;
        this.mDate = date;
    }

    public int getGroupPosition() {
        return mGroupPosition;
    }

    public String getDate() {
        return mDate;
    }

    public List<ExerciseItem> getExerciseList() {
        return mExerciseList;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void setExerciseList(List<ExerciseItem> exerciseList) {
        this.mExerciseList = exerciseList;
    }
}

