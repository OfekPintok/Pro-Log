
/*
 * Created by Ofek Pintok on 11/27/18 12:35 PM
 * Copyright (c) 2018 . All rights reserved
 * Last modified 11/12/18 8:15 PM
 */

package com.example.ofek.prolog.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.ofek.prolog.fragments.SettingsFragment;
import com.example.ofek.prolog.utils.Utils;

/**
 * This activity display the setting from a preference fragment(s).
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        // Customized action bar color
        SharedPreferences sharedPref = android.support.v7.preference.PreferenceManager
                .getDefaultSharedPreferences(this);
        int themePref = Integer.parseInt(sharedPref.getString(Utils.PREF_KEY_THEMES, "0"));

        switch (themePref) {
            case 0:
                // Default values
                break;
            case 1:
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                break;
        }
    }
}
