package com.goeuro.ab.listeners;

import java.util.List;

import com.goeuro.ab.model.Position;

public interface OnSearchLocationRequestListener {
	void onSearchLocationSuccess(List<Position> positions);
	void onSearchLocationError(Exception e);
}
