package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.itri.view.humanhealth.detail.Imp.ModifyDaoHibernateImpl;
import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.NewsMathOperator;
import org.itri.view.humanhealth.hibernate.NewsWarningCondition;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.hibernate.Sensor2healthType;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.itri.view.humanhealth.personal.chart.dao.EwsSpecDao;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Grid;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ModifyFormView extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;

	private ModifyDaoHibernateImpl hqeModify;

	private long patientId = 0;
	private long oximeterDefaultSensorId = 0;
	private long breathRateDefaultSensorId = 0;
	private long bodyTempDefaultSensorId = 0;

	private List<EwsSpecDao> ewsSpecDaoList = new ArrayList<EwsSpecDao>();

	@Wire
	private Window modifyWin;

	@Wire("#textboxPatientId")
	private Textbox textboxPatientId;
	@Wire("#selectboxPatient")
	private Selectbox selectboxPatient;
	private ListModelList<DateKeyValueSelectBox> patientModel = new ListModelList<DateKeyValueSelectBox>();

	@Wire("#textboxRoomId")
	private Textbox textboxRoomId;

	@Wire("#textboxOximeter")
	private Textbox textboxOximeter;
	@Wire("#selectboxOximeter")
	private Selectbox selectboxOximeter;
	private ListModelList<DateKeyValueSelectBox> oximeterModel = new ListModelList<DateKeyValueSelectBox>();

	@Wire("#textboxBreathRate")
	private Textbox textboxBreathRate;
	@Wire("#selectboxBreathRate")
	private Selectbox selectboxBreathRate;
	private ListModelList<DateKeyValueSelectBox> breathRateModel = new ListModelList<DateKeyValueSelectBox>();

	@Wire("#textboxBodyTemp")
	private Textbox textboxBodyTemp;
	@Wire("#selectboxTemp")
	private Selectbox selectboxTemp;
	private ListModelList<DateKeyValueSelectBox> bodyTempModel = new ListModelList<DateKeyValueSelectBox>();

	@Wire("#selectboxMathOperator")
	private Selectbox selectboxMathOperator;
	private ListModelList<DateKeyValueSelectBox> mathOperatorModel = new ListModelList<DateKeyValueSelectBox>();
	@Wire("#spinnerEwsPoint")
	private Spinner spinnerEwsPoint;
	@Wire("#spinnerEwsTime")
	private Spinner spinnerEwsTime;

	@Wire("#ewsGrid")
	private Grid ewsGrid;

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
		patientId = Long.parseLong(textboxPatientId.getValue());
		oximeterDefaultSensorId = Long.parseLong(textboxOximeter.getValue());
		breathRateDefaultSensorId = Long.parseLong(textboxBreathRate.getValue());
		bodyTempDefaultSensorId = Long.parseLong(textboxBodyTemp.getValue());
		getEwsSpec();

		// Get selectBox List
		getSelectBoxList();
		getSensorList();
		getNewsMathOperatorList();

	}

	// Get Patient List
	private void getSelectBoxList() {
		int selectedItemIndex = 0;
		boolean flag = false;
		List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		List<PatientInfo> dataList = hqeModify.getPatientList();
		for (PatientInfo patient : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(patient.getPatient().getPatientId(),
					patient.getName());
			patientList.add(item);
			if (!flag && patientId == patient.getPatient().getPatientId()) {
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
					if (oximeterIndex == 0 && oximeterDefaultSensorId == item.getValue()) {
						oximeterIndex = oximeterList.size() - 1;
					}
				} else if (bodyTempHealthTypeId == healthTypeId) {
					tempList.add(item);
					if (bodyTempIndex == 0 && bodyTempDefaultSensorId == item.getValue()) {
						bodyTempIndex = tempList.size() - 1;
					}

				} else if (breathHealthTypeId == healthTypeId) {
					breathRateList.add(item);
					if (breathRateIndex == 0 && breathRateDefaultSensorId == item.getValue()) {
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

	// Get EWS Math Selectbox List
	private void getNewsMathOperatorList() {
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

	// Get default EWS Spec. Grids
	private void getEwsSpec() {
		System.out.println("getEwsSpec");
		Long temp = 4L;
		ewsSpecDaoList = new ArrayList<EwsSpecDao>();
		List<NewsWarningCondition> ewsSpecList = hqeModify.getNewsWarningConditionList(temp);
		for (NewsWarningCondition ewsSpec : ewsSpecList) {
			EwsSpecDao item = new EwsSpecDao();
			item.setNewsWarningConditionId(ewsSpec.getNewsWarningConditionId());
			item.setValue("EWS " + ewsSpec.getNewsMathOperator().getOperator() + " " + ewsSpec.getNewsWarningThreshold()
					+ " / " + ewsSpec.getTimeBeforeWarning() + " sec");

			item.setNewsMathOperator(ewsSpec.getNewsMathOperator());
			item.setNewsWarningThreshold(ewsSpec.getNewsWarningThreshold());
			item.setTimeBeforeWarning(ewsSpec.getTimeBeforeWarning());
			ewsSpecDaoList.add(item);
		}

//		ListModelList ewsModel = new ListModelList(ewsSpecDaoList);
//		ewsModel.addToSelection(ewsModel.get(0));
//		ewsGrid.setModel(ewsModel);

	}

	@Listen("onClick = #ewsAddbtn")
	public void ewsAdd() {
		// Check required value
		if (spinnerEwsPoint.getValue() == null || spinnerEwsTime.getValue() == null) {
			Messagebox.show("EWS 分數 /EWS 持續時間  的表格都須填值!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			int ewsPoint = spinnerEwsPoint.getValue();
			int ewsTime = spinnerEwsTime.getValue();

			long mathId = mathOperatorModel.get(selectboxMathOperator.getSelectedIndex()).getValue();
			String mathText = mathOperatorModel.get(selectboxMathOperator.getSelectedIndex()).getText();
			EwsSpecDao item = new EwsSpecDao();
			item.setValue("EWS " + mathText + " " + ewsPoint + " / " + ewsTime + " min");
			item.setNewsWarningConditionId(mathId);
			item.setNewsWarningThreshold(ewsPoint);
			item.setTimeBeforeWarning(ewsTime);
			NewsMathOperator newsMathOperator = new NewsMathOperator();
			newsMathOperator.setNewsMathOperatorId(mathId);
			item.setNewsMathOperator(newsMathOperator);
		}
	}

	@Listen("onClick = #closeButton")
	public void close() {
		modifyWin.detach();
	}

	@Listen("onClick = #submitButton")
	public void submit() {
		// Get Selected Item
		DateKeyValueSelectBox patientItem = patientModel.getSelection().stream().findFirst().get();
		DateKeyValueSelectBox oximeterItem = oximeterModel.getSelection().stream().findFirst().get();
		DateKeyValueSelectBox breathRateItem = breathRateModel.getSelection().stream().findFirst().get();
		DateKeyValueSelectBox bodyTempItem = bodyTempModel.getSelection().stream().findFirst().get();

		// Check this room, if it's change
		boolean changePatientFlag = false;
		boolean changeOximeterFlag = false;
		boolean changeBodyTempFlag = false;
		boolean changeBreathRateFlag = false;
		String roomIdStr = textboxRoomId.getValue();
		Long roomId = Long.parseLong(roomIdStr);
		List<Combination> exitCombinationList = hqeModify.getCombinationByRoomId(roomId);
		for (Combination combination : exitCombinationList) {
			if (patientId != patientItem.getValue()) {
				changePatientFlag = true;
				break;
			}
			for (Sensor2healthType sensor2healthType : combination.getSensor().getSensor2healthTypes()) {
				long healthTypeId = sensor2healthType.getHealthType().getHealthTypeId();

				if ((heartRateHealthTypeId == healthTypeId || oximeterHealthTypeId == healthTypeId)
						&& oximeterDefaultSensorId != oximeterItem.getValue()) {
					changeOximeterFlag = true;
					break;
				} else if (bodyTempHealthTypeId == healthTypeId && bodyTempDefaultSensorId != bodyTempItem.getValue()) {
					changeBodyTempFlag = true;
					break;
				} else if (breathHealthTypeId == healthTypeId
						&& breathRateDefaultSensorId != breathRateItem.getValue()) {
					changeBreathRateFlag = true;
					break;
				}
			}
		}

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
				Combination item = createCombinationByPatientChange(combination, patientItem.getValue(),
						oximeterItem.getValue(), breathRateItem.getValue(), bodyTempItem.getValue(), now);
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
						Combination item = createCombinationBySensorChange(combination, oximeterItem.getValue(), now);
						newCombinationList.add(item);

						changeOximeterFlag = false;

					} else if (changeBodyTempFlag && bodyTempHealthTypeId == healthTypeId) {
						// Collection Modify
						modifyCombinationList.add(combination);

						// Create new data
						Combination item = createCombinationBySensorChange(combination, bodyTempItem.getValue(), now);
						newCombinationList.add(item);

						changeBodyTempFlag = false;

					} else if (changeBreathRateFlag && breathHealthTypeId == healthTypeId) {
						// Collection Modify
						modifyCombinationList.add(combination);

						// Create new data
						Combination item = createCombinationBySensorChange(combination, breathRateItem.getValue(), now);
						newCombinationList.add(item);

						changeBreathRateFlag = false;
					}
				}
			}
		}

		// Enable for DB
		for (Combination item : modifyCombinationList) {
			hqeModify.updateCombination(item);
		}
		for (Combination item : newCombinationList) {
			hqeModify.createCombination(item);
		}
		modifyWin.detach();
		BindUtils.postGlobalCommand(null, null, "refreshPatientInfo", null);
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

	public List<EwsSpecDao> getEwsSpecDaoList() {
		return ewsSpecDaoList;
	}
}