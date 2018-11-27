/*
 * Created by Ofek Pintok on 11/27/18 12:33 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/24/18 11:32 PM
 */

package com.example.ofek.prolog.activities;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofek.prolog.database.viewmodel.WorkoutViewModel;
import com.example.ofek.prolog.database.entity.ExerciseItem;
import com.example.ofek.prolog.R;
import com.example.ofek.prolog.database.entity.WorklineItem;
import com.example.ofek.prolog.database.entity.WorkoutGroup;
import com.example.ofek.prolog.fragments.NewExerciseDialogFragment;
import com.example.ofek.prolog.fragments.NewWorkoutDialogFragment;
import com.example.ofek.prolog.adapters.WorkoutAdapter;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.ofek.prolog.utils.Utils.*;

/**
 * The main activity of the app.
 * ProL0g is a log application that was created to assist the user with his workouts such as
 * managing the days he trained, which exercise(s) were done on the same workout and so on.
 */

public class HomeScreenActivity extends AppCompatActivity {

    private static boolean[] mIsGroupExpandedArray; // Used on restoration conditions

    private List<WorkoutGroup> mWorkoutList = new ArrayList<>();
    private ExpandableListView mExpandableListView;
    private WorkoutAdapter mListAdapter;
    private SharedPreferences mSharedPref;

    private WorkoutViewModel mWorkoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mExpandableListView = (ExpandableListView) findViewById(R.id.myList);

        mWorkoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        // Observe the changes in the Expandable ListView
        mWorkoutViewModel.getAllWorkouts().observe(this, new Observer<List<WorkoutGroup>>() {
            @Override
            public void onChanged(@Nullable final List<WorkoutGroup> workouts) {
                mListAdapter.setWorkoutList(workouts);
                mWorkoutList = workouts;

                // Setting the correct UI on startup/on change
                if (mListAdapter.getGroupCount() == 0) {
                    setUpdatedView(NO_WORKOUTS_VIEW);
                } else {
                    setUpdatedView(UPDATED_VIEW);
                }
            }
        });

        mListAdapter = new WorkoutAdapter(HomeScreenActivity.this, mWorkoutList);

        mExpandableListView.setAdapter(mListAdapter);

        // Set a context menu for the expandable ListView elements
        registerForContextMenu(mExpandableListView);

