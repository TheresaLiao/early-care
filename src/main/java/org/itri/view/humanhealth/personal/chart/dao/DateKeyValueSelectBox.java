package org.itri.view.humanhealth.personal.chart.dao;

import org.zkoss.zul.AbstractListModel;

public class DateKeyValueSelectBox extends AbstractListModel {
	private long value;
	private String text;

	public DateKeyValueSelectBox(long value, String text) {
		this.value = value;
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public long getValue() {
		return this.value;
	}

	@Override
	public Object getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
}
