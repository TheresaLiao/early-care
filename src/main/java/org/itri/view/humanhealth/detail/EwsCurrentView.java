package org.itri.view.humanhealth.detail;

import org.zkoss.zul.Audio;
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

	@Wire("window > bs-row > #curHbox > #patientIdTextbox")
	private Textbox patientIdTextbox;
	private long patientId = 0;

	@Wire("window > bs-row > #curHbox > #alertAudio")
	private Audio alertAudio;

	@Wire("window > bs-row > #curHbox > #ewsLabel")
	private Label ewsLabel;
	private String ewsStatus;

	private static String GREEN_HASH = "#5CE498";
	private static String RED_HASH = "#FF0000";
	private static String WHITE_HASH = "#FFFFFF";
	private static String STATUS_CRITICAL = "C";

	private int ewsSpec = 4;

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setPatientId(patientIdTextbox.getValue());

		getEwsValueById(getPatientId());
		hightLightLabel2();
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		getEwsValueById(getPatientId());
		hightLightLabel2();
	}

	private void hightLightLabel2() {
		if (getEwsStatus().equals(STATUS_CRITICAL)) {
			hbox.setStyle("background-color: " + RED_HASH + ";text-align: center");
			ewsLabel.setStyle("color: " + WHITE_HASH);

			alertAudio.play();
		} else {
			hbox.setStyle("text-align: center");
			ewsLabel.setStyle("color: " + WHITE_HASH);
		}
	}

	// Get real time data
	private void getEwsValueById(long patientId) {
		EwsViewDaoHibernateImpl hqe = new EwsViewDaoHibernateImpl();
		Patient patient = hqe.getPatientById(patientId);
		if (patient == null) {
			System.out.println("patientId :" + patientId + " can't find.");
		} else {
			ewsLabel.setValue(String.valueOf(patient.getTotalNewsScore()));
			setEwsStatus(patient.getNewsStatus());
		}
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientIdStr) {
		patientId = Long.parseLong(patientIdStr);
		this.patientId = patientId;
	}

	public String getEwsStatus() {
		return ewsStatus;
	}

	public void setEwsStatus(String ewsStatus) {
		this.ewsStatus = ewsStatus;
	}
}
