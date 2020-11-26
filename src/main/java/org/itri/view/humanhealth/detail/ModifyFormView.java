package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	private long orgPatientId = 0;
	private long newPatientId = 0;
	@Wire("#textboxPatientId")
	private Textbox textboxPatientId;
	@Wire("#selectboxPatient")
	private Selectbox selectboxPatient;
	private ListModelList<DateKeyValueSelectBox> patientModel = new ListModelList<DateKeyValueSelectBox>();

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
	private long oximeterOrgSensorId = 0;
	private long oximeterSensorId = 0;
	@Wire("#textboxOximeterSensorId")
	private Textbox textboxOximeterSensorId;
	@Wire("#selectboxOximeter")
	private Selectbox selectboxOximeter;
	private ListModelList<DateKeyValueSelectBox> oximeterModel = new ListModelList<DateKeyValueSelectBox>();

	// BreathRate component
	@Wire("#textboxBreathRateLow")
	private Textbox textboxBreathRateLow;
	@Wire("#textboxBreathRateHight")
	private Textbox textboxBreathRateHight;
	private long breathRateOrgSensorId = 0;
	private long breathRateSensorId = 0;
	@Wire("#textboxBreathRateSensorId")
	private Textbox textboxBreathRateSensorId;
	@Wire("#selectboxBreathRate")
	private Selectbox selectboxBreathRate;
	private ListModelList<DateKeyValueSelectBox> breathRateModel = new ListModelList<DateKeyValueSelectBox>();

	// BodyTemp component
	@Wire("#textboxBodyTempLow")
	private Textbox textboxBodyTempLow;
	@Wire("#textboxBodyTempHight")
	private Textbox textboxBodyTempHight;
	private long bodyTempOrgSensorId = 0;
	private long bodyTempSensorId = 0;
	@Wire("#textboxBodyTempSensorId")
	private Textbox textboxBodyTempSensorId;
	@Wire("#selectboxTemp")
	private Selectbox selectboxTemp;
	private ListModelList<DateKeyValueSelectBox> bodyTempModel = new ListModelList<DateKeyValueSelectBox>();

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
		setOrgPatientId(formateStr2Long(textboxPatientId.getValue()));
		setOximeterOrgSensorId(formateStr2Long(textboxOximeterSensorId.getValue()));
		setBreathRateOrgSensorId(formateStr2Long(textboxBreathRateSensorId.getValue()));
		setBodyTempOrgSensorId(formateStr2Long(textboxBodyTempSensorId.getValue()));

		// Get selectBox List
		getPatientList();
		getSensorList();
		getMathList();

		// Get org. EWS setting
		getEwsSpecGrid();
	}

	// Get Patient List
	private void getPatientList() {
		int selectedItemIndex = 0;
		boolean flag = false;
		List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		List<PatientInfo> dataList = hqeModify.getPatientList();
		for (PatientInfo patient : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(patient.getPatient().getPatientId(),
					patient.getName());
			patientList.add(item);
			if (!flag && getOrgPatientId() == patient.getPatient().getPatientId()) {
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
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(sensor.getSensorId(), sensor.getSensorName());

			for (Sensor2healthType sensorHealthType : sensor.getSensor2healthTypes()) {
				long healthTypeId = sensorHealthType.getHealthType().getHealthTypeId();
				if (oximeterHealthTypeId == healthTypeId) {
					oximeterList.add(item);
					if (oximeterIndex == 0 && getOximeterOrgSensorId() == item.getValue()) {
						oximeterIndex = oximeterList.size() - 1;
					}
				} else if (bodyTempHealthTypeId == healthTypeId) {
					tempList.add(item);
					if (bodyTempIndex == 0 && getBodyTempOrgSensorId() == item.getValue()) {
						bodyTempIndex = tempList.size() - 1;
					}

				} else if (breathHealthTypeId == healthTypeId) {
					breathRateList.add(item);
					if (breathRateIndex == 0 && getBreathRateOrgSensorId() == item.getValue()) {
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
		List<NewsWarningCondition> ewsSpecList = hqeModify.getNewsWarningConditionList(getOrgPatientId());
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
		System.out.println("removeEwsSpec");
		System.out.println(item.getValue());
		getEwsSpecDaoList().remove(item);
	}

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

	@Listen("onClick = #submitButton")
	public void submit() {
		// Get Selected Item
		setNewPatientId(patientModel.getSelection().stream().findFirst().get().getValue());
		setOximeterSensorId(oximeterModel.getSelection().stream().findFirst().get().getValue());
		setBreathRateSensorId(breathRateModel.getSelection().stream().findFirst().get().getValue());
		setBodyTempSensorId(bodyTempModel.getSelection().stream().findFirst().get().getValue());

		// Check this room, if it is changed.
		boolean changePatientFlag = false;
		boolean changeOximeterFlag = false;
		boolean changeBodyTempFlag = false;
		boolean changeBreathRateFlag = false;
		List<Combination> exitCombinationList = hqeModify.getCombinationByRoomId(getRoomId());
		submitCheckChange(changePatientFlag, changeOximeterFlag, changeBodyTempFlag, changeBreathRateFlag,
				exitCombinationList);

		// Collection modify data
		Date now = new Date();
		List<Combination> modifyCombinationList = new ArrayList<Combination>();
		List<Combination> newCombinationList = new ArrayList<Combination>();
		// Patient Change
		if (changePatientFlag) {
			for (Combination combination : exitCombinationList) {
				// Collection Modify
				modifyCombinationList.add(combination);

				// Create new data
				Combination item = createCombinationByPatientChange(combination, getNewPatientId(),
						getOximeterSensorId(), getBreathRateSensorId(), getBodyTempSensorId(), now);
				newCombinationList.add(item);
			}
		} else if (changeOximeterFlag || changeBodyTempFlag || changeBreathRateFlag) {
			for (Combination combination : exitCombinationList) {
				for (Sensor2healthType sensor2healthType : combination.getSensor().getSensor2healthTypes()) {
					long healthTypeId = sensor2healthType.getHealthType().getHealthTypeId();

					if (changeOximeterFlag
							&& (heartRateHealthTypeId == healthTypeId || oximeterHealthTypeId == healthTypeId)) {
						// Collection Modify
						modifyCombinationList.add(combination);

						// Create new data
						Combination item = createCombinationBySensorChange(combination, getOximeterSensorId(), now);
						newCombinationList.add(item);

						changeOximeterFlag = false;

					} else if (changeBodyTempFlag && bodyTempHealthTypeId == healthTypeId) {
						// Collection Modify
						modifyCombinationList.add(combination);

						// Create new data
						Combination item = createCombinationBySensorChange(combination, getBodyTempSensorId(), now);
						newCombinationList.add(item);

						changeBodyTempFlag = false;

					} else if (changeBreathRateFlag && breathHealthTypeId == healthTypeId) {
						// Collection Modify
						modifyCombinationList.add(combination);

						// Create new data
						Combination item = createCombinationBySensorChange(combination, getBreathRateSensorId(), now);
						newCombinationList.add(item);

						changeBreathRateFlag = false;
					}
				}
			}
		}
		// Enable for DB Combination
		for (Combination item : modifyCombinationList) {
			hqeModify.updateCombination(item);
		}
		for (Combination item : newCombinationList) {
			hqeModify.createCombination(item);
		}

		// Sensor Threshold
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
			System.out.println(item.getSensor().getSensorId() + "/ " + item.getHealthType().getHealthTypeId());
			hqeModify.deleteSensorThreshold(item.getSensor().getSensorId(), item.getHealthType().getHealthTypeId());
			hqeModify.createSensorThreshold(item);
		}

		// EWS Change
		if (isChangEwsFlag() == true) {
			// Delete old setting
			hqeModify.deleteNewsWarningConditionByPatientId(getOrgPatientId());

			// Add new ews setting
			for (EwsSpecDao deleteEwsSpec : getEwsSpecDaoList()) {
				Patient patient = new Patient();
				patient.setPatientId(getNewPatientId());
				deleteEwsSpec.setPatient(patient);
				hqeModify.createNewsWarningCondition(deleteEwsSpec.getNewsWarningCondition());
			}
		}

		modifyWin.detach();
		BindUtils.postGlobalCommand(null, null, "refreshPatientInfo", null);
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

	private void submitCheckChange(boolean changePatientFlag, boolean changeOximeterFlag, boolean changeBodyTempFlag,
			boolean changeBreathRateFlag, List<Combination> exitCombinationList) {
		for (Combination combination : exitCombinationList) {
			if (getOrgPatientId() != getNewPatientId()) {
				changePatientFlag = true;
				break;
			}
			for (Sensor2healthType sensor2healthType : combination.getSensor().getSensor2healthTypes()) {
				long healthTypeId = sensor2healthType.getHealthType().getHealthTypeId();

				if ((heartRateHealthTypeId == healthTypeId || oximeterHealthTypeId == healthTypeId)
						&& getOximeterOrgSensorId() != getOximeterSensorId()) {
					changeOximeterFlag = true;
					break;
				} else if (bodyTempHealthTypeId == healthTypeId && getBodyTempOrgSensorId() != getBodyTempSensorId()) {
					changeBodyTempFlag = true;
					break;
				} else if (breathHealthTypeId == healthTypeId
						&& getBreathRateOrgSensorId() != getBreathRateSensorId()) {
					changeBreathRateFlag = true;
					break;
				}
			}
		}
	}

	private Combination createCombinationBySensorChange(Combination combination, long sensorId, Date strDate) {
		Combination item = new Combination();
		item.setRoom(combination.getRoom());
		item.setPatient(combination.getPatient());
		item.setStartTime(strDate);

		Sensor sensor = new Sensor();
		sensor.setSensorId(sensorId);
		item.setSensor(sensor);
		return item;
	}

	private Combination createCombinationByPatientChange(Combination combination, long patientId, long oximeterSensorId,
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

	@Listen("onClick = #closeButton")
	public void close() {
		modifyWin.detach();
	}

	private static long formateStr2Long(String str) {
		if (str.isEmpty() || str == null) {
			return 0;
		}
		return Long.parseLong(str);
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

	public long getOrgPatientId() {
		return orgPatientId;
	}

	public void setOrgPatientId(long orgPatientId) {
		this.orgPatientId = orgPatientId;
	}

	public long getNewPatientId() {
		return newPatientId;
	}

	public void setNewPatientId(long newPatientId) {
		this.newPatientId = newPatientId;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
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

	public long getOximeterOrgSensorId() {
		return oximeterOrgSensorId;
	}

	public void setOximeterOrgSensorId(long oximeterOrgSensorId) {
		this.oximeterOrgSensorId = oximeterOrgSensorId;
	}

	public long getBreathRateOrgSensorId() {
		return breathRateOrgSensorId;
	}

	public void setBreathRateOrgSensorId(long breathRateOrgSensorId) {
		this.breathRateOrgSensorId = breathRateOrgSensorId;
	}

	public long getBodyTempOrgSensorId() {
		return bodyTempOrgSensorId;
	}

	public void setBodyTempOrgSensorId(long bodyTempOrgSensorId) {
		this.bodyTempOrgSensorId = bodyTempOrgSensorId;
	}
}