        // Get a reference to the fab component and set a click listener
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the last workout position and create a new workout
                createNewWorkout(mListAdapter.getGroupCount());
            }
        });

        // Set the default settings for new users ONLY
        android.support.v7.preference.PreferenceManager
                .setDefaultValues(this, R.xml.preferences, false);

        // Get the saved settings and apply them
        mSharedPref = android.support.v7.preference.PreferenceManager
                .getDefaultSharedPreferences(this);
        int themePref = Integer.parseInt(mSharedPref.getString(PREF_KEY_THEMES, "0"));
        int unitsPref = Integer.parseInt(mSharedPref.getString(PREF_KEY_UNITS, "0"));
        setThemeColor(themePref);
        setUnits(unitsPref);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int typeClicked = ExpandableListView.getPackedPositionType(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);
        int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);

        if (typeClicked == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            menu.setHeaderTitle(getString(R.string.context_menu_group_title) + (groupPosition + 1));
            menu.add(0, EDIT_GROUP, 1, R.string.context_menu_edit_date);
            menu.add(0, DELETE_GROUP, 2, R.string.context_menu_delete);
        }
        if (typeClicked == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            String childName = mWorkoutList.get(groupPosition)
                    .getExerciseList().get(childPosition).getExerciseName();

            menu.setHeaderTitle(getString(R.string.context_menu_child_title) + childName);
            menu.add(0, EDIT_CHILD, 1, R.string.context_menu_edit);
            menu.add(0, DELETE_CHILD, 2, R.string.context_menu_delete);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();

        int typeClicked = ExpandableListView.getPackedPositionType(info.packedPosition);
        int childPosition = ExpandableListView.getPackedPositionChild(info.packedPosition);
        final int groupPosition = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        WorkoutGroup workoutGroup = mWorkoutList.get(groupPosition);

        if (typeClicked == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
            switch (item.getItemId()) {
                case EDIT_GROUP:
                    editGroup(workoutGroup);
                    return true;

                case DELETE_GROUP:
                    deleteGroup(workoutGroup);
                    return true;

                default:
                    break;
            }
        }
        if (typeClicked == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            // childType:
            // 0 = Normal child (single workline)
            // 1 = Custom child (more than a single worklines)
            int childType =
                    typeOfChild(mWorkoutList.get(groupPosition)
                            .getExerciseList().get(childPosition).getWorklineItemsList());

            switch (item.getItemId()) {
                case EDIT_CHILD: /* Edit exercise */
                    ExerciseItem exercise =
                            mWorkoutList.get(groupPosition).getExerciseList().get(childPosition);
                    WorklineItem worklineItem = exercise.getWorklineItemsList().get(FIRST_ITEM);

                    editChild(childType, groupPosition, childPosition, exercise, worklineItem);

                    return true;

                case DELETE_CHILD: /* Delete exercise */
                    WorkoutGroup workout = mWorkoutList.get(groupPosition);

                    deleteChild(childPosition, workout);

                    return true;

                default:
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds Groups to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_homescreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_reset) {
            AlertDialog resetAlert = new AlertDialog.Builder(this)
                    .setTitle(R.string.resetAlert_title)
                    .setMessage(R.string.resetAlert_message)
                    .setPositiveButton(R.string.resetAlert_positive, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Snackbar positiveMessage =
                                    Snackbar.make(findViewById(R.id.home_screen_ConstrainLayout),
                                            R.string.resetAlert_positive_snakebar,
                                            Snackbar.LENGTH_SHORT);
                            mWorkoutViewModel.reset();
                            positiveMessage.show(); }
                    })
                    .setNegativeButton(R.string.resetAlert_negative, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss(); }
                    })
                    .create();

            resetAlert.show();
            // Restore exercise selective list to default
            restoreDefaultExerciseList();
        }

        if(id == R.id.action_reset_exercise_list) {
            // Restore exercise selective list to default
            AlertDialog resetAlert = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.exercise_reset_title))
                    .setPositiveButton(getString(R.string.exercise_reset_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            restoreDefaultExerciseList();
                        }})
                    .setNegativeButton(getString(R.string.exercise_reset_abort), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }})
                    .create();
            resetAlert.show();
        }

        return super.onOptionsItemSelected(item);
    }

    /** Re-set the saved expand statuses of the groups*/
    @Override
    protected void onStart() {
        super.onStart();
        int size = mExpandableListView.getCount();
        for (int i = 0; i < size; i++) {
            if (mIsGroupExpandedArray[i]) {
                mExpandableListView.expandGroup(i);
            }
        }
    }

    /** Check if the groups were expanded and save the status in an array */
    @Override
    protected void onStop() {
        super.onStop();
        int size = mExpandableListView.getCount();
        mIsGroupExpandedArray = new boolean[size];
        for (int i = 0; i < size; i++) {
            if (mExpandableListView.isGroupExpanded(i)) {
                mIsGroupExpandedArray[i] = true;
            }
        }
    }

    public DatePickerDialog newDatePickerDialog(DatePickerDialog.OnDateSetListener callBack,
                                                int year, int month, int dayOfMonth) {
        DatePickerDialog dpd = DatePickerDialog.newInstance(callBack, year, month, dayOfMonth);
        dpd.setThemeDark(false);
        dpd.setAccentColor(getResources().getColor(R.color.datePicker_color));
        dpd.setTitle(getString(R.string.datePicker_title));
        return dpd;
    }

    /** Insert a workout to the list with the data of the last workout position
     *  and the selected date.
     */
    public void buildWorkout(int groupPosition, String date) {
        //set new data as a group
        String workoutPosition = String.valueOf(groupPosition + 1);
        WorkoutGroup workoutGroup = new WorkoutGroup(workoutPosition, date);
        //insert the new workout group to the list
        mWorkoutList.add(workoutGroup);
        //update the visibility of some view items
        setUpdatedView(UPDATED_VIEW);

        mWorkoutViewModel.insert(workoutGroup);
        mListAdapter.notifyDataSetChanged();
    }

    /** Insert an exercise to a list of the default ExerciseList */
    public void addItemToExerciseList(int groupPosition, ExerciseItem child) {
        //get the group
        WorkoutGroup workoutGroup = mWorkoutList.get(groupPosition);
        //get the children for the group
        List<ExerciseItem> exerciseList = workoutGroup.getExerciseList();

        if (child != null) {
            exerciseList.add(child);
        }

        //update the changes
        workoutGroup.setExerciseList(exerciseList);
        mWorkoutViewModel.update(workoutGroup);
        mListAdapter.notifyDataSetChanged();

        //collapse all groups
        collapseAll();
        //expand the group where item was just added
        mExpandableListView.expandGroup(groupPosition);
        //set the current group to be selected so that it becomes visible
        mExpandableListView.setSelectedGroup(groupPosition);
    }

    public WorkoutAdapter getListAdapter() {
        return mListAdapter;
    }

    /** Set the selected weight units of the user */
    private void setUnits(int unitsPref) {
        switch (unitsPref) {
            case 0:
                sSelectedUnits = getResources().getString(R.string.KG_units);
                break;

            case 1:
                sSelectedUnits = getResources().getString(R.string.LB_units);
                break;

            default:
                break;
        }
    }

    /** Set the selected theme color of the user */
    private void setThemeColor(int themePref) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ConstraintLayout tableTabs = (ConstraintLayout) findViewById(R.id.tableTabs);

        switch (themePref) {
            case 0:
                // Default values
                break;

            case 1:
                toolbar.setBackgroundColor(Color.BLUE);
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
                fab.setRippleColor(Color.BLACK);
                tableTabs.setBackgroundColor(Color.BLACK);
                break;

            default:
                break;
        }
    }

    private void createNewWorkout(int workoutPosition) {
        NewWorkoutDialogFragment newWorkDialog = new NewWorkoutDialogFragment();
        newWorkDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomNewDialogFragment);
        newWorkDialog.setGroupPosition(workoutPosition);
        newWorkDialog.show(getFragmentManager(), "getDateFragment");
    }

    /** Collapse all groups in the exp. ListView */
    private void collapseAll() {
        int count = mListAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            mExpandableListView.collapseGroup(i);
        }
    }

    /** Update the new look of the screen after adding the first workout */
    private void setUpdatedView(int viewRespond) {
        //get reference to the visual group
        TextView noWorkoutsYet = (TextView) findViewById(R.id.text_noworkout);
        TextView welcomeTitle = (TextView) findViewById(R.id.text_welcome);
        ConstraintLayout tableTabs = (ConstraintLayout) findViewById(R.id.tableTabs);
        //set new visibilities to the group
        switch (viewRespond) {
            case NO_WORKOUTS_VIEW:
                welcomeTitle.setVisibility(View.VISIBLE);
                noWorkoutsYet.setVisibility(View.VISIBLE);
                tableTabs.setVisibility(View.INVISIBLE);
                break;

            case UPDATED_VIEW:
                welcomeTitle.setVisibility(View.INVISIBLE);
                noWorkoutsYet.setVisibility(View.INVISIBLE);
                tableTabs.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    private int typeOfChild(List<WorklineItem> worklineList) {
        if (worklineList.size() > 1) {
            return CUSTOM_CHILD;
        } else return NORMAL_CHILD;
    }

    /** Delete workout */
    private void deleteGroup(WorkoutGroup groupToDelete) {
        mWorkoutViewModel.delete(groupToDelete);
        mListAdapter.notifyDataSetChanged();
    }

    /** Edit date */
    private void editGroup(final WorkoutGroup groupToEdit) {
        // Create data for the DatePickerDialog
        DatePickerDialog dpd;
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR), // Initial year selection
                month = now.get(Calendar.MONTH), // Initial month selection
                dayOfMonth = now.get(Calendar.DAY_OF_MONTH); // Initial day selection

        //Construct and call to DatePickerDialog
        dpd = newDatePickerDialog(null, year, month, dayOfMonth);
        dpd.show(getFragmentManager(), "showDatePickerDialog");
        dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                String month = new DateFormatSymbols().getMonths()[monthOfYear - 1];
                String date = month + " " + dayOfMonth + ", " + year;
                groupToEdit.setDate(date);
                mWorkoutViewModel.update(groupToEdit);
                mListAdapter.notifyDataSetChanged();
            }
        });
    }

    /** Edit single workline child */
    private void editChild(int childType, int groupPosition,
                           int childPosition, ExerciseItem exercise, WorklineItem workline) {
        String exerciseName = exercise.getExerciseName();
        double weight = workline.getExerciseWeight();
        int reps = workline.getExerciseReps();
        int sets = workline.getSets();

        if (childType == NORMAL_CHILD) {
            // Create a dialog with the given data
            NewExerciseDialogFragment editExerciseDialog = new NewExerciseDialogFragment();
            Bundle bundle = new Bundle();

            bundle.putInt(BUNDLE_GROUP_POSITION, groupPosition);
            bundle.putInt(BUNDLE_CHILD_POSITION, childPosition);

            bundle.putBoolean(BUNDLE_EDIT_MODE, true);
            bundle.putString(BUNDLE_EXERCISE_NAME, exerciseName);
            bundle.putDouble(BUNDLE_EXERCISE_WEIGHT, weight);
            bundle.putInt(BUNDLE_EXERCISE_REPS, reps);
            bundle.putInt(BUNDLE_EXERCISE_SETS, sets);

            editExerciseDialog.setArguments(bundle);
            editExerciseDialog.show(getSupportFragmentManager(), "showEditedExercise");
        } else {
            //TODO: Create a method for a custom child
            Toast.makeText(this, "This function isn't available at the moment"
                    , Toast.LENGTH_SHORT).show();
        }
    }

    /** Delete an exercise */
    private void deleteChild(int childPosition, WorkoutGroup workout) {
        // Remove the exercise and update the workout group
        workout.getExerciseList().remove(childPosition);
        mWorkoutViewModel.update(workout);
        mListAdapter.notifyDataSetChanged();
    }

    /** Reset the value of the Exercises Preference Key in the Shared Preference file */
    private void restoreDefaultExerciseList(){
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(PREF_KEY_EXERCISES_SET, null);
        editor.apply();
    }
}
