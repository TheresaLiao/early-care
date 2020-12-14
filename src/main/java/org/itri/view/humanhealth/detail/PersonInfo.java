package org.itri.view.humanhealth.detail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.itri.view.humanhealth.detail.Imp.PersonInfoHibernateImpl;
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.HeartRhythmRecord;
import org.itri.view.humanhealth.hibernate.NewsRecord;
import org.itri.view.humanhealth.hibernate.NewsWarningCondition;
import org.itri.view.humanhealth.hibernate.OximeterRecord;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
import org.itri.view.humanhealth.hibernate.TempPadRecord;
import org.itri.view.humanhealth.personal.chart.Imp.BreathRateViewDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.Imp.EwsViewDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.Imp.OximeterViewDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.Imp.TemperatureViewDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.dao.EwsSpecDao;
import org.itri.view.humanhealth.personal.chart.dao.PersonState;
import org.itri.view.humanhealth.personal.chart.dao.SensorIdTimeDao;
import org.itri.view.humanhealth.personal.chart.dao.ThresholdDao;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import com.opencsv.CSVWriter;

public class PersonInfo {

	private List<PersonState> personStateList;
	private PersonInfoHibernateImpl hqe;
	static String NORMAL_PATH = "./resources/image/MapImages/icon_indicator_no_01.png";
	static String WARNING_PATH = "./resources/image/MapImages/icon_indicator_o_01.png";
	static String MODIFY_PAGE = "/humanHealthDetail/modifyConnectForm.zul";

	@Init
	public void init() {
		hqe = new PersonInfoHibernateImpl();
		queryStates();
	}

	@NotifyChange({ "personStateList" })
	@GlobalCommand
	public void refresHumanChartSet() {
		hqe = new PersonInfoHibernateImpl();
		queryStates();
	}

	@Command
	public void downloadClick(@BindingParam("item") PersonState item) throws IOException {
		System.out.println("downloadClick");

		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

		StringBuffer fileNameBuf = new StringBuffer();
		fileNameBuf.append(item.getBedRoom());
		fileNameBuf.append("_");
		fileNameBuf.append(dateFormat.format(new Date()));
		fileNameBuf.append(".csv");

//		// For ubuntu
		String parentPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/resources");
		File file = writeCsvFile(parentPath + "/" + fileNameBuf.toString(), item);
		Filedownload.save("resources/" + fileNameBuf.toString(), null);

		// For Window
//		File file = writeCsvFile(fileNameBuf.toString(), item);
//		byte[] buffer = new byte[(int) file.length()];
//		FileInputStream fs = new FileInputStream(file);
//		fs.read(buffer);
//		fs.close();
//		ByteArrayInputStream is = new ByteArrayInputStream(buffer);
//		System.out.println("fileNameBuf: " + fileNameBuf.toString());
//		AMedia amedia = new AMedia(fileNameBuf.toString(), "csv", "application/octet-stream", is);
//		Filedownload.save(amedia);

	}

	@Command
	public void modifyClick(@BindingParam("item") PersonState item) {
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("orderItems", item);
		Window window = (Window) Executions.createComponents(MODIFY_PAGE, null, arguments);
		window.doModal();
	}

