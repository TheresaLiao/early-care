package org.itri.view.patientInfo;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.itri.view.humanhealth.hibernate.Combination;
import org.itri.view.humanhealth.hibernate.Room;
import org.itri.view.humanhealth.hibernate.RoomGroup;
import org.itri.view.patientInfo.Imp.roomSummaryHibernateImpl;
import org.itri.view.patientInfo.dao.RoomDao;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class roomSummary {

	private static String modifyPage = "/patientInfo/roomCreate.zul";
	List<RoomDao> roomSummary = new ArrayList<RoomDao>();
	private roomSummaryHibernateImpl hqe;

	@NotifyChange("roomSummary")
	@Init
	public void init() {
		hqe = new roomSummaryHibernateImpl();
		queryStates();
	}

	@Command
	public void createClick() {
		Map<String, Object> arguments = new HashMap<String, Object>();
		Window window = (Window) Executions.createComponents(modifyPage, null, null);
		window.doModal();
	}

	@NotifyChange({ "roomSummary" })
	@GlobalCommand
	public void refreshRoomSummary() {
		queryStates();
	}

	static String MODIFY_PAGE = "/patientInfo/roomModify.zul";

	@Command
	public void modifyRoom(@BindingParam("roomDao") RoomDao item) {
		System.out.println("modifyRoom");
		Map<String, Object> arguments = new HashMap<String, Object>();
		arguments.put("orderItems", item);
		Window window = (Window) Executions.createComponents(MODIFY_PAGE, null, arguments);
		window.doModal();
	}

//	@Command
//	public void removerRoom(@BindingParam("roomDao") RoomDao item) {
//		System.out.println("removerRoom");
//
//		long roomId = item.getRoomId();
//		long patientId = item.getPatientId();
//
//		Messagebox.show("是否要刪除病床" + item.getRoomNum() + "?", "Info", Messagebox.OK | Messagebox.CANCEL,
//				Messagebox.QUESTION, new org.zkoss.zk.ui.event.EventListener() {
//					public void onEvent(Event e) {
//						if (Messagebox.ON_OK.equals(e.getName())) {
//							if (roomId != 0) {
//								hqe.deleteCombination(roomId, patientId);
//							}
//							hqe.deleteRoomGroup(roomId);
//							hqe.deleteRoom(roomId);
//							BindUtils.postGlobalCommand(null, null, "refreshRoomSummary", null);
//						}
//					}
//				});
//
//	}

	private void queryStates() {
		System.out.println("queryStates");
		roomSummary = new ArrayList<RoomDao>();

		List<Room> roomList = hqe.getRoomList();
		for (Room room : roomList) {
			roomSummary.add(new RoomDao(room));
		}

		List<Combination> combinationList = hqe.getCombinationList(distinctRoomId(roomSummary));
		List<RoomGroup> roomGroupList = hqe.getRoomGroupList();
		for (RoomDao roomInfo : roomSummary) {
			for (Combination combination : combinationList) {
				if (roomInfo.getRoomId() == combination.getRoom().getRoomId()) {
					roomInfo.setPatientId(combination.getPatient().getPatientId());
					roomInfo.setUsername(
							combination.getPatient().getPatientInfos().stream().findFirst().get().getName());
					break;
				}
			}
			for (RoomGroup roomGroup : roomGroupList) {
				if (roomInfo.getRoomId() == roomGroup.getRoom().getRoomId()) {
					roomInfo.setRoomGroup(roomGroup.getRoomGroup());
					break;
				}
			}
		}
	}

	private List<Long> distinctRoomId(List<RoomDao> roomSummary) {
		Set<Long> roomId = new HashSet<Long>();
		for (RoomDao room : roomSummary) {
			roomId.add(room.getRoomId());
		}
		List<Long> resp = new ArrayList<Long>(roomId);
		return resp;
	}

	public List<RoomDao> getRoomSummary() {
		return roomSummary;
	}

}
