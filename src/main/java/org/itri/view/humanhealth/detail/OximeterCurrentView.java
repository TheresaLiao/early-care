package org.itri.view.humanhealth.detail;

import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.personal.chart.Imp.OximeterViewDaoHibernateImpl;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
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

	@Wire("window > bs-row > #devStatHbox > vbox > #connectImg")
	private Image connectImg;

	private String GRAY_HASH = "#2F2F2F";
	private String BLACK_HASH = "#000000";
	private String BLUE_HASH = "#73E9FF";

	private Double oximeterHigh;
	private Double oximeterLow;

	private long sensortId = 0;

	private String deviceConnectionErrorNum = "3";
	private String CONNECT_OK = "resources/image/icon2-connect-b-ok.png";
	private String CONNECT_NO = "resources/image/icon2-connect-b-no.png";

	OximeterViewDaoHibernateImpl hqe = new OximeterViewDaoHibernateImpl();
	
	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getOximeterValueById(getSensortId());
		oximeterLabel.setValue(dataStr);

		// Set spec
		setOximeterHigh(heightLabel.getValue());
		setOximeterLow(lowLabel.getValue());

		// Listen spec
		hightLightLabel(dataStr);
		getSensorStatus(getSensortId());
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getOximeterValueById(getSensortId());
		oximeterLabel.setValue(dataStr);

		hightLightLabel(dataStr);
		getSensorStatus(getSensortId());
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

	private String getOximeterValueById(long sensortId) {
		
		RtOximeterRecord rowData = hqe.getRtOximeterRecord(sensortId);
		if (rowData != null) {
			return rowData.getOximeterData();
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

	public Double getOximeterHigh() {
		return oximeterHigh;
	}

	public void setOximeterHigh(String oximeterHighStr) {
		Double oximeterHigh = new Double(0.0);
		if (!oximeterHighStr.isEmpty()) {
			oximeterHigh = Double.valueOf(oximeterHighStr);
		}
		this.oximeterHigh = oximeterHigh;
	}

	public Double getOximeterLow() {
		return oximeterLow;
	}

	public void setOximeterLow(String oximeterLowStr) {
		Double oximeterLow = new Double(0.0);
		if (!oximeterLowStr.isEmpty()) {
			oximeterLow = Double.valueOf(oximeterLowStr);
		}
		this.oximeterLow = oximeterLow;
	}

	private String getConnectStatusIcon(String deviceStatus) {

		if (deviceStatus.equals(deviceConnectionErrorNum)) {
			return CONNECT_OK;
		}
		return CONNECT_NO;
	}
}
