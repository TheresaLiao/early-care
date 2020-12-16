package org.itri.view.humanhealth.detail;

import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.RtHeartRhythmRecord;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.personal.chart.Imp.BreathRateViewDaoHibernateImpl;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Audio;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class BreathRateCurrentView extends SelectorComposer<Window> {

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

	@Wire("window > bs-row > hbox > #breathRateLabel")
	private Label breathRateLabel;

	@Wire("window > bs-row > hbox >  #sensorIdTextbox")
	private Textbox sensorIdTextbox;
	private long sensortId = 0;

	@Wire("window > bs-row > hbox > #patientIdTextbox")
	private Textbox patientIdTextbox;
	private long patientId = 0;

	@Wire("window > bs-row > hbox > #alertAudio")
	private Audio alertAudio;

	@Wire("window > bs-row > #devStatHbox > vbox > #connectImg")
	private Image connectImg;

	private String GRAY_HASH = "#2F2F2F";
	private String BLACK_HASH = "#000000";
	private String WHITE_HASH = "#FFFFFF";

	private Double breathRateHigh;
	private Double breathRateLow;

	private String deviceConnectionErrorNum = "3";
	private String CONNECT_OK = "resources/image/icon2-connect-b-ok.png";
	private String CONNECT_NO = "resources/image/icon2-connect-b-no.png";
	private static String STATUS_CRITICAL = "C";

	BreathRateViewDaoHibernateImpl hqe = new BreathRateViewDaoHibernateImpl();

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setSensortId(sensorIdTextbox.getValue());
		setPatientId(formateStr2Long(patientIdTextbox.getValue()));
		String dataStr = getBreathRateValueById(getSensortId());
		breathRateLabel.setValue(dataStr);

		// Set spec
		setBreathRateHigh(heightLabel.getValue());
		setBreathRateLow(lowLabel.getValue());

		// Listen spec
		hightLightLabel2();
		getSensorStatus(getSensortId());
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		String dataStr = getBreathRateValueById(getSensortId());
		breathRateLabel.setValue(dataStr);

		hightLightLabel2();
		getSensorStatus(getSensortId());
	}

	// Set style for Hight Light Label
	private void hightLightLabel2() {
		Patient patient = hqe.getPatientById(getPatientId());

		if (patient.getBreathStatus().equals(STATUS_CRITICAL)) {
			heartBeatVbox.setStyle("background-color: " + WHITE_HASH);
			hbox.setStyle("background-color: " + WHITE_HASH + "; " + "text-align: center" + ";");

			hrLabel.setStyle("color: " + BLACK_HASH);
			heightLabel.setStyle("color: " + BLACK_HASH);
			lowLabel.setStyle("color: " + BLACK_HASH);
			breathRateLabel.setStyle("color: " + BLACK_HASH);

			alertAudio.play();

		} else {
			heartBeatVbox.setStyle("background-color: " + GRAY_HASH);
			hbox.setStyle("background-color: " + GRAY_HASH + "; " + "text-align: center" + ";");

			hrLabel.setStyle("color: " + WHITE_HASH);
			heightLabel.setStyle("color: " + WHITE_HASH);
			lowLabel.setStyle("color: " + WHITE_HASH);
			breathRateLabel.setStyle("color: " + WHITE_HASH);
		}
	}

	private long formateStr2Long(String str) {
		return Long.parseLong(str);
	}

	private String getBreathRateValueById(long sensorId) {
		RtHeartRhythmRecord rowData = hqe.getRtHeartRhythmRecord(sensorId);
		if (rowData != null) {
			return rowData.getBreathData();
		}
		return "0.0";
	}

	private void getSensorStatus(long sensorId) {
		Sensor sensor = hqe.getSensorBySensorId(sensortId);
		if (sensor == null) {
			connectImg.setSrc(CONNECT_NO);
		} else {
			connectImg.setSrc(getConnectStatusIcon(sensor.getSensorDeviceStatus()));
		}
	}

	public long getSensortId() {
		return sensortId;
	}

	public void setSensortId(String sensortIdStr) {
		if (sensortIdStr.isEmpty()) {
			sensortId = 0;
		} else {
			sensortId = Long.parseLong(sensortIdStr);
		}
		this.sensortId = sensortId;

	}

	public Double getBreathRateHigh() {
		return breathRateHigh;
	}

	public void setBreathRateHigh(String breathRateHighStr) {
		Double breathRateHigh = new Double(0.0);
		if (!breathRateHighStr.isEmpty()) {
			breathRateHigh = Double.valueOf(breathRateHighStr);
		}
		this.breathRateHigh = breathRateHigh;
	}

	public Double getBreathRateLow() {
		return breathRateLow;
	}

	public void setBreathRateLow(String breathRateLowStr) {
		Double breathRateLow = new Double(0.0);
		if (!breathRateLowStr.isEmpty()) {
			breathRateLow = Double.valueOf(breathRateLowStr);
		}
		this.breathRateLow = breathRateLow;
	}

	private String getConnectStatusIcon(String deviceStatus) {

		if (deviceStatus.equals(deviceConnectionErrorNum)) {
			return CONNECT_OK;
		}
		return CONNECT_NO;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}
}
