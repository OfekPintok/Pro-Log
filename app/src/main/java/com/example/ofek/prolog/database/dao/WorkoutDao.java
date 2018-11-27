/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/17/18 11:04 PM
 */

package com.example.ofek.prolog.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.ofek.prolog.database.entity.WorkoutGroup;

import java.util.List;

@Dao
public interface WorkoutDao {

    @Insert
    void insertWorkout(WorkoutGroup workout);

    @Delete
    void delete(WorkoutGroup workout);

    @Update
    void update(WorkoutGroup workout);

    @Query("DELETE FROM Workouts_table")
    void reset();

    @Query("Select * from Workouts_table ORDER BY Position ASC")
    LiveData<List<WorkoutGroup>> getAllWorkouts();

    @Query("SELECT * FROM Workouts_table")
    List<WorkoutGroup> getList();
}
