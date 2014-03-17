package com.goeuro.ab.fragments;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.goeuro.ab.R;
import com.goeuro.ab.listeners.OnCalendarChangedListener;
import com.goeuro.ab.listeners.OnSearchLocationRequestListener;
import com.goeuro.ab.model.Position;
import com.goeuro.ab.network.ApiClient;
import com.goeuro.ab.network.NetworkController;
import com.goeuro.ab.request.LocationRequest;
import com.goeuro.ab.utilities.Utilities;
import com.goeuro.ab.utilities.watchers.DateTextWatcher;

public class SearchLocationFragment extends Fragment {

	private static final String START_LOCATION_SAVED = "startLocationSaved";
	private static final String END_LOCATION_SAVED = "endLocationSaved";
	private static final String CALENDAR_LOCATION_SAVED = "calendarLocationSaved";
	
	private ApiClient mApiClient;
	private FragmentContainer mFragmentContainer;
	private AutoCompleteTextView mStartEdit, mEndEdit;
	private EditText mCalendarEdit;
	private Button mSearchButton;
	private LocationManager mLocationManager;
	private Location mUserLocation;
	private String mLastSearchedString;

	public static SearchLocationFragment newInstance() {
        SearchLocationFragment fragment = new SearchLocationFragment();
        return fragment;
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Fragment parentFragment = getParentFragment();
		if (parentFragment == null) {
			mFragmentContainer = (FragmentContainer) activity;
		} else {
			mFragmentContainer = (FragmentContainer) parentFragment;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
		mApiClient = NetworkController.getInstance().getApiClient();
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_search_location, container, false);

		mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		mUserLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    if (mUserLocation == null) {
	    	mUserLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	    }
	    
		createView(view);
		
		if (savedInstanceState != null) {
			String startLocation = savedInstanceState.getString(START_LOCATION_SAVED);
			String endLocation = savedInstanceState.getString(END_LOCATION_SAVED);
			String calendar = savedInstanceState.getString(CALENDAR_LOCATION_SAVED);
			
			mStartEdit.setText(startLocation);
			mEndEdit.setText(endLocation);
			mCalendarEdit.setText(calendar);
		}
		
		if (!Utilities.isInternetConnectionAvailable(getActivity())) {
			showConnectionAlert();
		}

		return view;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putString(START_LOCATION_SAVED, mStartEdit.getText().toString());
		outState.putString(END_LOCATION_SAVED, mEndEdit.getText().toString());
		outState.putString(CALENDAR_LOCATION_SAVED, mCalendarEdit.getText().toString());
	}

	private void createView(View view) {
		mStartEdit = (AutoCompleteTextView) view.findViewById(R.id.actv_fragment_search_location_start);
		mEndEdit = (AutoCompleteTextView) view.findViewById(R.id.actv_fragment_search_location_end);
		mCalendarEdit = (EditText) view.findViewById(R.id.et_fragment_search_location_calendar);
		ImageView calendarButton = (ImageView) view.findViewById(R.id.iv_fragment_search_location_show_calendar);
		mSearchButton = (Button) view.findViewById(R.id.btn_fragment_search_location);
		
		ArrayAdapter<String> locationAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line);
		mStartEdit.setAdapter(locationAdapter);
		mStartEdit.addTextChangedListener(new CustomTextWatcher(mStartEdit, locationAdapter));
		mEndEdit.setAdapter(locationAdapter);
		mEndEdit.addTextChangedListener(new CustomTextWatcher(mEndEdit, locationAdapter));
		
		mCalendarEdit.setKeyListener(null);
		mCalendarEdit.addTextChangedListener(new DateTextWatcher(mCalendarEdit, 
				new OnCalendarChangedListener() {
					@Override
					public void onCalendarChanged() {
						enableOrDisableSearchButton();
					}
				})
		);
		
		calendarButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFragmentContainer.showDateDialog(mCalendarEdit.getText().toString());
			}
		});
		
		mSearchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), getActivity().getString(R.string.location_search_not_implemented), Toast.LENGTH_LONG).show();
			}
		});
	}

	private void makeLocationRequest(final AutoCompleteTextView textview, final ArrayAdapter<String> adapter) {
		String locale = "de"; // Used "de" for testing reasons not checking for current locale
		final String paramToSearch = textview.getText().toString();

		if ((mLastSearchedString == null || !mLastSearchedString.equals(paramToSearch)) && Utilities.isInternetConnectionAvailable(getActivity())) {
			LocationRequest request = new LocationRequest(mApiClient, locale, paramToSearch, mUserLocation,
					new OnSearchLocationRequestListener() {
	
						@Override
						public void onSearchLocationSuccess(List<Position> positions) {
							if (mLastSearchedString == null || !mLastSearchedString.equals(paramToSearch)) {
								mLastSearchedString = paramToSearch;
								
								adapter.clear();
								if (positions != null) {
		                            for (Position position : positions) {
		                                adapter.add(position.getName());
		                            }
								}
								adapter.notifyDataSetChanged();
								
								if (textview.getText().toString().equals(paramToSearch)) {
									textview.setText(paramToSearch);
									textview.setSelection(paramToSearch.length());
								}
							}
						}
	
						@Override
						public void onSearchLocationError(Exception e) {
						}
					});
	
			request.execute();
		}
	}
	
	public void setDate(int day, int month, int year) {
		String dayString = String.valueOf(day);
		String monthString = String.valueOf(month);
		if (dayString.length() == 1) {
			dayString = "0" + dayString;
		}
		if (monthString.length() == 1) {
			monthString = "0" + monthString;
		}
		
		mCalendarEdit.setText(dayString + "." + monthString + "." + String.valueOf(year));
		mCalendarEdit.requestFocus();
		mCalendarEdit.setSelection(mCalendarEdit.getText().toString().length());
		enableOrDisableSearchButton();
	}
	
	private void enableOrDisableSearchButton() {
		String startText = mStartEdit.getText().toString().trim();
		String endText = mEndEdit.getText().toString().trim();
		int calendar = mCalendarEdit.getText().toString().trim().length();
		
		if (!TextUtils.isEmpty(startText) && !TextUtils.isEmpty(endText) && calendar == 10) {
			mSearchButton.setEnabled(true);
		} else {
			mSearchButton.setEnabled(false);
		}
	}
	
	protected void showConnectionAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(getActivity().getResources().getString(R.string.network_status_enable_connection))
				.setCancelable(false)
				.setTitle(getActivity().getResources().getString(R.string.network_status))
				.setPositiveButton(getActivity().getResources().getString(R.string.settings),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
								startActivity(myIntent);
								dialog.cancel();
							}
						})
				.setNegativeButton(getActivity().getResources().getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private class CustomTextWatcher implements TextWatcher {

		private static final long MULTIPLE_INSERT_DELAY = 500;
		
		private AutoCompleteTextView mTextView;
		private ArrayAdapter<String> mAdapter;
		private int mCounter, mReference;
		private Handler mHandler;
		
		private CustomTextWatcher(AutoCompleteTextView textview, ArrayAdapter<String> adapter) {
			mTextView = textview;
			mAdapter = adapter;
			mHandler = new Handler();
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (s.length() > 2) {
				mCounter++;
				mHandler.postDelayed(
						new Runnable() {
							public void run() {
								mReference++;
								if (mReference == mCounter) {
									makeLocationRequest(mTextView, mAdapter);
								}
							}
						}, MULTIPLE_INSERT_DELAY);
			} else {
				mAdapter.clear();
			}
			enableOrDisableSearchButton();
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
	}

	public interface FragmentContainer {
		void showDateDialog(String date);
	}
}
