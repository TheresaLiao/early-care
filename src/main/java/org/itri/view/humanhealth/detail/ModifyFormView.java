package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.List;

import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.personal.chart.Imp.PersonInfoTableDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Selectbox;

public class ModifyFormView {
	private PersonInfoTableDaoHibernateImpl hqe;

	private DateKeyValueSelectBox selectPatient;
	private List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();

	@Wire
	private Selectbox selectbox;

	@Init
	public void init() {
		System.out.println("ModifyFormView init");
		hqe = new PersonInfoTableDaoHibernateImpl();
		getSelectBoxList();
	}

	// Get Patient List
	private void getSelectBoxList() {
		List<PatientInfo> dataList = hqe.getPatientList();
		for (PatientInfo patient : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(patient.getPatient().getPatientId(),
					patient.getName());
			patientList.add(item);
		}
		selectPatient = patientList.get(0);
	}

	@Command
	public void submitClick() {
		System.out.println("ModifyFormView submitClick");
	}

	public List<DateKeyValueSelectBox> getPatientList() {
		return patientList;
	}

	public DateKeyValueSelectBox getSelectPatient() {
		return selectPatient;
	}

	public void setSelectPatient(DateKeyValueSelectBox selectPatient) {
		this.selectPatient = selectPatient;
	}
}
