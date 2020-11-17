package org.itri.view.humanhealth.personal.chart.dao;

import org.itri.view.humanhealth.hibernate.NewsMathOperator;

public class EwsSpecDao {

	private long newsWarningConditionId;
	private NewsMathOperator newsMathOperator;
	private int newsWarningThreshold;
	private int timeBeforeWarning;
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getNewsWarningConditionId() {
		return newsWarningConditionId;
	}

	public void setNewsWarningConditionId(long newsWarningConditionId) {
		this.newsWarningConditionId = newsWarningConditionId;
	}

	public NewsMathOperator getNewsMathOperator() {
		return newsMathOperator;
	}

	public void setNewsMathOperator(NewsMathOperator newsMathOperator) {
		this.newsMathOperator = newsMathOperator;
	}

	public int getNewsWarningThreshold() {
		return newsWarningThreshold;
	}

	public void setNewsWarningThreshold(int newsWarningThreshold) {
		this.newsWarningThreshold = newsWarningThreshold;
	}

	public int getTimeBeforeWarning() {
		return timeBeforeWarning;
	}

	public void setTimeBeforeWarning(int timeBeforeWarning) {
		this.timeBeforeWarning = timeBeforeWarning;
	}
}
