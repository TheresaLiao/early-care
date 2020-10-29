package org.itri.view.humanhealth.personal.chart.dao;

import java.util.Date;

public class PersonInfoTableDao {

	private Double heartRateData;
	private Double oximeterData;
	private Double breathData;
	private Double bodyTempData;
	private Double newsScore;
	private Date timeCreated;

	public Double getHeartRateData() {
		return heartRateData;
	}

	public void setHeartRateData(Double heartRateData) {
		this.heartRateData = heartRateData;
	}

	public Double getOximeterData() {
		return oximeterData;
	}

	public void setOximeterData(Double oximeterData) {
		this.oximeterData = oximeterData;
	}

	public Double getBreathData() {
		return breathData;
	}

	public void setBreathData(Double breathData) {
		this.breathData = breathData;
	}

	public Double getBodyTempData() {
		return bodyTempData;
	}

	public void setBodyTempData(Double bodyTempData) {
		this.bodyTempData = bodyTempData;
	}

	public Double getNewsScore() {
		return newsScore;
	}

	public void setNewsScore(Double newsScore) {
		this.newsScore = newsScore;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}
}
