package org.itri.view.humanhealth.hibernate;
// Generated 2020/4/24 �U�� 08:59:27 by Hibernate Tools 4.0.0.Final
import javax.persistence.GeneratedValue;
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
 * RtHeartRhythmRecord generated by hbm2java
 */
@SuppressWarnings("serial")
@Table(name = "rt_heart_rhythm_record")
public class RtHeartRhythmRecord implements java.io.Serializable {

	private long rtHeartRhythmRecordId;
	private Patient patient;
	private Sensor sensor;
	private String heartRateData;
	private String breathData;
	private String breathWaveData1;
	private String breathWaveData2;
	private String batteryLevel;
	private String heartRateStatus;
	private String breathStatus;
	private Date lastUpdated;

	public RtHeartRhythmRecord() {
	}

	public RtHeartRhythmRecord(long rtHeartRhythmRecordId, Patient patient, Sensor sensor, String heartRateData,
			String breathData, String breathWaveData1, String breathWaveData2, String batteryLevel,
			String heartRateStatus, String breathStatus, Date lastUpdated) {
		this.rtHeartRhythmRecordId = rtHeartRhythmRecordId;
		this.patient = patient;
		this.sensor = sensor;
		this.heartRateData = heartRateData;
		this.breathData = breathData;
		this.breathWaveData1 = breathWaveData1;
		this.breathWaveData2 = breathWaveData2;
		this.batteryLevel = batteryLevel;
		this.heartRateStatus = heartRateStatus;
		this.breathStatus = breathStatus;
		this.lastUpdated = lastUpdated;
	}

	@SequenceGenerator(name="rt_heart_rhythm_record_seq", sequenceName="rt_heart_rhythm_record_rt_heart_rhythm_record_id_seq", allocationSize=1)

	@Column(name = "rt_heart_rhythm_record_id", unique = true, nullable = false)
	public long getRtHeartRhythmRecordId() {
		return this.rtHeartRhythmRecordId;
	}

	public void setRtHeartRhythmRecordId(long rtHeartRhythmRecordId) {
		this.rtHeartRhythmRecordId = rtHeartRhythmRecordId;
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
	@JoinColumn(name = "sensor_id", nullable = false)
	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	@Column(name = "heart_rate_data", nullable = false, length = 64)
	public String getHeartRateData() {
		return this.heartRateData;
	}

	public void setHeartRateData(String heartRateData) {
		this.heartRateData = heartRateData;
	}

	@Column(name = "breath_data", nullable = false, length = 64)
	public String getBreathData() {
		return this.breathData;
	}

	public void setBreathData(String breathData) {
		this.breathData = breathData;
	}

	@Column(name = "breath_wave_data1", nullable = false, length = 64)
	public String getBreathWaveData1() {
		return this.breathWaveData1;
	}

	public void setBreathWaveData1(String breathWaveData1) {
		this.breathWaveData1 = breathWaveData1;
	}

	@Column(name = "breath_wave_data2", nullable = false, length = 64)
	public String getBreathWaveData2() {
		return this.breathWaveData2;
	}

	public void setBreathWaveData2(String breathWaveData2) {
		this.breathWaveData2 = breathWaveData2;
	}

	@Column(name = "battery_level", nullable = false, length = 64)
	public String getBatteryLevel() {
		return this.batteryLevel;
	}

	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	@Column(name = "heart_rate_status", nullable = false, length = 32)
	public String getHeartRateStatus() {
		return this.heartRateStatus;
	}

	public void setHeartRateStatus(String heartRateStatus) {
		this.heartRateStatus = heartRateStatus;
	}

	@Column(name = "breath_status", nullable = false, length = 32)
	public String getBreathStatus() {
		return this.breathStatus;
	}

	public void setBreathStatus(String breathStatus) {
		this.breathStatus = breathStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated", nullable = false, length = 29)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}