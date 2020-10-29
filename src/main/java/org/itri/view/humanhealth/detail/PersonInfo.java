package org.itri.view.humanhealth.detail;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientThreshold;
import org.itri.view.humanhealth.personal.chart.Imp.PersonInfosDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.dao.PersonState;
import org.itri.view.humanhealth.personal.chart.dao.ThresholdDao;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class PersonInfo {

	private List<PersonState> personStateList;
	private PersonInfosDaoHibernateImpl hqe;
	static String NORMAL_PATH = "./resources/image/MapImages/icon_indicator_no_01.png";
	static String WARNING_PATH = "./resources/image/MapImages/icon_indicator_o_01.png";

	@Init
	public void init() {
		hqe = new PersonInfosDaoHibernateImpl();
		queryStates();
	}

	@NotifyChange({ "personStateList" })
	@Command
	public void refreshPatientInfo() {
		hqe = new PersonInfosDaoHibernateImpl();
		queryStates();
	}

	@Command
	public void modifyClick(@BindingParam("item") PersonState item) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("orderItems", item);
		String template = "/humanHealthDetail/modifyConnectForm.zul";
		Window window = (Window) Executions.createComponents(template, null, arguments);
		window.doModal();
	}

	private void queryStates() {
		// Get patientList
		List<Patient> patientList = hqe.getPatientTotalNewsScoreFourList();
		personStateList = new LinkedList<>();
		for (Patient item : patientList) {
			PersonState patient = new PersonState();

			patient.setId(item.getPatientId());
			patient.setName(item.getPatientInfos().stream().findFirst().get().getName());
			patient.setBedRoom(item.getRoom().getRoomNum());
			patient.setHeartBeat(item.getRtHeartRhythmRecords().stream().findFirst().get().getHeartRateData());
			patient.setOximeter(item.getRtOximeterRecords().stream().findFirst().get().getOximeterData());
			patient.setBreathRate(item.getRtHeartRhythmRecords().stream().findFirst().get().getBreathData());
			patient.setBodyTemperature(item.getRtTempPadRecords().stream().findFirst().get().getBodyTempData());
			patient.setTotalNewsScore(item.getTotalNewsScore());

			patient.setEwsLow("4");
			personStateList.add(patient);
		}

		// Get each patient spec. high & low
		for (PersonState item : personStateList) {
			List<PatientThreshold> patientThresholdList = hqe.getPatientThresholdByPatientId(item.getId());

			for (PatientThreshold patientThreshold : patientThresholdList) {
				ThresholdDao thresholdDao = new ThresholdDao();
				thresholdDao.setSensorId(patientThreshold.getSensor().getSensorId());
				thresholdDao.setSpecHigh(patientThreshold.getCriticalHigh());
				thresholdDao.setSpecLow(patientThreshold.getCriticalLow());

				int healthTypeId = (int) patientThreshold.getHealthType().getHealthTypeId();
				switch (healthTypeId) {
				case 1:
					item.setHeartRateThreshold(thresholdDao);
				case 2:
					item.setOximeterThreshold(thresholdDao);
				case 3:
					item.setBodyTempThreshold(thresholdDao);
				case 4:
					item.setBreathRateThreshold(thresholdDao);
				default:
				}
			}
		}
	}

	public List<PersonState> getPersonStateList() {
		return personStateList;
	}

}
