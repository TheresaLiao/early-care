package org.itri.view.humanhealth.detail;

import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.itri.view.humanhealth.hibernate.RtOximeterRecord;
import org.itri.view.humanhealth.personal.chart.Imp.OximeterViewDaoHibernateImpl;

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

	@Wire("window > bs-row > hbox > textbox")
	private Textbox textboxId;

	@Wire("window > bs-row > hbox > label")
	private Label heartBeatLabel;

	private String GRAY_HASH = "#2F2F2F";
//	private String GREEN_HASH = "#5CE498";
	private String BLACK_HASH = "#000000";
	private String RED_HASH = "#FF0000";

	private Double heartRateHigh;
	private Double heartRateLow;

	private long sensortId = 0;

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getHeartBeatValueById(getSensortId());
		heartBeatLabel.setValue(dataStr);

		// Set spec
		setHeartRateHigh(heightLabel.getValue());
		setHeartRateLow(lowLabel.getValue());

		// Listen spec
		hightLightLabel(dataStr);
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getHeartBeatValueById(getSensortId());
		heartBeatLabel.setValue(dataStr);

		// Listen spec
		hightLightLabel(dataStr);
	}

	// Set style for Hight Light Label
	private void hightLightLabel(String dataStr) {

		double data = Double.valueOf(dataStr);
		double heightData = getHeartRateHigh();
		double lowData = getHeartRateLow();

		if (Double.compare(data, heightData) > 0 || Double.compare(data, lowData) < 0) {

			heartBeatVbox.setStyle("background-color: " + RED_HASH);
			hbox.setStyle("background-color: " + RED_HASH + ";text-align: center");

			hrLabel.setStyle("color: " + BLACK_HASH);
			heightLabel.setStyle("color: " + BLACK_HASH);
			lowLabel.setStyle("color: " + BLACK_HASH);
			heartBeatLabel.setStyle("color: " + BLACK_HASH);
		} else {
			heartBeatVbox.setStyle("background-color: " + GRAY_HASH);
			hbox.setStyle("background-color: " + GRAY_HASH + ";text-align: center");

			hrLabel.setStyle("color: " + RED_HASH);
			heightLabel.setStyle("color: " + RED_HASH);
			lowLabel.setStyle("color: " + RED_HASH);
			heartBeatLabel.setStyle("color: " + RED_HASH);
		}
	}

	private String getHeartBeatValueById(long sensorId) {
		OximeterViewDaoHibernateImpl hqe = new OximeterViewDaoHibernateImpl();
		RtOximeterRecord rowData = hqe.getRtOximeterRecord(sensorId);
		if (rowData != null) {
			return rowData.getHeartRateData();
		}
		return "0.0";
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
}
