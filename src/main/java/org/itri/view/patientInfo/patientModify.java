package org.itri.view.patientInfo;

import java.util.ArrayList;
import java.util.List;

import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Selectbox;

public class patientModify extends SelectorComposer<Component> {

	@Wire("#labelGender")
	private Label labelGender;

	@Wire("#labelAge")
	private Label labelAge;

	@Wire("#selectboxGenders")
	private Selectbox selectboxGenders;
	private ListModelList<DateKeyValueSelectBox> genderModel = new ListModelList<DateKeyValueSelectBox>();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		System.out.println(labelGender.getValue());
		System.out.println(labelAge.getValue());

		getGenderList();
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
