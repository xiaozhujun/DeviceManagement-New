package com.csei.entity;

public class Device {
	private int id;
	private String number;
	private String batchId;
	private String bachNumber;
	private String typeId;
	private String deviceType;
	private int isMainDevice;
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	public String getBatchId() {
		return batchId;
	}
	
	public void setBachNumber(String bachNumber) {
		this.bachNumber = bachNumber;
	}
	
	public String getBachNumber() {
		return bachNumber;
	}
	
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getTypeId() {
		return typeId;
	}
	
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	
	public void setIsMainDevice(int isMainDevice) {
		this.isMainDevice = isMainDevice;
	}
	
	public int getIsMainDevice() {
		return isMainDevice;
	}
}
