package org.itri.view.humanhealth.detail;

import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.personal.chart.Imp.PersonInfosDaoHibernateImpl;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

public class OximeterCurrentView extends SelectorComposer<Window> {

	@Wire("window > bs-row > hbox > vbox")
	private Vbox heartBeatVbox;

	@Wire("window > bs-row > hbox > vbox > #hrLabel")
	private Label hrLabel;

	@Wire("window > bs-row > hbox > vbox > #heightLabel")
	private Label heightLabel;

	@Wire("window > bs-row > hbox > vbox > #lowLabel")
	private Label lowLabel;

	@Wire("window > bs-row > hbox ")
	private Hbox hbox;

	@Wire("window > bs-row > hbox > textbox")
	private Textbox textboxId;

	@Wire("window > bs-row > hbox > label")
	private Label oximeterLabel;

	private String GRAY_HASH = "#2F2F2F";
	private String BLACK_HASH = "#000000";
	private String BLUE_HASH = "#73E9FF";

	private Double oximeterHigh;
	private Double oximeterLow;

	private long patientId = 0;

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setPatientId(textboxId.getValue());
		String dataStr = getOximeterValueById(getPatientId());
		oximeterLabel.setValue(dataStr);

		// Set spec
		setOximeterHigh(heightLabel.getValue());
		setOximeterLow(lowLabel.getValue());

		// Listen spec
		hightLightLabel(dataStr);
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		setPatientId(textboxId.getValue());
		String dataStr = getOximeterValueById(getPatientId());
		oximeterLabel.setValue(dataStr);

		hightLightLabel(dataStr);
	}

	private void hightLightLabel(String dataStr) {

		double data = Double.valueOf(dataStr);
		Double heightData = getOximeterHigh();
		Double lowData = getOximeterLow();

		if (Double.compare(data, heightData) > 0 || Double.compare(data, lowData) < 0) {

			heartBeatVbox.setStyle("background-color: " + BLUE_HASH);
			hbox.setStyle("background-color: " + BLUE_HASH + "; " + "text-align: center" + ";");

			hrLabel.setStyle("color: " + BLACK_HASH);
			heightLabel.setStyle("color: " + BLACK_HASH);
			lowLabel.setStyle("color: " + BLACK_HASH);
			oximeterLabel.setStyle("color: " + BLACK_HASH);
		} else {
			heartBeatVbox.setStyle("background-color: " + GRAY_HASH);
			hbox.setStyle("background-color: " + GRAY_HASH + "; " + "text-align: center" + ";");

			hrLabel.setStyle("color: " + BLUE_HASH);
			heightLabel.setStyle("color: " + BLUE_HASH);
			lowLabel.setStyle("color: " + BLUE_HASH);
			oximeterLabel.setStyle("color: " + BLUE_HASH);

		}
	}

	private String getOximeterValueById(long patientId) {

		PersonInfosDaoHibernateImpl hqe = new PersonInfosDaoHibernateImpl();
		Patient rowData = hqe.getPatientById(patientId);
		if (rowData != null) {
			return rowData.getRtOximeterRecords().stream().findFirst().get().getOximeterData();
		}
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

	public Double getOximeterHigh() {
		return oximeterHigh;
	}

	public void setOximeterHigh(String oximeterHighStr) {
		Double oximeterHigh = Double.valueOf(oximeterHighStr);
		this.oximeterHigh = oximeterHigh;
	}

	public Double getOximeterLow() {
		return oximeterLow;
	}

	public void setOximeterLow(String oximeterLowStr) {
		Double oximeterLow = Double.valueOf(oximeterLowStr);
		this.oximeterLow = oximeterLow;
	}

}
