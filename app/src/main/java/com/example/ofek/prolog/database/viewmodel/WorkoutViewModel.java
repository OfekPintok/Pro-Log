package com.example.ofek.prolog.database.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.ofek.prolog.database.entity.WorkoutGroup;
import com.example.ofek.prolog.database.repo.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {

    private WorkoutRepository mRepository;
    private LiveData<List<WorkoutGroup>> mAllWorkouts;

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WorkoutRepository(application);
        mAllWorkouts = mRepository.getAllWorkouts();
    }

    public LiveData<List<WorkoutGroup>> getAllWorkouts() {
        return mAllWorkouts;
    }

    public void insert (WorkoutGroup workout) {
        mRepository.insert(workout);
    }

    public void delete (WorkoutGroup workout) {
        mRepository.delete(workout);
    }

    public void update (WorkoutGroup workout) {
        mRepository.update(workout);
    }

    public void reset () {
        mRepository.reset();
    }


}
