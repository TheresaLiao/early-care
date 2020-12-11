package org.itri.view.patientInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.patientInfo.Imp.patientSummaryHibernateImpl;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class patientSummary {

	static String CREATE_PAGE = "/patientInfo/patientCreate.zul";
	static String MODIFY_PAGE = "/patientInfo/patientModify.zul";

	List<PatientInfo> patientSummary = new ArrayList<PatientInfo>();
	private patientSummaryHibernateImpl hqe;

	@NotifyChange("patientSummary")
	@Init
	public void init() {
		hqe = new patientSummaryHibernateImpl();
		queryStates();
	}

	private void queryStates() {
		System.out.println("queryStates");
		patientSummary = hqe.getPatientList();
	}

	@Command
	public void createClick() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		Window window = (Window) Executions.createComponents(CREATE_PAGE, null, null);
		window.doModal();
	}

	@NotifyChange({ "patientSummary" })
	@GlobalCommand
	public void refreshPatientSummary() {
		queryStates();
	}

	@Command
	public void modifyPatient(@BindingParam("roomDao") PatientInfo item) {
		System.out.println("modifyPatient");
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("orderItems", item);
		Window window = (Window) Executions.createComponents(MODIFY_PAGE, null, arguments);
		window.doModal();
	}

	public List<PatientInfo> getPatientSummary() {
		return patientSummary;
	}
}
