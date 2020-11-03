package org.itri.view.humanhealth.detail;

import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.personal.chart.Imp.EwsViewDaoHibernateImpl;

public class EwsCurrentView extends SelectorComposer<Window> {

	@Wire("window > bs-row > #curHbox > vbox > #tempLabel")
	private Label tempLabel;

	@Wire("window > bs-row > #curHbox > vbox > #heightLabel")
	private Label heightLabel;

	@Wire("window > bs-row > #curHbox")
	private Hbox hbox;

	@Wire("window > bs-row > #curHbox > textbox")
	private Textbox textboxId;

	@Wire("window > bs-row > #curHbox > label")
	private Label ewsLabel;

	private String GREEN_HASH = "#5CE498";
	private String RED_HASH = "#FF0000";
	private String WHITE_HASH = "#FFFFFF";

	private long patientId = 0;
	private int ewsSpec = 4;

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setPatientId(textboxId.getValue());
		String dataStr = getEwsValueById(getPatientId());
		ewsLabel.setValue(dataStr);

		hightLightLabel(dataStr);
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		setPatientId(textboxId.getValue());
		String dataStr = getEwsValueById(getPatientId());
		ewsLabel.setValue(dataStr);

		hightLightLabel(dataStr);
	}

	private void hightLightLabel(String dataStr) {
		double data = Integer.valueOf(dataStr);

		if (data >= ewsSpec) {
			hbox.setStyle("background-color: " + RED_HASH + ";text-align: center");
			ewsLabel.setStyle("color: " + WHITE_HASH);
		} else {
			hbox.setStyle("text-align: center");
			ewsLabel.setStyle("color: " + WHITE_HASH);
		}
	}

	// Get real time data
	private String getEwsValueById(long patientId) {
		EwsViewDaoHibernateImpl hqe = new EwsViewDaoHibernateImpl();
		Patient patient = hqe.getPatientById(patientId);
		if (patient != null)
			return String.valueOf(patient.getTotalNewsScore());

		System.out.println("patientId :" + patientId + " can't find.");
		return "NULL";
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientIdStr) {
		patientId = Long.parseLong(patientIdStr);
		this.patientId = patientId;
	}
}
