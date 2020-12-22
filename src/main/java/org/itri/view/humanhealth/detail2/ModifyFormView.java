package org.itri.view.humanhealth.detail2;

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
import org.itri.view.humanhealth.personal.chart.dao.PersonState;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ModifyFormView extends GenericForwardComposer {
	private static final long serialVersionUID = 1L;
	private ModifyDaoHibernateImpl hqeModify;

	// Get org setting value
	private long roomId = 0;
	private long patientIdOrg = 0;
	private long oximeterSensorIdOrg = 0;
	private long breathRateSensorIdOrg = 0;
	private long bodyTempSensorIdOrg = 0;
	private List<EwsSpecDao> ewsSpecDaoList = new ArrayList<EwsSpecDao>();

	// =============================================================================
	// Patient component
	private long patientIdNew = 0;
	private ListModel<DateKeyValueSelectBox> patientModelList = new ListModelList<DateKeyValueSelectBox>();
	private boolean changePatientFlag = false;

	// HeartRate component
	private String heartRateLow = new String();
	private String heartRateHight = new String();

	// Oximeter component
	private String oximeterLow = new String();
	private String oximeterHight = new String();
	private long oximeterSensorId = 0;
	private ListModel<DateKeyValueSelectBox> oximeterSensorModelList = new ListModelList<DateKeyValueSelectBox>();
	private boolean changeOximeterFlag = false;

	// BreathRate component
	private String breathRateLow = new String();
	private String breathRateHight = new String();
	private long breathRateSensorId = 0;
	private ListModel<DateKeyValueSelectBox> breathRateSensorModelList = new ListModelList<DateKeyValueSelectBox>();
	private boolean changeBreathRateFlag = false;

	// BodyTemp component
	private String bodyTempLow = new String();
	private String bodyTempHight = new String();
	private long bodyTempSensorId = 0;
	private ListModel<DateKeyValueSelectBox> bodyTempSensorModelList = new ListModelList<DateKeyValueSelectBox>();
	private boolean changeBodyTempFlag = false;

	// EWS component
	private Integer ewsPoint = 0;
	private Integer ewsTime = 0;
	private ListModel<DateKeyValueSelectBox> mathOperatorModelList = new ListModelList<DateKeyValueSelectBox>();

	@Wire("#ewsGrid")
	private Grid ewsGrid;
	private boolean changEwsFlag = false;

	private static long heartRateHealthTypeId = 1;
	private static long oximeterHealthTypeId = 2;
	private static long bodyTempHealthTypeId = 3;
	private static long breathHealthTypeId = 4;

	Window modifyWin;

	@Init
	@NotifyChange({ "ewsSpecDaoList" })
	public void init(@ExecutionArgParam("orderItems") PersonState item,
			@ContextParam(ContextType.VIEW) Component view) {

		// Connect DB
		hqeModify = new ModifyDaoHibernateImpl();
		modifyWin = (Window) view;

		// Get parent pass through parameter
		setRoomId(item.getRoomId());
		setPatientIdOrg(item.getPatientId());
		setOximeterSensorIdOrg(item.getOximeterThreshold().getSensorId());
		setBreathRateSensorIdOrg(item.getBreathRateThreshold().getSensorId());
		setBodyTempSensorIdOrg(item.getBodyTempThreshold().getSensorId());

		setHeartRateHight(item.getHeartRateThreshold().getSpecHigh());
		setHeartRateLow(item.getHeartRateThreshold().getSpecLow());

		setOximeterHight(item.getOximeterThreshold().getSpecHigh());
		setOximeterLow(item.getOximeterThreshold().getSpecLow());

		setBreathRateHight(item.getBreathRateThreshold().getSpecHigh());
		setBreathRateLow(item.getBreathRateThreshold().getSpecLow());

		setBodyTempHight(item.getBodyTempThreshold().getSpecHigh());
		setBodyTempLow(item.getBodyTempThreshold().getSpecLow());

		// Get selectBox List
		getPatientSelectList();
		getSensorSelectList();
		getMathSelectList();

		// Get org. EWS setting
		getEwsSpecGrid();
	}

	@Command
	public void close() {
		modifyWin.detach();
	}

	@Command
	@NotifyChange({ "ewsSpecDaoList" })
	public void ewsRemove(@BindingParam("ewsSpecDao") EwsSpecDao item) {
		setChangEwsFlag(true);
		ewsSpecDaoList.remove(item);
	}

	@Command
	@NotifyChange({ "ewsSpecDaoList" })
	public void ewsAdd() {
		// Check required value
		if (getEwsPoint() == null || getEwsPoint() == 0 || getEwsTime() == null || getEwsTime() == 0) {
			Messagebox.show("EWS 分數 /EWS 持續時間  的表格都須填值且不可為0!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			setChangEwsFlag(true);
			Set<DateKeyValueSelectBox> mathOperatorModel = ((ListModelList<DateKeyValueSelectBox>) getMathOperatorModelList())
					.getSelection();
			long mathId = mathOperatorModel.iterator().next().getValue();
			String mathText = mathOperatorModel.iterator().next().getText();

			EwsSpecDao item = new EwsSpecDao();
			NewsMathOperator newsMathOperator = new NewsMathOperator();
			newsMathOperator.setNewsMathOperatorId(mathId);
			item.setNewsMathOperator(newsMathOperator);
			item.setNewsWarningConditionId(mathId);
			item.setNewsWarningThreshold(ewsPoint);
			item.setTimeBeforeWarning(ewsTime * 60);
			item.setValue(setEwsValue(mathText, ewsPoint, ewsTime * 60));
			ewsSpecDaoList.add(item);
		}
	}

	@Command
	public void submit() {
		System.out.println("submit");

		setChangeBodyTempFlag(false);
		setChangeBreathRateFlag(false);
		setChangeOximeterFlag(false);
		setChangePatientFlag(false);

		// Get Selected Item
		setPatientIdNew(((ListModelList<DateKeyValueSelectBox>) getPatientModelList()).getSelection().iterator().next()
				.getValue());
		setOximeterSensorId(((ListModelList<DateKeyValueSelectBox>) getOximeterSensorModelList()).getSelection()
				.iterator().next().getValue());
		setBreathRateSensorId(((ListModelList<DateKeyValueSelectBox>) getBreathRateSensorModelList()).getSelection()
				.iterator().next().getValue());
		setBodyTempSensorId(((ListModelList<DateKeyValueSelectBox>) getBodyTempSensorModelList()).getSelection()
				.iterator().next().getValue());

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
		BindUtils.postGlobalCommand(null, null, "refresHumanChartSet2", null);
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

		SensorThreshold heartRateThreshold = getNewSensorThreshold(getHeartRateLow(), getHeartRateHight(),
				getOximeterSensorId(), 1L);
		SensorThreshold oximeterThreshold = getNewSensorThreshold(getOximeterLow(), getOximeterHight(),
				getOximeterSensorId(), 2L);
		SensorThreshold bodyTempThreshold = getNewSensorThreshold(getBodyTempLow(), getBodyTempHight(),
				getBodyTempSensorId(), 3L);
		SensorThreshold breathRateThreshold = getNewSensorThreshold(getBreathRateLow(), getBreathRateHight(),
				getBreathRateSensorId(), 4L);
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

	// Get Patient List
	private void getPatientSelectList() {
		System.out.println("getPatientSelectList");
		int selectedItemIndex = 0;
		boolean flag = false;
		List<DateKeyValueSelectBox> resp = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		Set<Long> usedPatientIdSet = hqeModify.getUsedPatientIdList();

		List<PatientInfo> dataList = hqeModify.getPatientList(new ArrayList<>(usedPatientIdSet), getPatientIdOrg());
		for (PatientInfo patient : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(patient.getPatient().getPatientId(),
					patient.getName());
			resp.add(item);
			if (!flag && getPatientIdOrg() == patient.getPatient().getPatientId()) {
				selectedItemIndex = resp.size() - 1;
				flag = true;
			}
		}

		// Set Default selected
		ListModel<DateKeyValueSelectBox> temp = new ListModelList<DateKeyValueSelectBox>(resp);
		((ListModelList<DateKeyValueSelectBox>) temp).addToSelection(resp.get(selectedItemIndex));
		setPatientModelList(temp);
	}

	// Get Sensor List
	private void getSensorSelectList() {
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
		ListModel<DateKeyValueSelectBox> oximeterSensorTemp = new ListModelList<DateKeyValueSelectBox>(oximeterList);
		((ListModelList<DateKeyValueSelectBox>) oximeterSensorTemp).addToSelection(oximeterList.get(oximeterIndex));
		setOximeterSensorModelList(oximeterSensorTemp);

		ListModel<DateKeyValueSelectBox> breathRateSensorTemp = new ListModelList<DateKeyValueSelectBox>(
				breathRateList);
		((ListModelList<DateKeyValueSelectBox>) breathRateSensorTemp).addToSelection(breathRateList.get(bodyTempIndex));
		setBreathRateSensorModelList(breathRateSensorTemp);

		ListModel<DateKeyValueSelectBox> bodyTempSensorTemp = new ListModelList<DateKeyValueSelectBox>(tempList);
		((ListModelList<DateKeyValueSelectBox>) bodyTempSensorTemp).addToSelection(tempList.get(bodyTempIndex));
		setBodyTempSensorModelList(bodyTempSensorTemp);

	}

	// Get EWS-Math List
	private void getMathSelectList() {
		List<DateKeyValueSelectBox> resp = new ArrayList<DateKeyValueSelectBox>();
		List<NewsMathOperator> dataList = hqeModify.getNewsMathOperatorList();
		for (NewsMathOperator mathOperator : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(mathOperator.getNewsMathOperatorId(),
					mathOperator.getOperator());
			resp.add(item);
		}

		// Set Default selected
		ListModel<DateKeyValueSelectBox> temp = new ListModelList<DateKeyValueSelectBox>(resp);
		((ListModelList<DateKeyValueSelectBox>) temp).addToSelection(resp.get(0));
		setMathOperatorModelList(temp);
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

	public ListModel<DateKeyValueSelectBox> getMathOperatorModelList() {
		return mathOperatorModelList;
	}

	public void setMathOperatorModelList(ListModel<DateKeyValueSelectBox> mathOperatorModelList) {
		this.mathOperatorModelList = mathOperatorModelList;
	}

	public ListModel<DateKeyValueSelectBox> getPatientModelList() {
		return patientModelList;
	}

	public void setPatientModelList(ListModel<DateKeyValueSelectBox> patientModelList) {
		this.patientModelList = patientModelList;
	}

	public ListModel<DateKeyValueSelectBox> getOximeterSensorModelList() {
		return oximeterSensorModelList;
	}

	public void setOximeterSensorModelList(ListModel<DateKeyValueSelectBox> oximeterSensorModelList) {
		this.oximeterSensorModelList = oximeterSensorModelList;
	}

	public ListModel<DateKeyValueSelectBox> getBreathRateSensorModelList() {
		return breathRateSensorModelList;
	}

	public void setBreathRateSensorModelList(ListModel<DateKeyValueSelectBox> breathRateSensorModelList) {
		this.breathRateSensorModelList = breathRateSensorModelList;
	}

	public ListModel<DateKeyValueSelectBox> getBodyTempSensorModelList() {
		return bodyTempSensorModelList;
	}

	public void setBodyTempSensorModelList(ListModel<DateKeyValueSelectBox> bodyTempSensorModelList) {
		this.bodyTempSensorModelList = bodyTempSensorModelList;
	}

	public Integer getEwsPoint() {
		return ewsPoint;
	}

	public void setEwsPoint(Integer ewsPoint) {
		this.ewsPoint = ewsPoint;
	}

	public Integer getEwsTime() {
		return ewsTime;
	}

	public void setEwsTime(Integer ewsTime) {
		this.ewsTime = ewsTime;
	}

	public String getHeartRateLow() {
		return heartRateLow;
	}

	public void setHeartRateLow(String heartRateLow) {
		this.heartRateLow = heartRateLow;
	}

	public String getHeartRateHight() {
		return heartRateHight;
	}

	public void setHeartRateHight(String heartRateHight) {
		this.heartRateHight = heartRateHight;
	}

	public String getOximeterLow() {
		return oximeterLow;
	}

	public void setOximeterLow(String oximeterLow) {
		this.oximeterLow = oximeterLow;
	}

	public String getOximeterHight() {
		return oximeterHight;
	}

	public void setOximeterHight(String oximeterHight) {
		this.oximeterHight = oximeterHight;
	}

	public String getBreathRateLow() {
		return breathRateLow;
	}

	public void setBreathRateLow(String breathRateLow) {
		this.breathRateLow = breathRateLow;
	}

	public String getBreathRateHight() {
		return breathRateHight;
	}

	public void setBreathRateHight(String breathRateHight) {
		this.breathRateHight = breathRateHight;
	}

	public String getBodyTempLow() {
		return bodyTempLow;
	}

	public void setBodyTempLow(String bodyTempLow) {
		this.bodyTempLow = bodyTempLow;
	}

	public String getBodyTempHight() {
		return bodyTempHight;
	}

	public void setBodyTempHight(String bodyTempHight) {
		this.bodyTempHight = bodyTempHight;
	}

}
