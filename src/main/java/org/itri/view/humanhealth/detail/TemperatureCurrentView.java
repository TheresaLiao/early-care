package org.itri.view.humanhealth.detail;

import org.itri.view.humanhealth.hibernate.RtTempPadRecord;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.personal.chart.Imp.TemperatureViewDaoHibernateImpl;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class TemperatureCurrentView extends SelectorComposer<Window> {
	@Wire("window > bs-row > hbox > vbox")
	private Vbox heartBeatVbox;

	@Wire("window > bs-row > hbox > vbox > #tempLabel")
	private Label tempLabel;

	@Wire("window > bs-row > hbox > vbox > #heightLabel")
	private Label heightLabel;

	@Wire("window > bs-row > hbox > vbox > #lowLabel")
	private Label lowLabel;

	@Wire("window > bs-row > hbox ")
	private Hbox hbox;

	@Wire("window > bs-row > hbox > textbox")
	private Textbox textboxId;

	@Wire("window > bs-row > hbox > label")
	private Label temperatureLabel;

	@Wire("window > bs-row > #devStatHbox > vbox > #connectImg")
	private Image connectImg;

	private String GRAY_HASH = "#2F2F2F";
	private String BLACK_HASH = "#000000";
	private String GREEN_HASH = "#5CE498";

	private Double bodyTempHigh;
	private Double bodyTempLow;

	private long sensortId = 0;

	private String deviceConnectionErrorNum = "3";
	private String CONNECT_OK = "resources/image/icon2-connect-b-ok.png";
	private String CONNECT_NO = "resources/image/icon2-connect-b-no.png";

	TemperatureViewDaoHibernateImpl hqe = new TemperatureViewDaoHibernateImpl();

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getTemperatureValueById(getSensortId());
		temperatureLabel.setValue(dataStr);

		// Set spec
		setBodyTempHigh(heightLabel.getValue());
		setBodyTempLow(lowLabel.getValue());

		// Listen spec
		hightLightLabel(dataStr);
		getSensorStatus(getSensortId());
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getTemperatureValueById(getSensortId());
		temperatureLabel.setValue(dataStr);

		// Listen spec
		hightLightLabel(dataStr);
		getSensorStatus(getSensortId());
	}

	private void hightLightLabel(String dataStr) {
		double data = Double.valueOf(dataStr);
		Double heightData = getBodyTempHigh();
		Double lowData = getBodyTempLow();

		if (Double.compare(data, heightData) > 0 || Double.compare(data, lowData) < 0) {

			heartBeatVbox.setStyle("background-color: " + GREEN_HASH);
			hbox.setStyle("background-color: " + GREEN_HASH + "; " + "text-align: center" + ";");

			tempLabel.setStyle("color: " + BLACK_HASH);
			heightLabel.setStyle("color: " + BLACK_HASH);
			lowLabel.setStyle("color: " + BLACK_HASH);
			temperatureLabel.setStyle("color: " + BLACK_HASH);
		} else {
			heartBeatVbox.setStyle("background-color: " + GRAY_HASH);
			hbox.setStyle("background-color: " + GRAY_HASH + "; " + "text-align: center" + ";");

			tempLabel.setStyle("color: " + GREEN_HASH);
			heightLabel.setStyle("color: " + GREEN_HASH);
			lowLabel.setStyle("color: " + GREEN_HASH);
			temperatureLabel.setStyle("color: " + GREEN_HASH);
		}
	}

	private String getTemperatureValueById(long sensortId) {
		RtTempPadRecord rowData = hqe.getRtTempPadRecord(sensortId);
		if (rowData != null) {
			return rowData.getBodyTempData();
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

	public Double getBodyTempHigh() {
		return bodyTempHigh;
	}

	public void setBodyTempHigh(String bodyTempHighStr) {
		Double bodyTempHigh = new Double(0.0);
		if (!bodyTempHighStr.isEmpty()) {
			bodyTempHigh = Double.valueOf(bodyTempHighStr);
		}
		this.bodyTempHigh = bodyTempHigh;
	}

	public Double getBodyTempLow() {
		return bodyTempLow;
	}

	public void setBodyTempLow(String bodyTempLowStr) {
		Double bodyTempLow = new Double(0.0);
		if (!bodyTempLowStr.isEmpty()) {
			bodyTempLow = Double.valueOf(bodyTempLowStr);
		}
		this.bodyTempLow = bodyTempLow;
	}

	private String getConnectStatusIcon(String deviceStatus) {

		if (deviceStatus.equals(deviceConnectionErrorNum)) {
			return CONNECT_OK;
		}
		return CONNECT_NO;
	}

}
