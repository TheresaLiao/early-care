package org.itri.view.humanhealth.personal.chart.dao;

import java.util.ArrayList;
import java.util.List;

import org.itri.view.admin.dao.Type;

public class PersonState {

	private long patientId;
	private String name;
	private long roomId;
	private String bedRoom;
	private String heartBeat;
	private String bodyTemperature;
	private String breathRate;
	private String oximeter;
	private Type type;
	private Integer totalNewsScore;
	private String totalStatusImgPath;
	private String historyDate;

	private List<Long> sensorIdList = new ArrayList<Long>();
	private ThresholdDao heartRateThreshold;
	private ThresholdDao oximeterThreshold;
	private ThresholdDao bodyTempThreshold;
	private ThresholdDao breathRateThreshold;

	private List<EwsSpecDao> ewsSpecList = new ArrayList<EwsSpecDao>();

	private String ewsLow;

	public Integer getTotalNewsScore() {
		return totalNewsScore;
	}

	public void setTotalNewsScore(Integer totalNewsScore) {
		this.totalNewsScore = totalNewsScore;
	}

	public String getTotalStatusImgPath() {
		return totalStatusImgPath;
	}

	public void setTotalStatusImgPath(String totalStatus) {
		this.totalStatusImgPath = totalStatus;
	}

	public String getBreathRate() {
		return breathRate;
	}

	public String getOximeter() {
		return oximeter;
	}

	public void setOximeter(String oximeter) {
		this.oximeter = oximeter;
	}

	public void setBreathRate(String breathRate) {
		this.breathRate = breathRate;
	}

	public Type getType() {
		return type;
	}

	public String getBedRoom() {
		return bedRoom;
	}

	public void setBedRoom(String bedRoom) {
		this.bedRoom = bedRoom;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeartBeat() {
		return heartBeat;
	}

	public void setHeartBeat(String heartBeat) {
		this.heartBeat = heartBeat;
	}

	public String getBodyTemperature() {
		return bodyTemperature;
	}

	public void setBodyTemperature(String bodyTemperature) {
		this.bodyTemperature = bodyTemperature;
	}

	public String getHistoryDate() {
		return historyDate;
	}

	public void setHistoryDate(String historyDate) {
		this.historyDate = historyDate;
	}

	public String getEwsLow() {
		return ewsLow;
	}

	public void setEwsLow(String ewsLow) {
		this.ewsLow = ewsLow;
	}

	public ThresholdDao getHeartRateThreshold() {
		return heartRateThreshold;
	}

	public void setHeartRateThreshold(ThresholdDao heartRateThreshold) {
		this.heartRateThreshold = heartRateThreshold;
	}

	public ThresholdDao getOximeterThreshold() {
		return oximeterThreshold;
	}

	public void setOximeterThreshold(ThresholdDao oximeterThreshold) {
		this.oximeterThreshold = oximeterThreshold;
	}

	public ThresholdDao getBodyTempThreshold() {
		return bodyTempThreshold;
	}

	public void setBodyTempThreshold(ThresholdDao bodyTempThreshold) {
		this.bodyTempThreshold = bodyTempThreshold;
	}

	public ThresholdDao getBreathRateThreshold() {
		return breathRateThreshold;
	}

	public void setBreathRateThreshold(ThresholdDao breathRateThreshold) {
		this.breathRateThreshold = breathRateThreshold;
	}

	public List<Long> getSensorIdList() {
		return sensorIdList;
	}

	public void setSensorIdList(List<Long> sensorIdList) {
		this.sensorIdList = sensorIdList;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public List<EwsSpecDao> getEwsSpecList() {
		return ewsSpecList;
	}

	public void setEwsSpecList(List<EwsSpecDao> ewsSpecList) {
		this.ewsSpecList = ewsSpecList;
	}

}
