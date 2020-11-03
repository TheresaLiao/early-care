package org.itri.view.humanhealth.detail;

import org.itri.view.humanhealth.hibernate.RtHeartRhythmRecord;
import org.itri.view.humanhealth.personal.chart.Imp.BreathRateViewDaoHibernateImpl;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Hbox;
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

	@Wire("window > bs-row > hbox > textbox")
	private Textbox textboxId;

	@Wire("window > bs-row > hbox > label")
	private Label breathRateLabel;

	private String GRAY_HASH = "#2F2F2F";
	private String BLACK_HASH = "#000000";
//	private String YELLOW_HASH = "#F8FF70";
	private String WHITE_HASH = "#FFFFFF";

	private Double breathRateHigh;
	private Double breathRateLow;

	private long sensortId = 0;

	@Override
	public void doAfterCompose(Window comp) throws Exception {

		// Component Setting
		super.doAfterCompose(comp);

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getBreathRateValueById(getSensortId());
		breathRateLabel.setValue(dataStr);

		// Set spec
		setBreathRateHigh(heightLabel.getValue());
		setBreathRateLow(lowLabel.getValue());

		// Listen spec
		hightLightLabel(dataStr);
	}

	@Listen("onTimer = #timer")
	public void updateData() {

		// get PatientId & find data by PatientId
		setSensortId(textboxId.getValue());
		String dataStr = getBreathRateValueById(getSensortId());
		breathRateLabel.setValue(dataStr);

		hightLightLabel(dataStr);

	}

	private void hightLightLabel(String dataStr) {
		double data = Double.valueOf(dataStr);
		Double heightData = getBreathRateHigh();
		Double lowData = getBreathRateLow();

		if (Double.compare(data, heightData) > 0 || Double.compare(data, lowData) < 0) {

			heartBeatVbox.setStyle("background-color: " + WHITE_HASH);
			hbox.setStyle("background-color: " + WHITE_HASH + "; " + "text-align: center" + ";");

			hrLabel.setStyle("color: " + BLACK_HASH);
			heightLabel.setStyle("color: " + BLACK_HASH);
			lowLabel.setStyle("color: " + BLACK_HASH);
			breathRateLabel.setStyle("color: " + BLACK_HASH);
		} else {
			heartBeatVbox.setStyle("background-color: " + GRAY_HASH);
			hbox.setStyle("background-color: " + GRAY_HASH + "; " + "text-align: center" + ";");

			hrLabel.setStyle("color: " + WHITE_HASH);
			heightLabel.setStyle("color: " + WHITE_HASH);
			lowLabel.setStyle("color: " + WHITE_HASH);
			breathRateLabel.setStyle("color: " + WHITE_HASH);
		}
	}

	private String getBreathRateValueById(long sensorId) {

		BreathRateViewDaoHibernateImpl hqe = new BreathRateViewDaoHibernateImpl();
		RtHeartRhythmRecord rowData = hqe.getRtHeartRhythmRecord(sensorId);
		if (rowData != null) {
			return rowData.getBreathData();
		}
		System.out.println("sensorId :" + sensorId + " can't find.");
		return "NULL";
	}

	public long getSensortId() {
		return sensortId;
	}

	public void setSensortId(String sensortIdStr) {
		sensortId = Long.parseLong(sensortIdStr);
		this.sensortId = sensortId;
		
	}

	public Double getBreathRateHigh() {
		return breathRateHigh;
	}

	public void setBreathRateHigh(String breathRateHighStr) {
		Double breathRateHigh = Double.valueOf(breathRateHighStr);
		this.breathRateHigh = breathRateHigh;
	}

	public Double getBreathRateLow() {
		return breathRateLow;
	}

	public void setBreathRateLow(String breathRateLowStr) {
		Double breathRateLow = Double.valueOf(breathRateLowStr);
		this.breathRateLow = breathRateLow;
	}

}
