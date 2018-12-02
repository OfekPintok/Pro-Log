/*
 * Created by Ofek Pintok on 11/27/18 12:33 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/27/18 12:30 PM
 */

package com.example.ofek.prolog.fragments;


import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofek.prolog.adapters.WorkoutAdapter;
import com.example.ofek.prolog.database.viewmodel.WorkoutViewModel;
import com.example.ofek.prolog.database.entity.ExerciseItem;
import com.example.ofek.prolog.activities.HomeScreenActivity;
import com.example.ofek.prolog.R;
import com.example.ofek.prolog.activities.SettingsActivity;
import com.example.ofek.prolog.database.entity.WorklineItem;
import com.example.ofek.prolog.database.entity.WorkoutGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.ofek.prolog.utils.Utils.*;


/**
 * This dialog allows the user to insert his data of an exercise, and link it to a
 * specific {@link WorkoutGroup} he created before.
 *
 */
public class NewExerciseDialogFragment extends DialogFragment implements
        AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static ArrayList<String> sDefaultExerciseList;
    private ArrayAdapter<String> mDefaultExerciseListAdapter;
    private List<WorkoutGroup> mWorkoutList;
    private WorkoutAdapter mListAdapter;
    private WorkoutViewModel mWorkoutViewModel;
    private Spinner mSpinner;
    private SharedPreferences mPref;


    private TextInputLayout mWeightLayout;
    private TextInputLayout mRepsLayout;
    private TextInputLayout mSetsLayout;
    private EditText mWeightEditText;
    private EditText mRepsEditText;
    private EditText mSetsEditText;

    private boolean mEditMode;
    private int mGroupPosition;
    private int mChildPosition;


    public NewExerciseDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_new_exercise,
                container, false);

        // Build the spinner that shows the exercises names
        mSpinner = (Spinner) view.findViewById(R.id.sp_exercise);
        mSpinner.setOnItemSelectedListener(this);
        sDefaultExerciseList = createExercisesList();
        mDefaultExerciseListAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, sDefaultExerciseList);
        mSpinner.setAdapter(mDefaultExerciseListAdapter);


        // Get the expandable list view adapter from the main activity
        mListAdapter = ((HomeScreenActivity) getActivity()).getListAdapter();

        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        mWorkoutViewModel.getAllWorkouts().observe(this, new Observer<List<WorkoutGroup>>() {
            @Override
            public void onChanged(@Nullable final List<WorkoutGroup> workouts) {
                mWorkoutList = workouts;
            }
        });

        // Set the title for this fragment
        getDialog().setTitle(R.string.new_exercise_title);

        // Receive data from other activities
        String exerciseName = "";
        double weight = 0.0;
        int reps = 0;
        int sets = 0;
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mEditMode = bundle.getBoolean(BUNDLE_EDIT_MODE);
            mGroupPosition = bundle.getInt(BUNDLE_GROUP_POSITION);
            mChildPosition = bundle.getInt(BUNDLE_CHILD_POSITION);
            exerciseName = bundle.getString(BUNDLE_EXERCISE_NAME);
            weight = bundle.getDouble(BUNDLE_EXERCISE_WEIGHT);
            reps = bundle.getInt(BUNDLE_EXERCISE_REPS);
            sets = bundle.getInt(BUNDLE_EXERCISE_SETS);
        }

        mWeightLayout = (TextInputLayout) view.findViewById(R.id.text_workout_date);
        mWeightLayout.setHint(getString(R.string.Weight));
        mRepsLayout = (TextInputLayout) view.findViewById(R.id.layout_exercise_repetitions);
        mRepsLayout.setHint(getString(R.string.Reps));
        mSetsLayout = (TextInputLayout) view.findViewById(R.id.layout_exercise_sets);
        mSetsLayout.setHint(getString(R.string.Sets));

        Button confirm = (Button) view.findViewById(R.id.button_exercise_confirm);
        confirm.setOnClickListener(this);
        Button cancel = (Button) view.findViewById(R.id.button_exercise_cancel);
        cancel.setOnClickListener(this);
        TextView units = (TextView) view.findViewById(R.id.text_exercise_units);
        ImageButton removeExerciseName = (ImageButton) view.findViewById(R.id.ibutton_exercise_delete);
        removeExerciseName.setOnClickListener(this);

        units.setText(String.format("%s" + " " + "units", sSelectedUnits));
        units.setOnClickListener(this);

        mWeightEditText = (EditText) view.findViewById(R.id.edit_exercise_weight);
        mRepsEditText = (EditText) view.findViewById(R.id.edit_exercise_repetitions);
        mSetsEditText = (EditText) view.findViewById(R.id.edit_exercise_sets);

        // Initialize the data of a dialog in edit mode
        if (reps != 0 && sets != 0) {
            setName(mSpinner, exerciseName);
            mWeightEditText.setText(String.valueOf(weight));
            mRepsEditText.setText(String.valueOf(reps));
            mSetsEditText.setText(String.valueOf(sets));
        }

        return view;
    }

    /**
     * The selection of the custom option in the exercise's name mSpinner will allow
     * the user to set his custom new exercise name.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String exercise;
        exercise = String.valueOf(parent.getItemAtPosition(position));

        // User selected the Custom exercise
        if (exercise.equals(getString(R.string.Custom))) {
            // Set custom EditText for the name
            final EditText newName = new EditText(getActivity());
            newName.setInputType(InputType.TYPE_CLASS_TEXT);

            // Set a dialog with name request
            final AlertDialog builder = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.exercise_title)
                    .setView(newName)
                    .setPositiveButton(R.string.finish, null)
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();

            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialog) {

                    Button finish = builder.getButton(DialogInterface.BUTTON_POSITIVE);
                    finish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String getName = newName.getText().toString();
                            // Check if the name is proper and doesn't already exists.
                            if (getName.isEmpty() || !isAlpha(getName)) {
                                Toast.makeText(getActivity(), R.string.error_proper_name,
                                        Toast.LENGTH_SHORT).show();
                            } else if (!checkIfExists(getName)) {
                                Toast.makeText(getActivity(), R.string.error_already_exists,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                mDefaultExerciseListAdapter.remove(getString(R.string.Custom));
                                sDefaultExerciseList.add(getName);
                                mDefaultExerciseListAdapter.add(getString(R.string.Custom));
                                saveChangesOnDefaultList(sDefaultExerciseList);
                                mDefaultExerciseListAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }
                    });
                    Button cancel = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                }
            });
            builder.show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Do nothing
    }

    /**
     * onClick handle 2 main cases - clicking on confirm and cancel buttons.
     * cancel close the dialog, while confirm is more complex
     * it has 2 cases: edit mode and the regular case
     *
     * in the regular case, there is an insertion of a new exercise to the workout,
     * or insertion of a new workline to the exercise if it is already exists.
     *
     * edit mode does exactly the same, but also remove the exercise that was selected.
     * NOTE: Edit mode handles an exercise with a single workline only.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case (R.id.button_exercise_confirm):

                WorklineItem worklineItem = new WorklineItem();
                List<WorklineItem> worklineItemsList;

                mSpinner = (Spinner) getView().findViewById(R.id.sp_exercise);

                // Get the input data
                String name = mSpinner.getSelectedItem().toString(),
                        weight = mWeightEditText.getText().toString(),
                        reps = mRepsEditText.getText().toString(),
                        sets = mSetsEditText.getText().toString();

                // If one of the fields is empty, display error
               boolean isEmpty = checkInputData(weight, reps, sets);

                if (!isEmpty){
                    worklineItem.setExerciseWeight(Double.parseDouble(weight));
                    worklineItem.setExerciseReps(Integer.parseInt(reps));
                    worklineItem.setSets(Integer.parseInt(sets));

                    // Get exercise list of a specific workout
                    List<ExerciseItem> exerciseList =
                            mWorkoutList.get(mGroupPosition).getExerciseList();

                    // Get the WorkoutGroup object of the current index
                    WorkoutGroup workoutGroup = mWorkoutList.get(mGroupPosition);

                    // Check if the exercise is already belong to the exercise list
                    ExerciseItem exercise = getExistExercise(exerciseList, name);

                    // Exercise isn't exist in the list
                    if (exercise == null) {
                        // Create a new exercise
                        exercise = new ExerciseItem(name);
                        worklineItemsList = new ArrayList<>();
                        worklineItemsList.add(worklineItem);
                        exercise.setWorklineItemsList(worklineItemsList);
                        if (mEditMode) {
                            // switch the old with a new exercise
                            exerciseList.set(mChildPosition, exercise);
                            updateExercise(workoutGroup, exercise);
                        } else {
                            // Just insert it to the exercises list
                            ((HomeScreenActivity) getActivity())
                                    .addItemToExerciseList(mGroupPosition, exercise);
                        }
                    } else {
                        // The exercise is already exists in the list
                        if (mEditMode) {
                            // In case the user want to save the same exercise name
                            if (exercise.getExerciseName().equals(name)) {
                                // Remove the first workline only
                                exercise.getWorklineItemsList().remove(FIRST_ITEM);
                            } else {
                                // Not the same exercise; remove the whole object
                                exerciseList.remove(mChildPosition);
                            }
                        }
                        // Insert the new workline
                        worklineItemsList = exercise.getWorklineItemsList();
                        worklineItemsList.add(worklineItem);
                        exercise.setWorklineItemsList(worklineItemsList);
                        // Update the current child
                        mChildPosition = workoutGroup.getExerciseList().indexOf(exercise);
                        // Update the group
                        updateExercise(workoutGroup, exercise);
                    }
                    dismiss();
                }
                break;

            case (R.id.button_exercise_cancel):
                this.dismiss();
                break;

            // Pressing on the units TextView will open the settings activity
            case (R.id.text_exercise_units):
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;

                // Remove selected item
            case (R.id.ibutton_exercise_delete):
                // Get the currently selected item
                String exerciseName = mSpinner.getSelectedItem().toString();
                // Allow to remove any exercise but Custom to avoid zero exercises
                if(exerciseName.equals(getString(R.string.Custom))){
                    Toast.makeText(getActivity(), R.string.exercise_delete_error, Toast.LENGTH_SHORT).show();
                } else {
                    mDefaultExerciseListAdapter.remove(exerciseName);
                    saveChangesOnDefaultList(sDefaultExerciseList);
                }
                break;

            default:
                    break;
        }
    }

    /** Search for the index of the selected name from the spinner  */
    public void setName(Spinner spinner, String name) {
        int count = spinner.getAdapter().getCount();
        for (int i = 0; i < count; i++) {
            if (spinner.getAdapter().getItem(i).toString().contains(name)) {
                spinner.setSelection(i);
            }
        }
    }

    /**
     * Set a list of exercises (names)
     * If there is a saved list in the system, use it, else
     * use the default one that stored in the resources
     * @return that list
     */
    private ArrayList<String> createExercisesList() {
        ArrayList<String> arrayExercisesList;
        Gson gson = new Gson();

        mPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        // Get the list from the preference file
        String exercisesJson = mPref.getString(PREF_KEY_EXERCISES_SET, null);

        if(exercisesJson == null) {
            arrayExercisesList =
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.Exercises)));
        } else {
            String[] exercisesArray = gson.fromJson(exercisesJson, String[].class);
            arrayExercisesList = new ArrayList<>(Arrays.asList(exercisesArray));
        }
        return arrayExercisesList ;
    }

    /** Check if the name is already exist in the list to avoid duplication  */
    private boolean checkIfExists(String name) {
        int count = mDefaultExerciseListAdapter.getCount();

        for (int i = 0; i < count; i++) {
            if (name.compareToIgnoreCase(mDefaultExerciseListAdapter.getItem(i)) == 0) {
                return false;
            }
            // Ignore the spaces and compare strings
            if (name.replace(" ", "").equalsIgnoreCase
                    (mDefaultExerciseListAdapter.getItem(i).replace(" ", ""))) {
                return false;
            }
        }
        return true;
    }

    /** Anything but letters is not allowed!  */
    private boolean isAlpha(String name) {
        char[] chars = name.toCharArray();
        // Check if the name begin with space
        if (chars[0] == ' ') {
            return false;
        }
        // The name can contain letters and spaces only
        for (char c : chars) {
            if (!((c >= 'a' && c <= 'z') | (c >= 'A' && c <= 'Z') | (c == ' '))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Look for the exercise in the workout's list
     * @return the exercise if exist in the list, null if not.
     */
    private ExerciseItem getExistExercise(List<ExerciseItem> exerciseItemsList, String name) {

        ExerciseItem exerciseItem;
        String exerciseName;
        int size = mWorkoutList.get(mGroupPosition).getExerciseList().size();

        for (int i = 0; i < size; i++) {
            exerciseItem = exerciseItemsList.get(i);
            exerciseName = exerciseItem.getExerciseName();

            if (name.equals(exerciseName)) {
                return exerciseItem;
            }
        }
        return null;
    }

    /** Avoid empty input fields */
    private boolean checkInputData(String weight, String reps, String sets) {

        if (weight.isEmpty() || reps.isEmpty() || sets.isEmpty()) {

            if (reps.isEmpty()) {
                mRepsLayout.setError(getString(R.string.error_blank_field));
            } else {
                mRepsLayout.setError(null);
            }
            if (weight.isEmpty()) {
                mWeightLayout.setError(getString(R.string.error_blank_field));
            } else {
                mWeightLayout.setError(null);
            }
            if (sets.isEmpty()) {
                mSetsLayout.setError(getString(R.string.error_blank_field));
            } else {
                mSetsLayout.setError(null);
            }
            return true;
        }
        return false;
    }

    /**
     * update the default list of exercises.
     *
     * using JSON to serialize the ArrayList into a string
     * and store it inside the Shared Preferences file
     * @param listToSave is the updated list as an ArrayList type
     */
    private void saveChangesOnDefaultList (ArrayList<String> listToSave) {
        Gson gson = new Gson();
        SharedPreferences.Editor editor = mPref.edit();
        String exercisesJson = gson.toJson(listToSave);
        editor.putString(PREF_KEY_EXERCISES_SET, exercisesJson);
        editor.apply();
    }

    /**
     * Set the updated exercise object inside the workout and overwrite
     * the workout object inside the database
     */
    private void updateExercise (WorkoutGroup workoutGroup, ExerciseItem exercise) {
        workoutGroup.getExerciseList().set(mChildPosition, exercise);
        mWorkoutViewModel.update(workoutGroup);
        mListAdapter.notifyDataSetChanged();
    }
}
