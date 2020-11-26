package org.itri.view.humanhealth.detail;

import org.itri.view.humanhealth.hibernate.RtTempPadRecord;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.personal.chart.Imp.TemperatureViewDaoHibernateImpl;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hbox;
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

	private String GRAY_HASH = "#2F2F2F";
	private String BLACK_HASH = "#000000";
	private String GREEN_HASH = "#5CE498";

	private Double bodyTempHigh;
	private Double bodyTempLow;

	private long sensortId = 0;

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
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getTemperatureValueById(getSensortId());
		temperatureLabel.setValue(dataStr);

		// Listen spec
		hightLightLabel(dataStr);
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

		TemperatureViewDaoHibernateImpl hqe = new TemperatureViewDaoHibernateImpl();
		RtTempPadRecord rowData = hqe.getRtTempPadRecord(sensortId);
		if (rowData != null) {
			return rowData.getBodyTempData();
		}
		return "0.0";
	}

	private String getBatteryPersent(String batteryLevel) {

		// volt top : 4.2 , bottom: 3.65
		double top = 4.2;
		double bottom = 3.65;
		double defaultData = 1;

		double gap = top - bottom;
		double value = Float.valueOf(batteryLevel);
		if (value < bottom) {
			return String.valueOf(defaultData);
		}

		double data = (value - bottom) / gap;
		return String.valueOf(data * 100);
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

}
