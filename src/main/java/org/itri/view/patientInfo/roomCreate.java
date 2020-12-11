package org.itri.view.patientInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.Patient;
import org.itri.view.humanhealth.hibernate.PatientInfo;
import org.itri.view.humanhealth.hibernate.Room;
import org.itri.view.humanhealth.hibernate.RoomGroup;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.itri.view.patientInfo.Imp.roomSummaryHibernateImpl;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class roomCreate extends SelectorComposer<Component> {

	@Wire
	private Window createRoomWin;

	@Wire("#textboxRoomName")
	private Textbox textboxRoomName;

	@Wire("#selectboxPatient")
	private Selectbox selectboxPatient;
	private ListModelList<DateKeyValueSelectBox> patientModel = new ListModelList<DateKeyValueSelectBox>();

	@Wire("#selectboxRoom")
	private Selectbox selectboxRoom;
	private ListModelList<DateKeyValueSelectBox> roomModel = new ListModelList<DateKeyValueSelectBox>();

	private roomSummaryHibernateImpl hqe;

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);

		hqe = new roomSummaryHibernateImpl();
		getPatientList();
		getRoomGroupList();
	}

	@Listen("onClick = #submitButton")
	public void submit() {
		if (textboxRoomName.getValue() == null) {
			Messagebox.show("病床名稱 病床名稱為必填欄位!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (hqe.getRoomByRoomNum(textboxRoomName.getValue()).getRoomId() != 0) {
			Messagebox.show("此病床名稱已經擁有，請修改病床名稱!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {
			Date now = new Date();

			// Create Room
			Room room = new Room();
			room.setIsFull(false);
			room.setLastUpdated(now);
			room.setRoomNum(textboxRoomName.getValue());
			hqe.createRoom(room);

			// Create Room Group
			RoomGroup roomGroup = new RoomGroup();
			Room roomDb = hqe.getRoomByRoomNum(textboxRoomName.getValue());
			roomGroup.setRoom(roomDb);
			String roomGroupStr = roomModel.get(selectboxRoom.getSelectedIndex()).getText();
			int roomGroupInt = Integer.valueOf(roomGroupStr);
			roomGroup.setRoomGroup(roomGroupInt);
			hqe.createRoomGroup(roomGroup);

			
			if (selectboxPatient.getSelectedIndex() != -1) {
				// Create Combination
				Combination item = new Combination();
				item.setRoom(roomDb);
				long patientId = patientModel.get(selectboxPatient.getSelectedIndex()).getValue();
				Patient patient = new Patient();
				patient.setPatientId(patientId);
				item.setPatient(patient);
				item.setStartTime(now);
				item.setSensor(null);
				hqe.createCombination(item);
			}

			// Close win
			createRoomWin.detach();
			BindUtils.postGlobalCommand(null, null, "refreshRoomSummary", null);
		}
	}

	@Listen("onClick = #closeButton")
	public void close() {
		createRoomWin.detach();
	}

	// Get Patient List
	private void getPatientList() {
		List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		Set<Long> usedPatientIdSet = hqe.getUsedPatientIdList();
		List<PatientInfo> dataList = new ArrayList<PatientInfo>();
		if (usedPatientIdSet.size() == 0) {
			dataList = hqe.getPatientList();
		} else {
			dataList = hqe.getPatientList(new ArrayList<>(usedPatientIdSet));
		}

		for (PatientInfo patient : dataList) {
			DateKeyValueSelectBox item = new DateKeyValueSelectBox(patient.getPatient().getPatientId(),
					patient.getName());
			patientList.add(item);
		}

		// Set Default selected
		if (patientList.size() != 0) {
			patientModel = new ListModelList(patientList);
			patientModel.addToSelection(patientModel.get(0));
			selectboxPatient.setModel(patientModel);
		}
	}

	// Get Room Group List
	private void getRoomGroupList() {
		List<DateKeyValueSelectBox> roomGroupList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		DateKeyValueSelectBox item0 = new DateKeyValueSelectBox(0L, "0");
		DateKeyValueSelectBox item1 = new DateKeyValueSelectBox(1L, "1");
		DateKeyValueSelectBox item2 = new DateKeyValueSelectBox(2L, "2");
		DateKeyValueSelectBox item3 = new DateKeyValueSelectBox(3L, "3");
		DateKeyValueSelectBox item4 = new DateKeyValueSelectBox(4L, "4");
		DateKeyValueSelectBox item5 = new DateKeyValueSelectBox(5L, "5");
		roomGroupList.add(item0);
		roomGroupList.add(item1);
		roomGroupList.add(item2);
		roomGroupList.add(item3);
		roomGroupList.add(item4);
		roomGroupList.add(item5);

		// Set Default selected
		roomModel = new ListModelList(roomGroupList);
		roomModel.addToSelection(roomModel.get(0));
		selectboxRoom.setModel(roomModel);
	}
}
