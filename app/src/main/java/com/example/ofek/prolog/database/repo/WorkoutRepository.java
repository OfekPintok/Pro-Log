/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/22/18 10:15 AM
 */

package com.example.ofek.prolog.database.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.os.AsyncTask;

import com.example.ofek.prolog.database.WorkoutRoomDatabase;
import com.example.ofek.prolog.database.dao.WorkoutDao;
import com.example.ofek.prolog.database.entity.WorkoutGroup;

import java.util.List;

public class WorkoutRepository {
    private WorkoutDao mWorkoutDao;
    private LiveData<List<WorkoutGroup>> mAllWorkouts;

    public WorkoutRepository(Application application) {
        WorkoutRoomDatabase db = WorkoutRoomDatabase.getDatabase(application);
        mWorkoutDao = db.workoutDao();
        mAllWorkouts = mWorkoutDao.getAllWorkouts();
    }

    // Get LiveData object of the list
    public LiveData<List<WorkoutGroup>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public void insert (WorkoutGroup workout) {
        new insertAsyncTask(mWorkoutDao).execute(workout);
    }

    public void update (WorkoutGroup workout) {
        new updateAsyncTask(mWorkoutDao).execute(workout);
    }

    public void delete (WorkoutGroup workout) {
        new deleteAsyncTask(mWorkoutDao).execute(workout);
    }

    // Delete all the workouts
    public void reset()  {
        new resetAsyncTask(mWorkoutDao).execute();
    }

    // Insert a workout
    private static class insertAsyncTask extends AsyncTask<WorkoutGroup, Void, Void> {

        private WorkoutDao mAsyncTaskDao;

        insertAsyncTask(WorkoutDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WorkoutGroup... params) {
            mAsyncTaskDao.insertWorkout(params[0]);
            return null;
        }
    }

    // Delete a workout
    private static class deleteAsyncTask extends AsyncTask<WorkoutGroup, Void, Void> {

        private WorkoutDao mAsyncTaskDao;

        deleteAsyncTask(WorkoutDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WorkoutGroup... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }

    // Update a workout
    private static class updateAsyncTask extends AsyncTask<WorkoutGroup, Void, Void> {

        private WorkoutDao mAsyncTaskDao;

        updateAsyncTask(WorkoutDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WorkoutGroup... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class resetAsyncTask extends AsyncTask<Void, Void, Void> {

        private WorkoutDao mAsyncTaskDao;

        resetAsyncTask(WorkoutDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.reset();
            return null;
        }
    }
}
