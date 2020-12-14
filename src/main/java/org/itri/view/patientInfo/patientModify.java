package org.itri.view.patientInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.itri.view.patientInfo.Imp.patientSummaryHibernateImpl;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class patientModify extends SelectorComposer<Component> {

	@Wire
	private Window modifyPatientWin;

	@Wire("#labelPatientId")
	private Label labelPatientId;
	private long patientIdOrg;

	@Wire("#textboxName")
	private Textbox textboxName;

	@Wire("#labelGender")
	private Label labelGender;
	private String genderOrg;

	@Wire("#labelAge")
	private Label labelAge;
	private int ageOrg = 0;
	@Wire("#spinnerAge")
	private Spinner spinnerAge;

	@Wire("#selectboxGenders")
	private Selectbox selectboxGenders;
	private ListModelList<DateKeyValueSelectBox> genderModel = new ListModelList<DateKeyValueSelectBox>();

	private patientSummaryHibernateImpl hqe = new patientSummaryHibernateImpl();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		setPatientIdOrg(formateStr2Long(labelPatientId.getValue()));
		setGenderOrg(labelGender.getValue());
		setAgeOrg(formateStr2Int(labelAge.getValue()));
		spinnerAge.setValue(getAgeOrg());

		getGenderList();
	}

	@Listen("onClick = #modifyButton")
	public void modify() {
		System.out.println("modifyButton");
		if (textboxName.getValue() == null || spinnerAge.getValue() == null) {
			Messagebox.show("姓名 與 年齡為必填欄位!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			PatientInfo patientInfoExit = hqe.getPatientInfoByPatientId(getPatientIdOrg());

			String name = textboxName.getValue();
			String gender = genderModel.get(selectboxGenders.getSelectedIndex()).getText();
			patientInfoExit.setAge(String.valueOf(spinnerAge.getValue()));
			patientInfoExit.setGender(gender);
			patientInfoExit.setName(name);
			patientInfoExit.setLastUpdated(new Date());

			hqe.updatePatientInfo(patientInfoExit);
		}

		// Close win
		modifyPatientWin.detach();
		BindUtils.postGlobalCommand(null, null, "refreshPatientSummary", null);
	}

	@Listen("onClick = #closeButton")
	public void close() {
		modifyPatientWin.detach();
	}

	private void getGenderList() {
		int selectedItemIndex = 0;
		boolean flag = false;
		List<DateKeyValueSelectBox> genderList = new ArrayList<DateKeyValueSelectBox>();
		DateKeyValueSelectBox itemF = new DateKeyValueSelectBox(1, "F");
		genderList.add(itemF);
		if (!flag && getGenderOrg().equals(itemF.getText())) {
			selectedItemIndex = genderList.size() - 1;
			flag = true;
		}

		DateKeyValueSelectBox itemM = new DateKeyValueSelectBox(2, "M");
		genderList.add(itemM);
		if (!flag && getGenderOrg().equals(itemM.getText())) {
			selectedItemIndex = genderList.size() - 1;
			flag = true;
		}

		// Set Default selected
		genderModel = new ListModelList(genderList);
		genderModel.addToSelection(genderModel.get(selectedItemIndex));
		selectboxGenders.setModel(genderModel);

	}

	private static long formateStr2Long(String str) {
		if (str.isEmpty() || str == null) {
			return 0;
		}
		return Long.valueOf(str);
	}

	private static int formateStr2Int(String str) {
		if (str.isEmpty() || str == null) {
			return 0;
		}
		return Integer.valueOf(str);
	}

	public String getGenderOrg() {
		return genderOrg;
	}

	public void setGenderOrg(String genderOrg) {
		this.genderOrg = genderOrg;
	}

	public int getAgeOrg() {
		return ageOrg;
	}

	public void setAgeOrg(int ageOrg) {
		this.ageOrg = ageOrg;
	}

	public long getPatientIdOrg() {
		return patientIdOrg;
	}

	public void setPatientIdOrg(long patientIdOrg) {
		this.patientIdOrg = patientIdOrg;
	}

}
