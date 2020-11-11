package org.itri.view.humanhealth.personal.chart.dao;

import java.util.Date;

public class SensorIdTimeDao {
	private long sensorId;
	private Date strTime;
	private Date endTime;

	public long getSensorId() {
		return sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	public Date getStrTime() {
		return strTime;
	}

	public void setStrTime(Date strTime) {
		this.strTime = strTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
