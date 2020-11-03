package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.itri.view.humanhealth.detail.Imp.PersonInfoHibernateImpl;
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
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
//	private PersonInfosDaoHibernateImpl hqe;
	private PersonInfoHibernateImpl hqe;
	static String NORMAL_PATH = "./resources/image/MapImages/icon_indicator_no_01.png";
	static String WARNING_PATH = "./resources/image/MapImages/icon_indicator_o_01.png";

	@Init
	public void init() {
//		hqe = new PersonInfosDaoHibernateImpl();
		hqe = new PersonInfoHibernateImpl();
		queryStates();
	}

	@NotifyChange({ "personStateList" })
	@Command
	public void refreshPatientInfo() {
//		hqe = new PersonInfosDaoHibernateImpl();
		hqe = new PersonInfoHibernateImpl();
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
		personStateList = new LinkedList<>();
		Set<Long> sensorIdSet = new HashSet<Long>();

		// Get patientList
		List<Combination> combinationList = hqe.getCombination();
		for (Combination item : combinationList) {

			boolean exitFlag = false;
			// merge by PatientId
			for (PersonState personState : personStateList) {
				if (personState.getPatientId() == item.getPatient().getPatientId()) {

					// Set sensorList
					List<Long> sensorIdList = personState.getSensorIdList();
					sensorIdList.add(item.getSensor().getSensorId());
					personState.setSensorIdList(sensorIdList);
					sensorIdSet.add(item.getSensor().getSensorId());

					exitFlag = true;
					break;
				}
			}
			// create new Patient
			if (!exitFlag) {
				PersonState patient = new PersonState();

				// Set Patient Info
				patient.setPatientId(item.getPatient().getPatientId());
				patient.setName(item.getPatient().getPatientInfos().stream().findFirst().get().getName());
				patient.setBedRoom(item.getRoom().getRoomNum());
				patient.setTotalNewsScore(item.getPatient().getTotalNewsScore());
				patient.setEwsLow("4");

				// Set sensorList
				List<Long> sensorIdList = new ArrayList<Long>();
				sensorIdList.add(item.getSensor().getSensorId());
				patient.setSensorIdList(sensorIdList);
				sensorIdSet.add(item.getSensor().getSensorId());

				personStateList.add(patient);
			}
		}

		// Get each patient spec. high & low
		List<Long> sensorIdList = new ArrayList<Long>(sensorIdSet);
		Collections.sort(sensorIdList);
		List<SensorThreshold> patientThresholdList = hqe.getSensorThresholdByIdList(sensorIdList);

		// Set each sensor threshold spec value
		for (PersonState item : personStateList) {
			for (Long sensorId : item.getSensorIdList()) {
				for (SensorThreshold patientThreshold : patientThresholdList) {
					if (sensorId == patientThreshold.getSensor().getSensorId()) {
						int healthTypeId = (int) patientThreshold.getHealthType().getHealthTypeId();

						ThresholdDao thresholdDao = new ThresholdDao();
						thresholdDao.setSensorId(patientThreshold.getSensor().getSensorId());
						thresholdDao.setSpecHigh(patientThreshold.getCriticalHigh());
						thresholdDao.setSpecLow(patientThreshold.getCriticalLow());

						if (1 == healthTypeId) {
							item.setHeartRateThreshold(thresholdDao);
						} else if (2 == healthTypeId) {
							item.setOximeterThreshold(thresholdDao);
						} else if (3 == healthTypeId) {
							item.setBodyTempThreshold(thresholdDao);
						} else if (4 == healthTypeId) {
							item.setBreathRateThreshold(thresholdDao);
						}
					}
				}
			}
		}
	}

	public List<PersonState> getPersonStateList() {
		return personStateList;
	}
}
