/*
 * Created by Ofek Pintok on 11/27/18 12:36 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/19/18 2:19 PM
 */

package com.example.ofek.prolog.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ofek.prolog.activities.HomeScreenActivity;
import com.example.ofek.prolog.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormatSymbols;
import java.util.Calendar;

/**
 * This dialog allows the user to insert data of a new workout, such as date.
 * group position is automatically selected.
 *
 */
public class NewWorkoutDialogFragment extends DialogFragment implements
        View.OnClickListener {

    private int mGroupPosition;
    private String mDate;

    private TextView mPickDate;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_new_workout, container, false);

        //Set the title for this fragment
        getDialog().setTitle(R.string.new_workout_title);

        //Format the first line of the dialog
        TextView workoutPositionLine = (TextView) view.findViewById(R.id.text_workout_1);
        int workoutPosition = mGroupPosition + 1;
        String workoutPositionString = Integer.toString(workoutPosition);
        workoutPositionLine.setText(String.format(getString(R.string.new_workout_line_1), workoutPositionString));

        //get reference to mPickDate TextView
        mPickDate = (TextView) view.findViewById(R.id.text_workout_date);
        mPickDate.setOnClickListener(this);

        //Image button that calls a DatePickerDialog
        ImageButton calendar = (ImageButton) view.findViewById(R.id.imageb_workout_calendar);
        calendar.setOnClickListener(this);

        //Buttons of the dialog
        Button cancel = (Button) view.findViewById(R.id.button_workout_cancel);
        cancel.setOnClickListener(this);
        Button confirm = (Button) view.findViewById(R.id.button_workout_confirm);
        confirm.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case (R.id.imageb_workout_calendar):
            case (R.id.text_workout_date):

                DatePickerDialog dpd;
                Calendar now = Calendar.getInstance();
                int year = now.get(Calendar.YEAR), // Initial year selection
                        month = now.get(Calendar.MONTH), // Initial month selection
                        dayOfMonth = now.get(Calendar.DAY_OF_MONTH); // Initial day selection

                //Construct and call to DatePickerDialog
                dpd = ((HomeScreenActivity) getActivity()).newDatePickerDialog(null, year, month, dayOfMonth);
                dpd.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        String month = new DateFormatSymbols().getMonths()[monthOfYear];
                        String date = month + " " + dayOfMonth + ", " + year;
                        mPickDate.setText(date);
                        setDate(date);
                    }
                });

                dpd.show(getFragmentManager(), "showDatePickerDialog");
                break;

            case R.id.button_workout_confirm:
                if (mDate != null) {
                    //Pass the data of the new workout
                    ((HomeScreenActivity) getActivity()).buildWorkout(mGroupPosition, mDate);
                    this.dismiss();
                } else {
                    Toast.makeText(getActivity(), R.string.error_no_selected_date, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.button_workout_cancel:
                this.dismiss();
                break;
        }
    }

    public void setGroupPosition(int groupPosition) {
        this.mGroupPosition = groupPosition;
    }

    public void setDate(String date) {
        this.mDate = date;
    }
}
