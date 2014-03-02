package com.goeuro.ab;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.goeuro.ab.fragments.SearchLocationFragment;
import com.goeuro.ab.network.NetworkController;
import com.goeuro.ab.utilities.Constants;
import com.goeuro.ab.utilities.DateUtils;

public class MainActivity extends FragmentActivity implements SearchLocationFragment.FragmentContainer, OnDateSetListener {

    private static final String DATEPICKER_TAG = "datepicker";
    private static final String FRAGMENT_TAG_SEARCH_LOCATION = "FRAGMENT_TAG_SEARCH_LOCATION";
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		NetworkController.getInstance().init(getApplicationContext());
		
        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fm.beginTransaction()
            	.add(R.id.fl_activity_main_fragment_container, new SearchLocationFragment(), FRAGMENT_TAG_SEARCH_LOCATION)
            	.commit();
        } else {
            fm.findFragmentByTag(FRAGMENT_TAG_SEARCH_LOCATION);
        }
	}

	@Override
	public void showDateDialog(String date) {
        Calendar calendar = Calendar.getInstance();
		
        if (!TextUtils.isEmpty(date) && date.length() == 10) {
        	int day = Integer.valueOf(date.substring(0, 2));
        	int month = Integer.valueOf(date.substring(3, 5));
        	int year = Integer.valueOf(date.substring(6, 10));
        	if (DateUtils.isDateValid(day, month, year)) {
        		calendar.set(Calendar.DAY_OF_MONTH, day);
        		calendar.set(Calendar.MONTH, month);
        		calendar.set(Calendar.YEAR, year);
        	}
        }
        
		DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), 
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
        datePickerDialog.setYearRange(Constants.MIN_CALENDAR, Constants.MAX_CALENDAR);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
	}

	@Override
	public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
		FragmentManager fragmentManager = getSupportFragmentManager();
        SearchLocationFragment searchLocationFragment = (SearchLocationFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG_SEARCH_LOCATION);
        searchLocationFragment.setDate(day, month, year);
	}
}
