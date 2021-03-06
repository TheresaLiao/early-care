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
 * RtHeartRhythmRecord generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "rt_heart_rhythm_record")
public class RtHeartRhythmRecord implements java.io.Serializable {

	private long rtHeartRhythmRecordId;
	private Sensor sensor;
	private String breathData;
	private String breathWaveData1;
	private String breathWaveData2;
	private String batteryLevel;
	private String breathStatus;
	private Date lastUpdated;

	public RtHeartRhythmRecord() {
	}

	public RtHeartRhythmRecord(Sensor sensor, String breathData, String breathWaveData1, 
			String breathWaveData2, String batteryLevel, String breathStatus, Date lastUpdated) {
		this.sensor = sensor;
		this.breathData = breathData;
		this.breathWaveData1 = breathWaveData1;
		this.breathWaveData2 = breathWaveData2;
		this.batteryLevel = batteryLevel;
		this.breathStatus = breathStatus;
		this.lastUpdated = lastUpdated;
	}
	
	public RtHeartRhythmRecord(long rtHeartRhythmRecordId, Sensor sensor, String breathData,
			String breathWaveData1, String breathWaveData2, String batteryLevel, String breathStatus, Date lastUpdated) {
		this.rtHeartRhythmRecordId = rtHeartRhythmRecordId;
		this.sensor = sensor;
		this.breathData = breathData;
		this.breathWaveData1 = breathWaveData1;
		this.breathWaveData2 = breathWaveData2;
		this.batteryLevel = batteryLevel;
		this.breathStatus = breathStatus;
		this.lastUpdated = lastUpdated;
	}

	@SequenceGenerator(name="rt_heart_rhythm_record_seq", sequenceName="rt_heart_rhythm_record_rt_heart_rhythm_record_id_seq")	@GeneratedValue(generator="rt_heart_rhythm_record_seq")	@Id

	@Column(name = "rt_heart_rhythm_record_id", unique = true, nullable = false)
	public long getRtHeartRhythmRecordId() {
		return this.rtHeartRhythmRecordId;
	}

	public void setRtHeartRhythmRecordId(long rtHeartRhythmRecordId) {
		this.rtHeartRhythmRecordId = rtHeartRhythmRecordId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_id", nullable = false)
	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
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
