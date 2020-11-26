package org.itri.view.humanhealth.personal.chart.dao;

import org.itri.view.humanhealth.hibernate.NewsWarningCondition;

public class EwsSpecDao extends NewsWarningCondition {

	private String value;

	public EwsSpecDao() {
	}

	public NewsWarningCondition getNewsWarningCondition() {
		NewsWarningCondition resp = new NewsWarningCondition();
		resp.setCountBeforeWarning(this.getCountBeforeWarning());
		resp.setCurrentCount(this.getCurrentCount());
		resp.setIsDeleted(this.isIsDeleted());
		resp.setNewsMathOperator(this.getNewsMathOperator());
		resp.setNewsWarningConditionId(this.getNewsWarningConditionId());
		resp.setNewsWarningThreshold(this.getNewsWarningThreshold());
		resp.setPatient(this.getPatient());
		resp.setTimeBeforeWarning(this.getTimeBeforeWarning());
		return resp;
	}

	public EwsSpecDao(NewsWarningCondition item) {
		setCountBeforeWarning(item.getCountBeforeWarning());
		setCurrentCount(item.getCurrentCount());
		setIsDeleted(item.isIsDeleted());
		setNewsMathOperator(item.getNewsMathOperator());
		setNewsWarningConditionId(item.getNewsWarningConditionId());
		setNewsWarningThreshold(item.getNewsWarningThreshold());
		setPatient(item.getPatient());
		setTimeBeforeWarning(item.getTimeBeforeWarning());
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
