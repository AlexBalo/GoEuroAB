package com.goeuro.ab.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
import com.goeuro.ab.utilities.watchers.DateTextWatcher;

public class SearchLocationFragment extends Fragment {

	private static final String START_LOCATION_SAVED = "startLocationSaved";
	private static final String END_LOCATION_SAVED = "endLocationSaved";
	private static final String CALENDAR_LOCATION_SAVED = "calendarLocationSaved";
	
	private ApiClient mApiClient;
	private FragmentContainer mFragmentContainer;
	private EditText mStartEdit, mEndEdit, mCalendarEdit;
	private Button mSearchButton;

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

		createView(view);
		
		if (savedInstanceState != null) {
			String startLocation = savedInstanceState.getString(START_LOCATION_SAVED);
			String endLocation = savedInstanceState.getString(END_LOCATION_SAVED);
			String calendar = savedInstanceState.getString(CALENDAR_LOCATION_SAVED);
			
			mStartEdit.setText(startLocation);
			mEndEdit.setText(endLocation);
			mCalendarEdit.setText(calendar);
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
		mStartEdit = (EditText) view.findViewById(R.id.actv_fragment_search_location_start);
		mEndEdit = (EditText) view.findViewById(R.id.actv_fragment_search_location_end);
		mCalendarEdit = (EditText) view.findViewById(R.id.et_fragment_search_location_calendar);
		ImageView calendarButton = (ImageView) view.findViewById(R.id.iv_fragment_search_location_show_calendar);
		mSearchButton = (Button) view.findViewById(R.id.btn_fragment_search_location);
		
		mCalendarEdit.addTextChangedListener(new DateTextWatcher(mCalendarEdit, 
				new OnCalendarChangedListener() {
					@Override
					public void onCalendarChanged() {
						checkOtherFields();
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

	private void makeLocationRequest() {
		String locale = "de"; // Used "de" for testing reasons
		String paramToSearch = "Pots";

		LocationRequest request = new LocationRequest(mApiClient, locale,
				paramToSearch, new OnSearchLocationRequestListener() {

					@Override
					public void onSearchLocationSuccess(List<Position> positions) {
						String ciccio = "ciccio";
						ciccio += "-bombo";

					}

					@Override
					public void onSearchLocationError(Exception e) {
						String ciccio = "ciccio";
						ciccio += "-bombo";
					}
				});

		request.execute();
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
		checkOtherFields();
	}
	
	private void checkOtherFields() {
		String startText = mStartEdit.getText().toString().trim();
		String endText = mEndEdit.getText().toString().trim();
		int calendar = mCalendarEdit.getText().toString().trim().length();
		
		if (!TextUtils.isEmpty(startText) && !TextUtils.isEmpty(endText) && calendar == 10) {
			mSearchButton.setEnabled(true);
		} else {
			mSearchButton.setEnabled(false);
		}
	}

	public interface FragmentContainer {
		void showDateDialog(String date);
	}
}
