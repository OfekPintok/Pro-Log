/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/19/18 2:21 PM
 */

package com.example.ofek.prolog.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.ofek.prolog.database.entity.ExerciseItem;
import com.example.ofek.prolog.R;
import com.example.ofek.prolog.database.entity.WorklineItem;
import com.example.ofek.prolog.database.entity.WorkoutGroup;
import com.example.ofek.prolog.adapters.CustomExerciseAdapter;

import java.util.List;

import static com.example.ofek.prolog.utils.Utils.BUNDLE_CHILD_POSITION;
import static com.example.ofek.prolog.utils.Utils.BUNDLE_GROUP_POSITION;


/**
 * This dialog can be shown after the user click on a custom button (of a selected exercise).
 * the dialog contain a list of {@link WorklineItem}, showed in a ListView.
 *
 */
public class CustomExerciseDialogFragment extends DialogFragment {

    private List<WorkoutGroup> mWorkoutList;

    private ExerciseItem mExerciseItem;
    private int mGroupPosition;
    private int mChildPosition;

    public CustomExerciseDialogFragment() {
        // Required empty public constructor
    }

    public void setWorkoutList(List<WorkoutGroup> workoutList) {
        this.mWorkoutList = workoutList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =
                inflater.inflate(R.layout.dialog_custom_exercise, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mChildPosition = bundle.getInt(BUNDLE_CHILD_POSITION);
            mGroupPosition = bundle.getInt(BUNDLE_GROUP_POSITION);
        }

        mExerciseItem = mWorkoutList.get(mGroupPosition).getExerciseList().get(mChildPosition);
        List<WorklineItem> worklineItemsList = mExerciseItem.getWorklineItemsList();

        getDialog().setTitle(getString(R.string.Custom_exercise_title) + mExerciseItem.getExerciseName());

        CustomExerciseAdapter adapter =
                new CustomExerciseAdapter(getActivity(), worklineItemsList);

        ListView listView = (ListView) view.findViewById(R.id.listview_custom);
        listView.setAdapter(adapter);

        Button closeButton = (Button) view.findViewById(R.id.button_custom);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

}
