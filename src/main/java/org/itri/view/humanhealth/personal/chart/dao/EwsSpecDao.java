package org.itri.view.humanhealth.personal.chart.dao;

import org.itri.view.humanhealth.hibernate.NewsWarningCondition;

public class EwsSpecDao extends NewsWarningCondition {

	private String value;

	public EwsSpecDao() {
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
