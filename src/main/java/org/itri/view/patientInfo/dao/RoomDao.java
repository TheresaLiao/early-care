package org.itri.view.patientInfo.dao;

import org.itri.view.humanhealth.hibernate.Room;

public class RoomDao {
	private long roomId;
	private String roomNum;
	private long patientId;
	private String username;
	private int roomGroup;

	public RoomDao(Room room) {
		this.setRoomId(room.getRoomId());
		this.setRoomNum(room.getRoomNum());
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public String getRoomNum() {
		return roomNum;
	}

	public void setRoomNum(String roomNum) {
		this.roomNum = roomNum;
	}

	public int getRoomGroup() {
		return roomGroup;
	}

	public void setRoomGroup(int roomGroup) {
		this.roomGroup = roomGroup;
	}
}
