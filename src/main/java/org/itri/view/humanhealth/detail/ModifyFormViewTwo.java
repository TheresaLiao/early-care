package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.itri.view.humanhealth.detail.Imp.SensorDaoHibernateImpl;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.hibernate.Sensor2healthType;
import org.itri.view.humanhealth.personal.chart.Imp.PersonInfoTableDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.zkoss.bind.annotation.SelectorParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ModifyFormViewTwo extends SelectorComposer<Component> {
	private static final long serialVersionUID = 1L;

	private PersonInfoTableDaoHibernateImpl hqe;
	private SensorDaoHibernateImpl hqeSensor;

	private long patientId = 0;
	private long oximeterSensorId = 0;
	private long breathRateSensorId = 0;
	private long bodyTempSensorId = 0;

	@Wire
	private Window modifyWin;

	@Wire("#textboxPatientId")
	private Textbox textboxPatientId;
	@Wire("#selectboxPatient")
	private Selectbox selectboxPatient;

	//
	@Wire("#textboxOximeter")
	private Textbox textboxOximeter;
	@Wire("#selectboxOximeter")
	private Selectbox selectboxOximeter;

	@Wire("#textboxBreathRate")
	private Textbox textboxBreathRate;
	@Wire("#selectboxBreathRate")
	private Selectbox selectboxBreathRate;

	@Wire("#textboxBodyTemp")
	private Textbox textboxBodyTemp;
	@Wire("#selectboxTemp")
	private Selectbox selectboxTemp;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		// Connect DB
		hqe = new PersonInfoTableDaoHibernateImpl();
		hqeSensor = new SensorDaoHibernateImpl();

		// Get parent pass through parameter
		patientId = Long.parseLong(textboxPatientId.getValue());
		oximeterSensorId = Long.parseLong(textboxOximeter.getValue());
		breathRateSensorId = Long.parseLong(textboxBreathRate.getValue());
		bodyTempSensorId = Long.parseLong(textboxBodyTemp.getValue());

		// Get selectBox List
		getSelectBoxList();
		getSensorList();
	}

	@Listen("onClick = #submitButton")
	public void submit() {

		
		modifyWin.detach();
	}

	@Listen("onClick = #closeButton")
	public void close() {
		modifyWin.detach();
	}

	// Get Patient List
	private void getSelectBoxList() {
		int selectedItemIndex = 0;
		boolean flag = false;
		List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		List<PatientInfo> dataList = hqe.getPatientList();
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
		ListModelList model = new ListModelList(patientList);
		model.addToSelection(model.get(selectedItemIndex));
		selectboxPatient.setModel(model);
	}

	// Get Sensor List
	private void getSensorList() {
		int oximeterIndex = 0;
		boolean oximeterFlag = false;
		List<DateKeyValueSelectBox> oximeterList = new ArrayList<DateKeyValueSelectBox>();

		int breathRateIndex = 0;
		boolean breathRateFlag = false;
		List<DateKeyValueSelectBox> breathRateList = new ArrayList<DateKeyValueSelectBox>();

		int bodyTempIndex = 0;
		boolean bodyTempFlag = false;
		List<DateKeyValueSelectBox> tempList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		List<Sensor> SensorList = hqeSensor.getSensorListBySensorTypeId();
		for (Sensor sensor : SensorList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(sensor.getSensorId(), sensor.getSensorName());
			Set<Sensor2healthType> sensorHealthTypeSet = sensor.getSensor2healthTypes();

			for (Sensor2healthType sensorHealthType : sensorHealthTypeSet) {
				long healthTypeId = sensorHealthType.getHealthType().getHealthTypeId();
				if (healthTypeId == 2) {
					oximeterList.add(item);
					if (!oximeterFlag && oximeterSensorId == item.getValue()) {
						oximeterIndex = oximeterList.size() - 1;
						oximeterFlag = true;
					}
				} else if (healthTypeId == 3) {
					tempList.add(item);
					if (!bodyTempFlag && bodyTempSensorId == item.getValue()) {
						bodyTempIndex = tempList.size() - 1;
						bodyTempFlag = true;
					}

				} else if (healthTypeId == 4) {
					breathRateList.add(item);
					if (!breathRateFlag && breathRateSensorId == item.getValue()) {
						breathRateIndex = breathRateList.size() - 1;
						breathRateFlag = true;
					}
				}
			}
		}

		// Set Default selected
		ListModelList oximeterModel = new ListModelList(oximeterList);
		oximeterModel.addToSelection(oximeterModel.get(oximeterIndex));
		selectboxOximeter.setModel(oximeterModel);

		ListModelList breathRateModel = new ListModelList(breathRateList);
		breathRateModel.addToSelection(breathRateModel.get(breathRateIndex));
		selectboxBreathRate.setModel(breathRateModel);

		ListModelList bodyTempModel = new ListModelList(tempList);
		bodyTempModel.addToSelection(bodyTempModel.get(bodyTempIndex));
		selectboxTemp.setModel(bodyTempModel);
	}
}