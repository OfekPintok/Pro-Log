/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/21/18 12:19 AM
 */

package com.example.ofek.prolog.database.entity;

/**
 * Workline is defined as a group of attributes.
 * each workline contains weight, reps and sets
 * i.e, 60kg for 3 sets of 2 reps.
 *
 */

public class WorklineItem {

    private Double mExerciseWeight = 0.0;

    private int mExerciseReps = 0;

    private int mSets = 0;

    public WorklineItem() {
    }

    public Double getExerciseWeight() {
        return mExerciseWeight;
    }

    public void setExerciseWeight(Double ExerciseWeight) {
        this.mExerciseWeight = ExerciseWeight;
    }

    public int getExerciseReps() {
        return mExerciseReps;
    }

    public void setExerciseReps(int ExerciseReps) {
        this.mExerciseReps = ExerciseReps;
    }

    public int getSets() {
        return mSets;
    }

    public void setSets(int sets) {
        this.mSets = sets;
    }
}