	private File writeCsvFile(String filePath, PersonState item) throws IOException {
		File file = new File(filePath);
		try {
			FileWriter outputfile = new FileWriter(filePath);
			CSVWriter writer = new CSVWriter(outputfile);
			writer.writeAll(getRawData(item));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private List<String[]> getRawData(PersonState item) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		hqe = new PersonInfoHibernateImpl();
		OximeterViewDaoHibernateImpl oxhqe = new OximeterViewDaoHibernateImpl();
		BreathRateViewDaoHibernateImpl brhqe = new BreathRateViewDaoHibernateImpl();
		TemperatureViewDaoHibernateImpl temphqe = new TemperatureViewDaoHibernateImpl();
		EwsViewDaoHibernateImpl ewshqe = new EwsViewDaoHibernateImpl();

		List<Combination> combinationList = hqe.getCombinationByRoomId(item.getRoomId(), item.getPatientId());
		List<SensorIdTimeDao> sensorTimeList = new ArrayList<SensorIdTimeDao>();

		for (Combination combination : combinationList) {
			SensorIdTimeDao sensorIdTimeDao = new SensorIdTimeDao();
			sensorIdTimeDao.setSensorId(combination.getSensor().getSensorId());
			sensorIdTimeDao.setStrTime(combination.getStartTime());
			sensorIdTimeDao.setEndTime(combination.getEndTime());
			sensorTimeList.add(sensorIdTimeDao);
		}
		System.out.println("combination size: " + combinationList.size());

		// Title
		List<String[]> resp = new ArrayList<String[]>();
		resp.add(new String[] { "Room", "Name" });
		resp.add(new String[] { item.getBedRoom(), item.getName() });

		// HeartRateRecord
		List<OximeterRecord> oxmeterDataList = oxhqe.getOximeterBySensorListRecordList(sensorTimeList);
		System.out.println("oxmeterDataList size: " + oxmeterDataList.size());
		resp.add(new String[] { "Time", "HeartBeat" });
		for (OximeterRecord oxmeterData : oxmeterDataList) {
			resp.add(new String[] { dateFormat.format(oxmeterData.getTimeCreated()), oxmeterData.getHeartRateData() });
		}

		// OximeterRecord
		resp.add(new String[] { "Time", "oximeter" });
		for (OximeterRecord oxmeterData : oxmeterDataList) {
			resp.add(new String[] { dateFormat.format(oxmeterData.getTimeCreated()), oxmeterData.getOximeterData() });
		}

		// BreathRate
		List<HeartRhythmRecord> breathRateList = brhqe.getHeartRhythmBySensorListRecordList(sensorTimeList);
		System.out.println("breathRateList size: " + breathRateList.size());
		resp.add(new String[] { "Time", "BreathRate" });
		for (HeartRhythmRecord breathRate : breathRateList) {
			resp.add(new String[] { dateFormat.format(breathRate.getTimeCreated()), breathRate.getBreathData() });
		}

		// Temperature
		List<TempPadRecord> tempList = temphqe.getTempPadBySensorListRecordList(sensorTimeList);
		System.out.println("tempList size: " + tempList.size());
		resp.add(new String[] { "Time", "Temperature" });
		for (TempPadRecord temp : tempList) {
			resp.add(new String[] { dateFormat.format(temp.getTimeCreated()), temp.getBodyTempData() });
		}

		// EWS
		List<NewsRecord> ewsList = ewshqe.getNewsRecordOneMonthByPatientId(item.getPatientId());
		System.out.println("ewsList size: " + ewsList.size());
		resp.add(new String[] { "Time", "EWS" });
		for (NewsRecord ews : ewsList) {
			resp.add(new String[] { dateFormat.format(ews.getTimeCreated()), Integer.toString(ews.getNewsScore()) });
		}
		return resp;
	}

	private void queryStates() {
		personStateList = new LinkedList<>();
		Set<Long> sensorIdSet = new HashSet<Long>();

		// Get patientList
		List<Long> roomIdList = hqe.getRoomIdListByRoomGroup(1);

		List<Combination> combinationList = hqe.getCombinationEndTimeNullByRoomId(roomIdList);
		for (Combination item : combinationList) {

			boolean exitFlag = false;
			// merge by PatientId
			for (PersonState personState : personStateList) {
				if (personState.getPatientId() == item.getPatient().getPatientId() && item.getSensor() != null) {

					// Set sensorList
					List<Long> sensorIdList = personState.getSensorIdList();
					sensorIdSet.add(item.getSensor().getSensorId());
					sensorIdList.add(item.getSensor().getSensorId());
					personState.setSensorIdList(sensorIdList);

					exitFlag = true;
					break;
				}
			}
			// create new Patient
			if (!exitFlag) {
				PersonState patient = new PersonState();

				// Set Patient Info
				patient.setPatientId(item.getPatient().getPatientId());
				patient.setRoomId(item.getRoom().getRoomId());
				patient.setBedRoom(item.getRoom().getRoomNum());
				patient.setTotalNewsScore(item.getPatient().getTotalNewsScore());
				patient.setName(item.getPatient().getPatientInfos().stream().findFirst().get().getName());
				patient.setEwsLow("4");

				// Set sensorList
				if (item.getSensor() != null) {
					List<Long> sensorIdList = new ArrayList<Long>();
					sensorIdList.add(item.getSensor().getSensorId());
					patient.setSensorIdList(sensorIdList);
					sensorIdSet.add(item.getSensor().getSensorId());
				}

				// Set EWS Spec.
				List<EwsSpecDao> ewsSpecList = new ArrayList<EwsSpecDao>();
				for (NewsWarningCondition ewsSpec : item.getPatient().getNewsWarningConditions()) {
					EwsSpecDao ewsSpecDao = new EwsSpecDao();
					ewsSpecDao.setNewsWarningConditionId(ewsSpec.getNewsWarningConditionId());
					ewsSpecDao.setValue(
							"EWS " + " " + ewsSpec.getNewsWarningThreshold() + " / " + ewsSpec.getTimeBeforeWarning());
					ewsSpecList.add(ewsSpecDao);
				}
				patient.setEwsSpecList(ewsSpecList);

				personStateList.add(patient);
			}
		}

		// Get each patient spec. high & low
		if (sensorIdSet.size() != 0) {
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

	}

	public List<PersonState> getPersonStateList() {
		return personStateList;
	}
}
