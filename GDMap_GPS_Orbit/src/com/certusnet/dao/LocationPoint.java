package com.certusnet.dao;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Location_Table")
public class LocationPoint{

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField(columnName = "latitude")
	private String latitude;
	
	@DatabaseField(columnName = "longitude")
	private String longitude;
	
	@DatabaseField(columnName = "markFlag")
	private String markFlag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getMarkFlag() {
		return markFlag;
	}

	public void setMarkFlag(String markFlag) {
		this.markFlag = markFlag;
	}
	
	
}
