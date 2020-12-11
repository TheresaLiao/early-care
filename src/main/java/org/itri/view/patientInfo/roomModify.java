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
import org.itri.view.humanhealth.hibernate.Sensor;
import org.itri.view.humanhealth.personal.chart.dao.DateKeyValueSelectBox;
import org.itri.view.patientInfo.Imp.roomSummaryHibernateImpl;
import org.jfree.text.TextBox;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Selectbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

public class roomModify extends SelectorComposer<Component> {

	@Wire
	private Window modifyRoomWin;

	@Wire("#labelRoomId")
	private Label labelRoomId;
	private Long roomIdOrg;

	@Wire("#labelRoomGroup")
	private Label labelRoomGroup;
	private Long roomGroupOrg;

	@Wire("#labelPatientId")
	private Label labelPatientId;
	private Long patientIdOrg;

	@Wire("#textboxRoomName")
	private Textbox textboxRoomName;
	private String roomNameOrg;

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

		long roomId = formateStr2Long(labelRoomId.getValue());
		long roomGroup = formateStr2Long(labelRoomGroup.getValue());
		long patientId = formateStr2Long(labelPatientId.getValue());

		setRoomIdOrg(roomId);
		setRoomGroupOrg(roomGroup);
		setPatientIdOrg(patientId);
		setRoomNameOrg(textboxRoomName.getValue());

		getPatientList();
		getRoomGroupList();
	}

	@Listen("onClick = #submitButton")
	public void submit() {
		if (textboxRoomName.getValue() == null) {
			Messagebox.show("病床名稱 病床名稱為必填欄位!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else if (hqe.getRoomByRoomNum(textboxRoomName.getValue()).getRoomId() != getRoomIdOrg()
				&& hqe.getRoomByRoomNum(textboxRoomName.getValue()).getRoomId() != 0) {
			Messagebox.show("此病床名稱已經擁有，請修改病床名稱!", "Warning", Messagebox.OK, Messagebox.EXCLAMATION);
		} else {

			Room room = hqe.getRoomByRoomId(getRoomIdOrg());

			// Change Room name
			if (!getRoomNameOrg().equals(textboxRoomName.getValue())) {
				room.setRoomNum(textboxRoomName.getValue());
				hqe.updateRoom(room);
			}

			// Change Room Group
			if (getRoomGroupOrg() != roomModel.getSelection().stream().findFirst().get().getValue()) {
				RoomGroup exitRoomGroup = hqe.getRoomGroupByRoomId(getRoomIdOrg());
				if (exitRoomGroup.getRoomGroupId() != 0) {
					exitRoomGroup.setRoomGroup((int) roomModel.getSelection().stream().findFirst().get().getValue());
					hqe.updateRoomGroup(exitRoomGroup);
				} else {
					RoomGroup newRoomGroup = new RoomGroup();
					newRoomGroup.setRoom(exitRoomGroup.getRoom());
					newRoomGroup.setRoomGroup((int) roomModel.getSelection().stream().findFirst().get().getValue());
					hqe.createRoomGroup(newRoomGroup);
				}
			}

			// Change Patient
			Date now = new Date();
			if (selectboxPatient.getSelectedIndex() == -1) {
				// Patient change into empty ,so combination close those RoomIdOrg item
				if (getPatientIdOrg() != 0) {
					List<Combination> exitCombinationList = hqe.getCombinationByRoomId(getRoomIdOrg());
					for (Combination exitCombination : exitCombinationList) {
						exitCombination.setEndTime(now);
						hqe.updateCombinationEndTime(exitCombination);
					}
				}
			} else {
				// Get exit Combination
				List<Combination> exitCombinationList = hqe.getCombinationByRoomId(getRoomIdOrg());

				if (exitCombinationList.size() == 0) {
					Combination newCombination = new Combination();
					newCombination.setStartTime(now);
					newCombination.setRoom(room);

					Patient patient = new Patient();
					patient.setPatientId(patientModel.getSelection().stream().findFirst().get().getValue());
					newCombination.setPatient(patient);

					hqe.createCombination(newCombination);

				} else if (getPatientIdOrg() != patientModel.getSelection().stream().findFirst().get().getValue()) {
					System.out.println("Change Patient");
					for (Combination exitCombination : exitCombinationList) {
						hqe.updateCombinationEndTime(exitCombination);

						Combination newCombination = initCombination(exitCombination);
						Patient patient = new Patient();
						patient.setPatientId(patientModel.getSelection().stream().findFirst().get().getValue());
						newCombination.setPatient(patient);
						newCombination.setStartTime(now);
						hqe.createCombination(newCombination);
					}
				}
			}

			// Close win
			modifyRoomWin.detach();
			BindUtils.postGlobalCommand(null, null, "refreshRoomSummary", null);
		}

	}

	private Combination initCombination(Combination exitCombination) {
		Date now = new Date();

		Combination resp = new Combination();
		resp.setRoom(exitCombination.getRoom());
		resp.setPatient(exitCombination.getPatient());
		resp.setSensor(exitCombination.getSensor());

		resp.setStartTime(now);

		return resp;
	}

	// Get Patient List
	private void getPatientList() {
		int selectedItemIndex = 0;
		boolean flag = false;
		List<DateKeyValueSelectBox> patientList = new ArrayList<DateKeyValueSelectBox>();

		// Get List
		Set<Long> usedPatientIdSet = hqe.getUsedPatientIdList();

		List<PatientInfo> dataList = hqe.getPatientList(new ArrayList<>(usedPatientIdSet), getPatientIdOrg());
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
		if (patientList.size() != 0) {
			patientModel = new ListModelList(patientList);
			patientModel.addToSelection(patientModel.get(selectedItemIndex));
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
		roomModel.addToSelection(roomModel.get(getRoomGroupOrg().intValue()));
		selectboxRoom.setModel(roomModel);
	}

	private static long formateStr2Long(String str) {
		if (str.isEmpty() || str == null) {
			return 0;
		}
		return Long.parseLong(str);
	}

	public Long getRoomIdOrg() {
		return roomIdOrg;
	}

	public void setRoomIdOrg(Long roomIdOrg) {
		this.roomIdOrg = roomIdOrg;
	}

	public Long getRoomGroupOrg() {
		return roomGroupOrg;
	}

	public void setRoomGroupOrg(Long roomGroupOrg) {
		this.roomGroupOrg = roomGroupOrg;
	}

	public Long getPatientIdOrg() {
		return patientIdOrg;
	}

	public void setPatientIdOrg(Long patientIdOrg) {
		this.patientIdOrg = patientIdOrg;
	}

	public String getRoomNameOrg() {
		return roomNameOrg;
	}

	public void setRoomNameOrg(String roomNameOrg) {
		this.roomNameOrg = roomNameOrg;
	}
}
