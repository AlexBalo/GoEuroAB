package com.goeuro.ab.network.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.goeuro.ab.model.Position;

public class LocationParser {

	public static ArrayList<Position> parsePositionJson(JSONObject positionJson) {
		ArrayList<Position> positions = new ArrayList<Position>();

		try {
			JSONArray results = positionJson.getJSONArray(Tags.RESULTS);
			for (int i = 0; i < results.length(); i++) {
				JSONObject position = results.getJSONObject(i);
				String rootType = position.optString(Tags._TYPE);
				String id = position.optString(Tags._ID);
				String name = position.optString(Tags.NAME);
				String type = position.optString(Tags.TYPE);
				JSONObject geolocation = position.optJSONObject(Tags.GEO_POSITION);
				String latitude = geolocation.optString(Tags.LATITUDE);
				String longitude = geolocation.optString(Tags.LONGITUDE);

				Position positionObject = new Position();
				positionObject.setRootType(rootType);
				positionObject.setId(id);
				positionObject.setName(name);
				positionObject.setType(type);
				positionObject.setLatitude(latitude);
				positionObject.setLongitude(longitude);

				positions.add(positionObject);
			}
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return positions;
	}
}
