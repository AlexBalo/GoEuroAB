package com.goeuro.ab.model;

public class Position {

    private String mRootType; 
    private String mId;
    private String mName;
    private String mType;
    private String mLatitude;
    private String mLongitude;
    
    // Getters
    public String getRootType() { return mRootType; }
    
    public String getId() { return mId; }
    
    public String getName() { return mName; }
    
    public String getType() { return mType; }
    
    public String getLatitude() { return mLatitude; }
    
    public String getLongitude() { return mLongitude; }
    
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
    
    public void setLatitude(String latitude) { 
    	mLatitude = latitude; 
    }
    
    public void setLongitude(String longitude) { 
    	mLongitude = longitude; 
    }
}
