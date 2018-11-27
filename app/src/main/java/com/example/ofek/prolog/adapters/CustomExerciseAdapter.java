/*
 * Created by Ofek Pintok on 11/27/18 12:35 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/11/18 5:38 PM
 */

package com.example.ofek.prolog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ofek.prolog.fragments.CustomExerciseDialogFragment;
import com.example.ofek.prolog.R;
import com.example.ofek.prolog.database.entity.WorklineItem;

import java.util.ArrayList;
import java.util.List;

import static com.example.ofek.prolog.utils.Utils.sSelectedUnits;

/**
 * Custom adapter that manages the ListView of {@link CustomExerciseDialogFragment}
 *
 */
public class CustomExerciseAdapter extends ArrayAdapter<WorklineItem> {

    private List<WorklineItem> mWorklineItemsList = new ArrayList<>();
    private Context mContext;

    public CustomExerciseAdapter(Context context, List<WorklineItem> worklineItems) {
        super(context, 0, worklineItems);
        this.mWorklineItemsList = worklineItems;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_custom_exercise, parent, false);
        }

        WorklineItem currentWorklineItem = mWorklineItemsList.get(position);
        double weight = currentWorklineItem.getExerciseWeight();
        int reps = currentWorklineItem.getExerciseReps();
        int sets = currentWorklineItem.getSets();
        String weightS = String.valueOf(weight) + sSelectedUnits,
                repsS = String.valueOf(reps) + " " + mContext.getString(R.string.Reps) ,
                setsS = String.valueOf(sets) + " " + mContext.getString(R.string.Sets);


        TextView weight_tv = (TextView) view.findViewById(R.id.text_custom_weight);
        weight_tv.setText(weightS);

        TextView reps_tv = (TextView) view.findViewById(R.id.text_custom_reps);
        reps_tv.setText(repsS);

        TextView sets_tv = (TextView) view.findViewById(R.id.text_custom_sets);
        sets_tv.setText(setsS);

        return view;
    }
}
