package org.itri.view.patientInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.itri.view.humanhealth.hibernate.NewsMathOperator;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.itri.view.patientInfo.Imp.patientSummaryHibernateImpl;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class patientCreate extends SelectorComposer<Component> {

	@Wire
	private Window createPatientWin;

	@Wire("#textboxName")
	private Textbox textboxName;

	@Wire("#selectboxGenders")
	private Selectbox selectboxGenders;
	private ListModelList<DateKeyValueSelectBox> genderModel = new ListModelList<DateKeyValueSelectBox>();

	@Wire("#spinnerAge")
	private Spinner spinnerAge;

	private patientSummaryHibernateImpl hqe = new patientSummaryHibernateImpl();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		getGenderList();
	}

	@Listen("onClick = #submitButton")
	public void submit() {
		if (textboxName.getValue() == null || spinnerAge.getValue() == null) {
			Messagebox.show("姓名 與 年齡為必填欄位!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			String name = textboxName.getValue();
			String gender = genderModel.get(selectboxGenders.getSelectedIndex()).getText();
			int age = spinnerAge.getValue();

			Patient patient = new Patient();
			patient.setBodyTempStatus("N");
			patient.setBreathStatus("N");
			patient.setHeartRateStatus("N");
			patient.setOximeterStatus("N");
			patient.setPmFiveStatus("N");
			patient.setMattressStatus("N");
			patient.setBodyTempNewsScore(0);
			patient.setBreathNewsScore(0);
			patient.setHeartRateNewsScore(0);
			patient.setNewsWarningCounter(0);
			patient.setOximeterNewsScore(0);
			patient.setTotalNewsScore(0);
			patient.setIsDeleted(false);
			patient.setIsNewsWarning(false);
			patient.setLastUpdated(new Date());
			patient.setTimeCreated(new Date());
//			patient.setCombinations(Set<Combination>);
//			patient.setNewsRecords(Set<NewsRecord>);
//			patient.setNewsWarningConditions(Set<NewsWarningCondition>);
//			patient.setPatientInfos(Set<PatientInfo>);
//			patient.setPatientId(long);
			hqe.createPatient(patient);

			PatientInfo patientInfo = new PatientInfo();
			patientInfo.setAge(String.valueOf(age));
			patientInfo.setIsDeleted(false);
			patientInfo.setLastUpdated(new Date());
			patientInfo.setName(name);
			patientInfo.setTimeCreated(new Date());
			patientInfo.setPatient(patient);
			patientInfo.setGender(gender);
			patientInfo.setUsername(null);
			patientInfo.setPassword(null);
			patientInfo.setIdNum("");
//			item.setPatientInfoId(long);
			hqe.createPatientInfo(patientInfo);

			createPatientWin.detach();
		}
		BindUtils.postGlobalCommand(null, null, "refreshPatientSummary", null);
	}

	@Listen("onClick = #closeButton")
	public void close() {
		createPatientWin.detach();
	}

	private void getGenderList() {
		List<DateKeyValueSelectBox> genderList = new ArrayList<DateKeyValueSelectBox>();
		DateKeyValueSelectBox itemF = new DateKeyValueSelectBox(1, "F");
		DateKeyValueSelectBox itemM = new DateKeyValueSelectBox(2, "M");
		genderList.add(itemF);
		genderList.add(itemM);

		// Set Default selected
		genderModel = new ListModelList(genderList);
		genderModel.addToSelection(genderModel.get(0));
		selectboxGenders.setModel(genderModel);
	}
}
