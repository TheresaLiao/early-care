package org.itri.view.humanhealth.personal.chart.dao;

public class ThresholdDao {
	private long sensorId;
	private String specHigh;
	private String specLow;

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public String getSpecHigh() {
		return specHigh;
	}

	public void setSpecHigh(String specHigh) {
		this.specHigh = specHigh;
	}

	public String getSpecLow() {
		return specLow;
	}

	public void setSpecLow(String specLow) {
		this.specLow = specLow;
	}
}
