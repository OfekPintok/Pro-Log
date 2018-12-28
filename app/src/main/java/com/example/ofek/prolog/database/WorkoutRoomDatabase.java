/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/21/18 12:23 AM
 */

package com.example.ofek.prolog.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.ofek.prolog.database.dao.WorkoutDao;
import com.example.ofek.prolog.database.entity.WorkoutGroup;

@Database(entities = {WorkoutGroup.class}, version = 4, exportSchema = false)
public abstract class WorkoutRoomDatabase extends RoomDatabase {

    public abstract WorkoutDao workoutDao();

    private static WorkoutRoomDatabase INSTANCE;

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };


    public static WorkoutRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WorkoutRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WorkoutRoomDatabase.class, "Workout_database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WorkoutDao mDao;

        PopulateDbAsync(WorkoutRoomDatabase db) {
            mDao = db.workoutDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            return null;
        }
    }

}

