package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.itri.view.humanhealth.detail.Imp.ModifyDaoHibernateImpl;
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.HealthType;
import org.itri.view.humanhealth.hibernate.NewsMathOperator;
import org.itri.view.humanhealth.hibernate.NewsWarningCondition;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.hibernate.Sensor2healthType;
import org.itri.view.humanhealth.hibernate.SensorThreshold;
import org.itri.view.humanhealth.hibernate.Users;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.itri.view.humanhealth.personal.chart.dao.EwsSpecDao;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.annotation.Command;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ModifyFormView extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;
	private ModifyDaoHibernateImpl hqeModify;

	@Wire
	private Window modifyWin;

	private long roomId = 0;
	@Wire("#textboxRoomId")
	private Textbox textboxRoomId;

	// Patient component
	private long patientIdOrg = 0;
	private long patientIdNew = 0;
	@Wire("#textboxPatientId")
	private Textbox textboxPatientId;
	@Wire("#selectboxPatient")
	private Selectbox selectboxPatient;
	private ListModelList<DateKeyValueSelectBox> patientModel = new ListModelList<DateKeyValueSelectBox>();
	private boolean changePatientFlag = false;

	// HeartRate component
	@Wire("#textboxHeartRateLow")
	private Textbox textboxHeartRateLow;
	@Wire("#textboxHeartRateHight")
	private Textbox textboxHeartRateHight;

	// Oximeter component
	@Wire("#textboxOximeterLow")
	private Textbox textboxOximeterLow;
	@Wire("#textboxOximeterHight")
	private Textbox textboxOximeterHight;
	private long oximeterSensorIdOrg = 0;
	private long oximeterSensorId = 0;
	@Wire("#textboxOximeterSensorId")
	private Textbox textboxOximeterSensorId;
	@Wire("#selectboxOximeter")
	private Selectbox selectboxOximeter;
	private ListModelList<DateKeyValueSelectBox> oximeterModel = new ListModelList<DateKeyValueSelectBox>();
	private boolean changeOximeterFlag = false;

	// BreathRate component
	@Wire("#textboxBreathRateLow")
	private Textbox textboxBreathRateLow;
	@Wire("#textboxBreathRateHight")
	private Textbox textboxBreathRateHight;
	private long breathRateSensorIdOrg = 0;
	private long breathRateSensorId = 0;
	@Wire("#textboxBreathRateSensorId")
	private Textbox textboxBreathRateSensorId;
	@Wire("#selectboxBreathRate")
	private Selectbox selectboxBreathRate;
	private ListModelList<DateKeyValueSelectBox> breathRateModel = new ListModelList<DateKeyValueSelectBox>();
	private boolean changeBreathRateFlag = false;

	// BodyTemp component
	@Wire("#textboxBodyTempLow")
	private Textbox textboxBodyTempLow;
	@Wire("#textboxBodyTempHight")
	private Textbox textboxBodyTempHight;
	private long bodyTempSensorIdOrg = 0;
	private long bodyTempSensorId = 0;
	@Wire("#textboxBodyTempSensorId")
	private Textbox textboxBodyTempSensorId;
	@Wire("#selectboxTemp")
	private Selectbox selectboxTemp;
	private ListModelList<DateKeyValueSelectBox> bodyTempModel = new ListModelList<DateKeyValueSelectBox>();
	private boolean changeBodyTempFlag = false;

	// EWS component
	@Wire("#spinnerEwsPoint")
	private Spinner spinnerEwsPoint;
	@Wire("#selectboxMathOperator")
	private Selectbox selectboxMathOperator;
	private ListModelList<DateKeyValueSelectBox> mathOperatorModel = new ListModelList<DateKeyValueSelectBox>();
	@Wire("#spinnerEwsTime")
	private Spinner spinnerEwsTime;

	@Wire("#ewsGrid")
	private Grid ewsGrid;
	private List<EwsSpecDao> ewsSpecDaoList = new ArrayList<EwsSpecDao>();
	private boolean changEwsFlag = false;

	private static long heartRateHealthTypeId = 1;
	private static long oximeterHealthTypeId = 2;
	private static long bodyTempHealthTypeId = 3;
	private static long breathHealthTypeId = 4;

	@NotifyChange({ "ewsSpecDaoList" })
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// Connect DB
		hqeModify = new ModifyDaoHibernateImpl();

		// Get parent pass through parameter
		setRoomId(Long.parseLong(textboxRoomId.getValue()));
		setPatientIdOrg(formateStr2Long(textboxPatientId.getValue()));
		setOximeterSensorIdOrg(formateStr2Long(textboxOximeterSensorId.getValue()));
		setBreathRateSensorIdOrg(formateStr2Long(textboxBreathRateSensorId.getValue()));
		setBodyTempSensorIdOrg(formateStr2Long(textboxBodyTempSensorId.getValue()));

		// Get selectBox List
		getPatientList();
		getSensorList();
		getMathList();

		// Get org. EWS setting
		getEwsSpecGrid();
	}

	@Listen("onClick = #ewsAddbtn")
	public void ewsAdd() {
		// Check required value
		if (spinnerEwsPoint.getValue() == null || spinnerEwsTime.getValue() == null) {
			Messagebox.show("EWS 分數 /EWS 持續時間  的表格都須填值!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			setChangEwsFlag(true);
			int ewsPoint = spinnerEwsPoint.getValue();
			int ewsTime = spinnerEwsTime.getValue();

			long mathId = mathOperatorModel.get(selectboxMathOperator.getSelectedIndex()).getValue();
			String mathText = mathOperatorModel.get(selectboxMathOperator.getSelectedIndex()).getText();

			List<EwsSpecDao> tempEwsSpecDaoList = getEwsSpecDaoList();
			EwsSpecDao item = new EwsSpecDao();
			NewsMathOperator newsMathOperator = new NewsMathOperator();
			newsMathOperator.setNewsMathOperatorId(mathId);
			item.setNewsMathOperator(newsMathOperator);
			item.setNewsWarningConditionId(mathId);
			item.setNewsWarningThreshold(ewsPoint);
			item.setTimeBeforeWarning(ewsTime * 60);
			item.setValue(setEwsValue(mathText, ewsPoint, ewsTime * 60));
			tempEwsSpecDaoList.add(item);

			setEwsSpecDaoList(tempEwsSpecDaoList);
			System.out.println("ewsSpecDaoList size: " + getEwsSpecDaoList().size());
			ListModelList ewsModel = new ListModelList(getEwsSpecDaoList());
			ewsGrid.setModel(ewsModel);
		}
	}

	@Command
	public void removeEwsSpec(@BindingParam("ewsSpecDao") EwsSpecDao item) {
		getEwsSpecDaoList().remove(item);
	}

	@Listen("onClick = #submitButton")
	public void submit() {

		setChangeBodyTempFlag(false);
		setChangeBreathRateFlag(false);
		setChangeOximeterFlag(false);
		setChangePatientFlag(false);

		// Get Selected Item
		setPatientIdNew(patientModel.getSelection().stream().findFirst().get().getValue());
		setOximeterSensorId(oximeterModel.getSelection().stream().findFirst().get().getValue());
		setBreathRateSensorId(breathRateModel.getSelection().stream().findFirst().get().getValue());
		setBodyTempSensorId(bodyTempModel.getSelection().stream().findFirst().get().getValue());

		// update Combination
		updateCombination();

		// Sensor Threshold
		updateThreshold();

		// EWS Change
		if (isChangEwsFlag() == true) {
			// Delete old setting
			hqeModify.deleteNewsWarningConditionByPatientId(getPatientIdOrg());

			// Add new ews setting
			for (EwsSpecDao deleteEwsSpec : getEwsSpecDaoList()) {
				Patient patient = new Patient();
				patient.setPatientId(getPatientIdNew());
				deleteEwsSpec.setPatient(patient);
				hqeModify.createNewsWarningCondition(deleteEwsSpec.getNewsWarningCondition());
			}
		}
		modifyWin.detach();
		BindUtils.postGlobalCommand(null, null, "refresHumanChartSet", null);
	}

	@Listen("onClick = #closeButton")
	public void close() {
		modifyWin.detach();
	}

	private void updateCombination() {
		// Get exit Combination
		List<Combination> exitCombinationList = hqeModify.getCombinationByRoomId(getRoomId());
		System.out.println("exitCombinationList size: " + exitCombinationList.size());

		// Check this room, if it is changed.
		submitCheckChange(exitCombinationList);

		// Collection modify data
		List<Combination> closeCombinationList = new ArrayList<Combination>();
		List<Combination> newCombinationList = new ArrayList<Combination>();
		if (exitCombinationList.size() < 2) {
			createCollectionItem(exitCombinationList, closeCombinationList, newCombinationList);
		} else {
			submitCollectionItem(exitCombinationList, closeCombinationList, newCombinationList);
		}

		// Enable for DB Combination
		for (Combination item : closeCombinationList) {
			hqeModify.updateCombinationEndTime(item);

		}
		for (Combination item : newCombinationList) {
			hqeModify.createCombination(item);
		}
	}

	private void submitCheckChange(List<Combination> exitCombinationList) {
		for (Combination combination : exitCombinationList) {

			if (getPatientIdOrg() != getPatientIdNew()) {
				setChangePatientFlag(true);
				break;
			}

			if (combination.getSensor() == null) {
				if (getOximeterSensorId() != 0)
					setChangeOximeterFlag(true);
				if (getBodyTempSensorId() != 0)
					setChangeBodyTempFlag(true);
				if (getBreathRateSensorId() != 0)
					setChangeBreathRateFlag(true);
				break;
			} else {
				if (getOximeterSensorIdOrg() != getOximeterSensorId())
					setChangeOximeterFlag(true);
				if (getBodyTempSensorIdOrg() != getBodyTempSensorId())
					setChangeBodyTempFlag(true);
				if (getBreathRateSensorIdOrg() != getBreathRateSensorId())
					setChangeBreathRateFlag(true);
			}
		}
	}

	private void createCollectionItem(List<Combination> exitCombinationList, List<Combination> closeCombinationList,
			List<Combination> newCombinationList) {
		// Remove exitCombination by update end_time
		Combination exitCombination = exitCombinationList.get(0);
		closeCombinationList.add(exitCombination);

		// Create init Combination for three sensor
		Combination sensorOximeter = initCombination(exitCombination, getOximeterSensorId());
		Combination sensorBreathRate = initCombination(exitCombination, getBreathRateSensorId());
		Combination sensorBodyTemp = initCombination(exitCombination, getBodyTempSensorId());
		newCombinationList.add(sensorOximeter);
		newCombinationList.add(sensorBreathRate);
		newCombinationList.add(sensorBodyTemp);
	}

	private void submitCollectionItem(List<Combination> exitCombinationList, List<Combination> closeCombinationList,
			List<Combination> newCombinationList) {
		Date now = new Date();

		// Patient Change
		if (isChangePatientFlag()) {
			for (Combination combination : exitCombinationList) {
				// Collection Modify
				closeCombinationList.add(combination);

				// Create new data
				Combination item = modifyCombinationByPatientChange(combination, getPatientIdNew(),
						getOximeterSensorId(), getBreathRateSensorId(), getBodyTempSensorId(), now);
				newCombinationList.add(item);
			}
		}
		// Sensor Change
		else if (isChangeOximeterFlag() || isChangeBodyTempFlag() || isChangeBreathRateFlag()) {
			for (Combination combination : exitCombinationList) {
				for (Sensor2healthType sensor2healthType : combination.getSensor().getSensor2healthTypes()) {
					long healthTypeId = sensor2healthType.getHealthType().getHealthTypeId();

					if (isChangeOximeterFlag()
							&& (heartRateHealthTypeId == healthTypeId || oximeterHealthTypeId == healthTypeId)) {
						// Collection Modify
						closeCombinationList.add(combination);

						// Create new data
						Combination item = modifyCombinationBySensorChange(combination, getOximeterSensorId(), now);
						newCombinationList.add(item);

						setChangeOximeterFlag(false);

					} else if (isChangeBodyTempFlag() && bodyTempHealthTypeId == healthTypeId) {
						// Collection Modify
						closeCombinationList.add(combination);

						// Create new data
						Combination item = modifyCombinationBySensorChange(combination, getBodyTempSensorId(), now);
						newCombinationList.add(item);

						setChangeBodyTempFlag(false);

					} else if (isChangeBreathRateFlag() && breathHealthTypeId == healthTypeId) {
						// Collection Modify
						closeCombinationList.add(combination);

						// Create new data
						Combination item = modifyCombinationBySensorChange(combination, getBreathRateSensorId(), now);
						newCombinationList.add(item);

						setChangeBreathRateFlag(false);
					}
				}
			}
		}
	}

	private Combination initCombination(Combination exitCombination, long sensorId) {
		Date now = new Date();

		Combination resp = new Combination();
		resp.setRoom(exitCombination.getRoom());
		resp.setPatient(exitCombination.getPatient());

		Sensor sensor = new Sensor();
		sensor.setSensorId(sensorId);
		resp.setSensor(sensor);

		resp.setStartTime(now);

		return resp;
	}

	private Combination modifyCombinationBySensorChange(Combination combination, long sensorId, Date strDate) {
		Combination item = new Combination();
		item.setRoom(combination.getRoom());
		item.setPatient(combination.getPatient());
		item.setStartTime(strDate);

		Sensor sensor = new Sensor();
		sensor.setSensorId(sensorId);
		item.setSensor(sensor);
		return item;
	}

	private Combination modifyCombinationByPatientChange(Combination combination, long patientId, long oximeterSensorId,
			long breathRateSensorId, long bodyTempSensorId, Date strDate) {
		Combination item = new Combination();
		item.setRoom(combination.getRoom());

		Patient patient = new Patient();
		patient.setPatientId(patientId);
		item.setPatient(patient);
		item.setStartTime(strDate);

		for (Sensor2healthType sensor2healthType : combination.getSensor().getSensor2healthTypes()) {
			long healthTypeId = sensor2healthType.getHealthType().getHealthTypeId();
			Sensor sensor = new Sensor();
			if (heartRateHealthTypeId == healthTypeId || oximeterHealthTypeId == healthTypeId) {
				sensor.setSensorId(oximeterSensorId);
			} else if (bodyTempHealthTypeId == healthTypeId) {
				sensor.setSensorId(bodyTempSensorId);
			} else if (breathHealthTypeId == healthTypeId) {
				sensor.setSensorId(breathRateSensorId);
			}
			item.setSensor(sensor);
		}
		return item;
	}

	private void updateThreshold() {
		List<SensorThreshold> thresholdList = new ArrayList<SensorThreshold>();

		SensorThreshold heartRateThreshold = getNewSensorThreshold(textboxHeartRateLow.getValue(),
				textboxHeartRateHight.getValue(), getOximeterSensorId(), 1L);
		SensorThreshold oximeterThreshold = getNewSensorThreshold(textboxOximeterLow.getValue(),
				textboxOximeterHight.getValue(), getOximeterSensorId(), 2L);
		SensorThreshold bodyTempThreshold = getNewSensorThreshold(textboxBodyTempLow.getValue(),
				textboxBodyTempHight.getValue(), getBodyTempSensorId(), 3L);
		SensorThreshold breathRateThreshold = getNewSensorThreshold(textboxBreathRateLow.getValue(),
				textboxBreathRateHight.getValue(), getBreathRateSensorId(), 4L);
		thresholdList.add(heartRateThreshold);
		thresholdList.add(oximeterThreshold);
		thresholdList.add(bodyTempThreshold);
		thresholdList.add(breathRateThreshold);

		// Enable for DB SensorThreshold
		for (SensorThreshold item : thresholdList) {
			hqeModify.deleteSensorThreshold(item.getSensor().getSensorId(), item.getHealthType().getHealthTypeId());
			hqeModify.createSensorThreshold(item);
		}
	}

	private SensorThreshold getNewSensorThreshold(String criticalLow, String criticalHigh, long sensorId,
			long healthTypeId) {
		SensorThreshold item = new SensorThreshold();

		Sensor sensor = new Sensor();
		sensor.setSensorId(sensorId);
		item.setSensor(sensor);
		HealthType healthType = new HealthType();
		healthType.setHealthTypeId(healthTypeId);
		item.setHealthType(healthType);
		Users users = new Users();
		users.setUsersId(1);
		item.setUsers(users);

		item.setCriticalHigh(criticalHigh);
		item.setCriticalLow(criticalLow);
		item.setLastUpdated(new Date());
		item.setTimeCreated(new Date());
		item.setDurationTimes("0");

//		item.setSensorThresholdId(long);
//		setDataName(String)
//		setIsDeleted(boolean)

		return item;
	}

	// Get Patient List
	private void getPatientList() {
		int selectedItemIndex = 0;
		boolean flag = false;
		List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		Set<Long> usedPatientIdSet = hqeModify.getUsedPatientIdList();

		List<PatientInfo> dataList = hqeModify.getPatientList(new ArrayList<>(usedPatientIdSet), getPatientIdOrg());
		for (PatientInfo patient : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(patient.getPatient().getPatientId(),
					patient.getName());
			patientList.add(item);
			if (!flag && getPatientIdOrg() == patient.getPatient().getPatientId()) {
				selectedItemIndex = patientList.size() - 1;
				flag = true;
			}
		}

		// Set Default selected
		patientModel = new ListModelList(patientList);
		patientModel.addToSelection(patientModel.get(selectedItemIndex));
		selectboxPatient.setModel(patientModel);
	}

	// Get Sensor List
	private void getSensorList() {
		int oximeterIndex = 0;
		List<DateKeyValueSelectBox> oximeterList = new ArrayList<DateKeyValueSelectBox>();

		int breathRateIndex = 0;
		List<DateKeyValueSelectBox> breathRateList = new ArrayList<DateKeyValueSelectBox>();

		int bodyTempIndex = 0;
		List<DateKeyValueSelectBox> tempList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		List<Sensor> SensorList = hqeModify.getSensorListBySensorTypeId();
		for (Sensor sensor : SensorList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(sensor.getSensorId(), sensor.getSensorNameChinese());

			for (Sensor2healthType sensorHealthType : sensor.getSensor2healthTypes()) {
				long healthTypeId = sensorHealthType.getHealthType().getHealthTypeId();
				if (oximeterHealthTypeId == healthTypeId) {
					oximeterList.add(item);
					if (oximeterIndex == 0 && getOximeterSensorIdOrg() == item.getValue()) {
						oximeterIndex = oximeterList.size() - 1;
					}
				} else if (bodyTempHealthTypeId == healthTypeId) {
					tempList.add(item);
					if (bodyTempIndex == 0 && getBodyTempSensorIdOrg() == item.getValue()) {
						bodyTempIndex = tempList.size() - 1;
					}

				} else if (breathHealthTypeId == healthTypeId) {
					breathRateList.add(item);
					if (breathRateIndex == 0 && getBreathRateSensorIdOrg() == item.getValue()) {
						breathRateIndex = breathRateList.size() - 1;
					}
				}
			}
		}

		// Set Default selected
		oximeterModel = new ListModelList(oximeterList);
		oximeterModel.addToSelection(oximeterModel.get(oximeterIndex));
		selectboxOximeter.setModel(oximeterModel);

		breathRateModel = new ListModelList(breathRateList);
		breathRateModel.addToSelection(breathRateModel.get(breathRateIndex));
		selectboxBreathRate.setModel(breathRateModel);

		bodyTempModel = new ListModelList(tempList);
		bodyTempModel.addToSelection(bodyTempModel.get(bodyTempIndex));
		selectboxTemp.setModel(bodyTempModel);
	}

	// Get EWS-Math List
	private void getMathList() {
		List<DateKeyValueSelectBox> mathOperatorList = new ArrayList<DateKeyValueSelectBox>();
		List<NewsMathOperator> dataList = hqeModify.getNewsMathOperatorList();
		for (NewsMathOperator mathOperator : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(mathOperator.getNewsMathOperatorId(),
					mathOperator.getOperator());
			mathOperatorList.add(item);
		}

		// Set Default selected
		mathOperatorModel = new ListModelList(mathOperatorList);
		mathOperatorModel.addToSelection(mathOperatorModel.get(0));
		selectboxMathOperator.setModel(mathOperatorModel);
	}

	// Get default EWS-Spec.-Grids
	private void getEwsSpecGrid() {
		List<EwsSpecDao> tempEwsSpecDaoList = new ArrayList<EwsSpecDao>();
		List<NewsWarningCondition> ewsSpecList = hqeModify.getNewsWarningConditionList(getPatientIdOrg());
		for (NewsWarningCondition ewsSpec : ewsSpecList) {
			EwsSpecDao item = new EwsSpecDao(ewsSpec);
			item.setValue(setEwsValue(ewsSpec.getNewsMathOperator().getOperator(), ewsSpec.getNewsWarningThreshold(),
					ewsSpec.getTimeBeforeWarning()));
			tempEwsSpecDaoList.add(item);
		}
		setEwsSpecDaoList(tempEwsSpecDaoList);
		System.out.println("ewsSpecDaoList size: " + getEwsSpecDaoList().size());
		ListModelList ewsModel = new ListModelList(getEwsSpecDaoList());
		ewsGrid.setModel(ewsModel);
	}

	// Print EWS Spec. datagrid show
	private String setEwsValue(String mathText, int ewsPoint, int ewsTime) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("EWS ");
		sbf.append(mathText);
		sbf.append(" ");
		sbf.append(ewsPoint);
		sbf.append(" / ");
		sbf.append(ewsTime);
		sbf.append(" sec");
		return sbf.toString();
	}

	private static long formateStr2Long(String str) {
		if (str.isEmpty() || str == null) {
			return 0;
		}
		return Long.parseLong(str);
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public long getOximeterSensorIdOrg() {
		return oximeterSensorIdOrg;
	}

	public void setOximeterSensorIdOrg(long oximeterSensorIdOrg) {
		this.oximeterSensorIdOrg = oximeterSensorIdOrg;
	}

	public long getBreathRateSensorIdOrg() {
		return breathRateSensorIdOrg;
	}

	public void setBreathRateSensorIdOrg(long breathRateSensorIdOrg) {
		this.breathRateSensorIdOrg = breathRateSensorIdOrg;
	}

	public long getBodyTempSensorIdOrg() {
		return bodyTempSensorIdOrg;
	}

	public void setBodyTempSensorIdOrg(long bodyTempSensorIdOrg) {
		this.bodyTempSensorIdOrg = bodyTempSensorIdOrg;
	}

	public long getPatientIdOrg() {
		return patientIdOrg;
	}

	public void setPatientIdOrg(long patientIdOrg) {
		this.patientIdOrg = patientIdOrg;
	}

	public List<EwsSpecDao> getEwsSpecDaoList() {
		return ewsSpecDaoList;
	}

	public void setEwsSpecDaoList(List<EwsSpecDao> ewsSpecDaoList) {
		this.ewsSpecDaoList = ewsSpecDaoList;
	}

	public boolean isChangEwsFlag() {
		return changEwsFlag;
	}

	public void setChangEwsFlag(boolean changEwsFlag) {
		this.changEwsFlag = changEwsFlag;
	}

	public long getOximeterSensorId() {
		return oximeterSensorId;
	}

	public void setOximeterSensorId(long oximeterSensorId) {
		this.oximeterSensorId = oximeterSensorId;
	}

	public long getBreathRateSensorId() {
		return breathRateSensorId;
	}

	public void setBreathRateSensorId(long breathRateSensorId) {
		this.breathRateSensorId = breathRateSensorId;
	}

	public long getBodyTempSensorId() {
		return bodyTempSensorId;
	}

	public void setBodyTempSensorId(long bodyTempSensorId) {
		this.bodyTempSensorId = bodyTempSensorId;
	}

	public long getPatientIdNew() {
		return patientIdNew;
	}

	public void setPatientIdNew(long patientIdNew) {
		this.patientIdNew = patientIdNew;
	}

	public boolean isChangePatientFlag() {
		return changePatientFlag;
	}

	public void setChangePatientFlag(boolean changePatientFlag) {
		this.changePatientFlag = changePatientFlag;
	}

	public boolean isChangeOximeterFlag() {
		return changeOximeterFlag;
	}

	public void setChangeOximeterFlag(boolean changeOximeterFlag) {
		this.changeOximeterFlag = changeOximeterFlag;
	}

	public boolean isChangeBreathRateFlag() {
		return changeBreathRateFlag;
	}

	public void setChangeBreathRateFlag(boolean changeBreathRateFlag) {
		this.changeBreathRateFlag = changeBreathRateFlag;
	}

	public boolean isChangeBodyTempFlag() {
		return changeBodyTempFlag;
	}

	public void setChangeBodyTempFlag(boolean changeBodyTempFlag) {
		this.changeBodyTempFlag = changeBodyTempFlag;
	}

}
