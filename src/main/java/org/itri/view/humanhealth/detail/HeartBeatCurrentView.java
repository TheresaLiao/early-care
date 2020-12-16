package org.itri.view.humanhealth.detail;

import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.personal.chart.Imp.OximeterViewDaoHibernateImpl;
import org.zkoss.zul.Audio;

public class HeartBeatCurrentView extends SelectorComposer<Window> {

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

	@Wire("window > bs-row > hbox > #heartBeatLabel")
	private Label heartBeatLabel;

	@Wire("window > bs-row > hbox > #sensorIdTextbox")
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
	private String RED_HASH = "#FF0000";

	private Double heartRateHigh;
	private Double heartRateLow;

	private static String deviceConnectionErrorNum = "3";
	private static String CONNECT_OK = "resources/image/icon2-connect-b-ok.png";
	private static String CONNECT_NO = "resources/image/icon2-connect-b-no.png";
	private static String STATUS_CRITICAL = "C";
	private static String ALERT_MUSIC = "resources/music/mixkit-home-standard-ding-dong-109.wav";

	OximeterViewDaoHibernateImpl hqe = new OximeterViewDaoHibernateImpl();

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setSensortId(sensorIdTextbox.getValue());
		setPatientId(formateStr2Long(patientIdTextbox.getValue()));
		String dataStr = getHeartBeatValueById(getSensortId());
		heartBeatLabel.setValue(dataStr);

		// Set spec
		setHeartRateHigh(heightLabel.getValue());
		setHeartRateLow(lowLabel.getValue());

		// Listen spec
		hightLightLabel2();
		getSensorStatus(getSensortId());
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		String dataStr = getHeartBeatValueById(getSensortId());
		heartBeatLabel.setValue(dataStr);

		// Listen spec
		hightLightLabel2();
		getSensorStatus(getSensortId());
	}

	// Set style for Hight Light Label
	private void hightLightLabel2() {
		Patient patient = hqe.getPatientById(getPatientId());

		if (patient.getHeartRateStatus().equals(STATUS_CRITICAL)) {
			heartBeatVbox.setStyle("background-color: " + RED_HASH);
			hbox.setStyle("background-color: " + RED_HASH + ";text-align: center");

			hrLabel.setStyle("color: " + BLACK_HASH);
			heightLabel.setStyle("color: " + BLACK_HASH);
			lowLabel.setStyle("color: " + BLACK_HASH);
			heartBeatLabel.setStyle("color: " + BLACK_HASH);

			alertAudio.play();
		} else {
			heartBeatVbox.setStyle("background-color: " + GRAY_HASH);
			hbox.setStyle("background-color: " + GRAY_HASH + ";text-align: center");

			hrLabel.setStyle("color: " + RED_HASH);
			heightLabel.setStyle("color: " + RED_HASH);
			lowLabel.setStyle("color: " + RED_HASH);
			heartBeatLabel.setStyle("color: " + RED_HASH);
		}
	}

	private long formateStr2Long(String str) {
		return Long.parseLong(str);
	}

	private String getHeartBeatValueById(long sensorId) {
		RtOximeterRecord rowData = hqe.getRtOximeterRecord(sensorId);
		if (rowData != null) {
			return rowData.getHeartRateData();
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

	public Double getHeartRateHigh() {
		return heartRateHigh;
	}

	public void setHeartRateHigh(String heartRateHighStr) {
		Double heartRateHigh = new Double(0.0);
		if (!heartRateHighStr.isEmpty()) {
			heartRateHigh = Double.valueOf(heartRateHighStr);
		}
		this.heartRateHigh = heartRateHigh;
	}

	public double getHeartRateLow() {
		return heartRateLow;
	}

	public void setHeartRateLow(String heartRateLowStr) {
		Double heartRateLow = new Double(0.0);
		if (!heartRateLowStr.isEmpty()) {
			heartRateLow = Double.valueOf(heartRateLowStr);
		}
		this.heartRateLow = heartRateLow;
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
