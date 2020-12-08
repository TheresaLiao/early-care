package org.itri.view.humanhealth.hibernate;
// Generated 2020/10/29 �U�� 04:58:12 by Hibernate Tools 4.0.0.Final
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Combination generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "combination")
public class Combination implements java.io.Serializable {

	private long combinationId;
	private Room room;
	private Patient patient;
	private Sensor sensor;
	private Date startTime;
	private Date endTime;

	public Combination() {
	}
	
	public Combination(Room room, Patient patient, Sensor sensor, Date startTime) {
		this.room = room;
		this.patient = patient;
		this.sensor = sensor;
		this.startTime = startTime;
	}
	
	public Combination(long combinationId, Room room, Patient patient, Sensor sensor, Date startTime) {
		this.combinationId = combinationId;
		this.room = room;
		this.patient = patient;
		this.sensor = sensor;
		this.startTime = startTime;
	}

	public Combination(long combinationId, Room room, Patient patient, Sensor sensor, Date startTime, Date endTime) {
		this.combinationId = combinationId;
		this.room = room;
		this.patient = patient;
		this.sensor = sensor;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@SequenceGenerator(name="combination_seq", sequenceName="combination_combination_id_seq")	@GeneratedValue(generator="combination_seq")	@Id

	@Column(name = "combination_id", unique = true, nullable = false)
	public long getCombinationId() {
		return this.combinationId;
	}

	public void setCombinationId(long combinationId) {
		this.combinationId = combinationId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "room_id", nullable = false)
	public Room getRoom() {
		return this.room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", nullable = false)
	public Patient getPatient() {
		return this.patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_id")
	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", nullable = false, length = 29)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", length = 29)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
