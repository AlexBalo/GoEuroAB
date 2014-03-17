package com.goeuro.ab.utilities.watchers;

import com.goeuro.ab.listeners.OnCalendarChangedListener;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DateTextWatcher implements TextWatcher {
	private EditText mEditText;
	private OnCalendarChangedListener mListener;

	public DateTextWatcher(EditText e, OnCalendarChangedListener listener) {
		mEditText = e;
		mListener = listener;
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	public void afterTextChanged(Editable s) {
		String text = mEditText.getText().toString();

		if (text.length() >= 3 && !text.substring(2, 3).equals(".")) {
			text = text.substring(0, 2) + "." + text.substring(2, text.length());
			mEditText.setText(text);
			mEditText.setSelection(text.length());
		}
		
		if (text.length() >= 6 && !text.substring(5, 6).equals(".")) {
			text = text.substring(0, 5) + "." + text.substring(5, text.length());
			mEditText.setText(text);
			mEditText.setSelection(text.length());
		}
		
		notifyListener();
	}
	
	private void notifyListener() {
		if (mListener != null) {
			mListener.onCalendarChanged();
		}
	}
}