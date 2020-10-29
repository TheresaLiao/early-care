package org.itri.view.humanhealth.detail;

import java.util.ArrayList;
import java.util.List;

import org.itri.view.humanhealth.detail.Imp.SensorDaoHibernateImpl;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.personal.chart.Imp.PersonInfoTableDaoHibernateImpl;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class ModifyFormViewTwo extends SelectorComposer<Window> {
	private static final long serialVersionUID = 1L;

	private PersonInfoTableDaoHibernateImpl hqe;
	private SensorDaoHibernateImpl hqeSensor;

	@Wire("#textboxPatientId")
	private Textbox textboxPatientId;
	@Wire("#selectboxPatient")
	private Selectbox selectboxPatient;

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

	private long patientId = 0;
	private long oximeterSensorId = 0;
	private long breathRateSensorId = 0;
	private long bodyTempSensorId = 0;

	@Override
	public void doAfterCompose(Window comp) throws Exception {
		super.doAfterCompose(comp);

		hqe = new PersonInfoTableDaoHibernateImpl();
		hqeSensor = new SensorDaoHibernateImpl();

		patientId = Long.parseLong(textboxPatientId.getValue());
		oximeterSensorId = Long.parseLong(textboxOximeter.getValue());
		breathRateSensorId = Long.parseLong(textboxBreathRate.getValue());
		bodyTempSensorId = Long.parseLong(textboxBodyTemp.getValue());
		System.out.println("oximeterSensorId :" + oximeterSensorId);
		System.out.println("breathRateSensorId :" + breathRateSensorId);
		System.out.println("bodyTempSensorId :" + bodyTempSensorId);

		getSelectBoxList();
		getSensorList();
	}

	// Get Patient List
	private void getSelectBoxList() {
		// Get List
		List<PatientInfo> dataList = hqe.getPatientList();
		int selectedItemIndex = 0;
		List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();
		for (int index = 0; index < dataList.size(); index++) {
			PatientInfo patient = dataList.get(index);
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(patient.getPatient().getPatientId(),
					patient.getName());
			patientList.add(item);

			if (patient.getPatient().getPatientId() == patientId) {
				selectedItemIndex = index;
			}
		}

		// Set Default selected
		ListModelList model = new ListModelList(patientList);
		model.addToSelection(model.get(selectedItemIndex));
		selectboxPatient.setModel(model);
	}

	private void getSensorList() {
		List<DateKeyValueSelectBox> oximeterList = new ArrayList<DateKeyValueSelectBox>();
		List<DateKeyValueSelectBox> breathRateList = new ArrayList<DateKeyValueSelectBox>();
		List<DateKeyValueSelectBox> tempList = new ArrayList<DateKeyValueSelectBox>();

		int oximeterIndex = 0;
		int breathRateIndex = 0;
		int bodyTempIndex = 0;

		boolean oximeterFlag = false;
		boolean breathRateFlag = false;
		boolean bodyTempFlag = false;

		List<Sensor> SensorList = hqeSensor.getSensorListBySensorTypeId();
		for (Sensor sensor : SensorList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(sensor.getSensorId(), sensor.getSensorName());
			int sensorTypeId = (int) sensor.getSensorType().getSensorTypeId();

			if (1 == sensorTypeId) {
				oximeterList.add(item);
				if (!oximeterFlag && oximeterSensorId == item.getValue()) {
					oximeterIndex = oximeterList.size();
					System.out.println("oximeterSensorId :" + oximeterSensorId);
					System.out.println("oximeterIndex :" + oximeterIndex);
					System.out.println("item.getValue() :" + item.getValue());
					System.out.println("item.getText() :" + item.getText());
					oximeterFlag = true;
				}
			}

			if (2 == sensorTypeId) {
				breathRateList.add(item);
				if (!breathRateFlag && breathRateSensorId == item.getValue()) {
					breathRateIndex = breathRateList.size() + 1;
					System.out.println("breathRateSensorId :" + breathRateSensorId);
					System.out.println("breathRateIndex :" + breathRateIndex);
					System.out.println("item.getValue() :" + item.getValue());
					System.out.println("item.getText() :" + item.getText());
					breathRateFlag = true;
				}
			}

			if (3 == sensorTypeId) {
				tempList.add(item);
				if (!bodyTempFlag && bodyTempSensorId == item.getValue()) {
					bodyTempIndex = tempList.size() + 1;
					System.out.println("bodyTempSensorId :" + bodyTempSensorId);
					System.out.println("bodyTempIndex :" + bodyTempIndex);
					System.out.println("item.getValue() :" + item.getValue());
					System.out.println("item.getText() :" + item.getText());
					bodyTempFlag = true;
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

	@Listen("onClick = #closeClick")
	public void close() {
//		modifyWin.detach();
	}

}