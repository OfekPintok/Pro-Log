/*
 * Created by Ofek Pintok on 11/27/18 12:35 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/19/18 2:21 PM
 */

package com.example.ofek.prolog.adapters;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ofek.prolog.fragments.CustomExerciseDialogFragment;
import com.example.ofek.prolog.database.entity.ExerciseItem;
import com.example.ofek.prolog.fragments.NewExerciseDialogFragment;
import com.example.ofek.prolog.R;
import com.example.ofek.prolog.database.entity.WorklineItem;
import com.example.ofek.prolog.database.entity.WorkoutGroup;

import java.util.List;

import static com.example.ofek.prolog.utils.Utils.*;


/**
 * This is the adapter of the Expandable ListView component.
 *
 */
public class WorkoutAdapter extends BaseExpandableListAdapter {

    private List<WorkoutGroup> mWorkoutList;
    private Context mContext;

    public WorkoutAdapter(Context Context, List<WorkoutGroup> WorkoutList) {
        this.mContext = Context;
        this.mWorkoutList = WorkoutList;
    }

    //********************************
    // Group Methods
    //********************************
    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        WorkoutGroup workoutGroup = (WorkoutGroup) getGroup(groupPosition);

        //Set Bundle and pass the group position
        final Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_GROUP_POSITION, groupPosition);

        if (view == null) {
            LayoutInflater inf = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inf.inflate(R.layout.item_new_workout, null);
        }

        TextView workoutPositionHeader = (TextView) view.findViewById(R.id.text_iw_position);
        int workoutPositionNum = Integer.valueOf(workoutGroup.getGroupPosition());
        String workoutPosition = "#" + workoutPositionNum;
        workoutPositionHeader.setText(workoutPosition);

        TextView dateHeader = (TextView) view.findViewById(R.id.text_iw_date);
        dateHeader.setText(workoutGroup.getDate().trim());

        ImageButton newExercise = (ImageButton) view.findViewById(R.id.button_iw_add);
        newExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                //create a dialog that ask the details of the exercise
                NewExerciseDialogFragment newExerciseDialog = new NewExerciseDialogFragment();
                newExerciseDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomNewDialogFragment);
                newExerciseDialog.setArguments(bundle);
                newExerciseDialog.show(fragmentManager, "showExerciseDialog");
            }
        });

        return view;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public int getGroupCount() {
        return mWorkoutList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mWorkoutList.get(groupPosition);
    }

    //********************************
    // Child Methods
    //********************************
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        ExerciseItem exerciseItem = (ExerciseItem) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater)
                    mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = infalInflater.inflate(R.layout.item_new_exercise, null);
        }

        List<WorklineItem> worklineItemsList = exerciseItem.getWorklineItemsList();

        //Set new name
        TextView exrow_name = view.findViewById(R.id.text_ie_name);
        exrow_name.setText(exerciseItem.getExerciseName());

        TextView exrow_load = view.findViewById(R.id.text_ie_load);
        Button custom_button = view.findViewById(R.id.button_exrow_custom);

        if (worklineItemsList.size() > 1) {

            exrow_load.setVisibility(View.INVISIBLE);
            custom_button.setVisibility(View.VISIBLE);

            final Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_GROUP_POSITION, groupPosition);
            bundle.putInt(BUNDLE_CHILD_POSITION, childPosition);

            custom_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomExerciseDialogFragment listOfExercise = new CustomExerciseDialogFragment();
                    listOfExercise.setWorkoutList(mWorkoutList);
                    listOfExercise.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomNewDialogFragment);

                    //Set FragmentManager
                    FragmentManager fragmentManager =
                            ((AppCompatActivity) mContext).getSupportFragmentManager();


                    listOfExercise.setArguments(bundle);

                    listOfExercise.show(fragmentManager, "showlistOfExercise");
                }
            });
        } else {

            exrow_load.setVisibility(View.VISIBLE);
            custom_button.setVisibility(View.INVISIBLE);

            // Show A single workline item only
            WorklineItem worklineItem = worklineItemsList.get(FIRST_ITEM);

            //Set new load String
            String load = worklineItem.getExerciseWeight().toString() + sSelectedUnits + " for " +
                    String.valueOf(worklineItem.getSets()) + "x"
                    + String.valueOf(worklineItem.getExerciseReps());
            exrow_load.setText(load);
        }

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<ExerciseItem> exerciseList =
                mWorkoutList.get(groupPosition).getExerciseList();
        return exerciseList.size();
    }


    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ExerciseItem> exerciseList =
                mWorkoutList.get(groupPosition).getExerciseList();
        return exerciseList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public void setWorkoutList(List<WorkoutGroup> workoutList) {
        this.mWorkoutList = workoutList;
        notifyDataSetChanged();
    }

}