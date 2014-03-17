package com.goeuro.ab.model;

import java.util.Comparator;

import android.location.Location;

public class Position implements Comparable<Position> {

    private String mRootType; 
    private String mId;
    private String mName;
    private String mType;
    private double mLatitude;
    private double mLongitude;
    
    private Location mUserLocation;
    
    // Getters
    public String getRootType() { return mRootType; }
    
    public String getId() { return mId; }
    
    public String getName() { return mName; }
    
    public String getType() { return mType; }
    
    public double getLatitude() { return mLatitude; }
    
    public double getLongitude() { return mLongitude; }
    
    public Location getUserLocation() { return mUserLocation; }
    
    // Setters
    public void setRootType(String rootType) {
    	mRootType = rootType;
    }
    
    public void setId(String id) {
    	mId = id;
    }
    
    public void setName(String name) { 
    	mName = name; 
    }
    
    public void setType(String type) { 
    	mType = type; 
    }
    
    public void setLatitude(Double latitude) { 
    	mLatitude = latitude; 
    }
    
    public void setLongitude(Double longitude) { 
    	mLongitude = longitude; 
    }
    
    public void setUserLocation(Location userLocation) {
    	mUserLocation = userLocation;
    }

    
	public int compareTo(Position comparePosition) {
 
		//ascending order
		return this.compareTo(comparePosition);
 
	}
 
	public static Comparator<Position> PositionDistanceComparator = new Comparator<Position>() {
 
	    public int compare(Position position1, Position position2) {
 
	    	Location userLocation = position1.getUserLocation();
	    	
	    	Location position1Location = new Location("First location");
	    	position1Location.setLatitude(position1.getLatitude());
	    	position1Location.setLongitude(position1.getLongitude());

	    	Location position2Location = new Location("Second location");
	    	position2Location.setLatitude(position2.getLatitude());
	    	position2Location.setLongitude(position2.getLongitude());
	    	
	    	// Get distance in KMs
	    	Float position1Distance = (userLocation.distanceTo(position1Location)) / 1000;
	    	Float position2Distance = (userLocation.distanceTo(position2Location)) / 1000;
 
	      //ascending order
	      return position1Distance.compareTo(position2Distance);
 
	      //descending order
	      //return fruitName2.compareTo(fruitName1);
	    }
 
	};
}
