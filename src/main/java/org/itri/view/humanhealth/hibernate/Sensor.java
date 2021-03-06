package org.itri.view.humanhealth.hibernate;
// Generated 2020/10/29 �U�� 04:58:12 by Hibernate Tools 4.0.0.Final
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Sensor generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "sensor")
public class Sensor implements java.io.Serializable {

	private long sensorId;
	private SensorType sensorType;
	private String deviceId;
	private String sensorName;
	private String sensorNameChinese;
	private String sensorDeviceStatus;
	private Set<SensorThreshold> sensorThresholds = new HashSet<SensorThreshold>(0);
	private Set<Combination> combinations = new HashSet<Combination>(0);
	private Set<RtHeartRhythmRecord> rtHeartRhythmRecords = new HashSet<RtHeartRhythmRecord>(0);
	private Set<PmFiveRecord> pmFiveRecords = new HashSet<PmFiveRecord>(0);
	private Set<RtMattressRecord> rtMattressRecords = new HashSet<RtMattressRecord>(0);
	private Set<Gateway2sensor> gateway2sensors = new HashSet<Gateway2sensor>(0);
	private Set<RtPmFiveRecord> rtPmFiveRecords = new HashSet<RtPmFiveRecord>(0);
	private Set<RtRespirationCoughRecord> rtRespirationCoughRecords = new HashSet<RtRespirationCoughRecord>(0);
	private Set<MattressRecord> mattressRecords = new HashSet<MattressRecord>(0);
	private Set<RtOximeterRecord> rtOximeterRecords = new HashSet<RtOximeterRecord>(0);
	private Set<Sensor2healthType> sensor2healthTypes = new HashSet<Sensor2healthType>(0);
	private Set<OximeterRecord> oximeterRecords = new HashSet<OximeterRecord>(0);
	private Set<RtTempPadRecord> rtTempPadRecords = new HashSet<RtTempPadRecord>(0);
	private Set<TempPadRecord> tempPadRecords = new HashSet<TempPadRecord>(0);
	private Set<HeartRhythmRecord> heartRhythmRecords = new HashSet<HeartRhythmRecord>(0);
	private Set<RespirationCoughRecord> respirationCoughRecords = new HashSet<RespirationCoughRecord>(0);

	public Sensor() {
	}

	public Sensor(SensorType sensorType, String deviceId, String sensorName) {
		this.sensorType = sensorType;
		this.deviceId = deviceId;
		this.sensorName = sensorName;
	}
	
	public Sensor(long sensorId, SensorType sensorType, String deviceId, String sensorName, 
			String sensorNameChinese) {
		this.sensorId = sensorId;
		this.sensorType = sensorType;
		this.deviceId = deviceId;
		this.sensorName = sensorName;
		this.sensorNameChinese = sensorNameChinese;
	}

	public Sensor(long sensorId, SensorType sensorType, String deviceId, String sensorName, String sensorNameChinese,
			String sensorDeviceStatus,
			Set<SensorThreshold> sensorThresholds, Set<Combination> combinations,
			Set<RtHeartRhythmRecord> rtHeartRhythmRecords, Set<PmFiveRecord> pmFiveRecords,
			Set<RtMattressRecord> rtMattressRecords, Set<Gateway2sensor> gateway2sensors,
			Set<RtPmFiveRecord> rtPmFiveRecords, Set<RtRespirationCoughRecord> rtRespirationCoughRecords,
			Set<MattressRecord> mattressRecords, Set<RtOximeterRecord> rtOximeterRecords,
			Set<Sensor2healthType> sensor2healthTypes, Set<OximeterRecord> oximeterRecords,
			Set<RtTempPadRecord> rtTempPadRecords, Set<TempPadRecord> tempPadRecords,
			Set<HeartRhythmRecord> heartRhythmRecords, Set<RespirationCoughRecord> respirationCoughRecords) {
		this.sensorId = sensorId;
		this.sensorType = sensorType;
		this.deviceId = deviceId;
		this.sensorName = sensorName;
		this.sensorNameChinese = sensorNameChinese;
		this.sensorDeviceStatus = sensorDeviceStatus;
		this.sensorThresholds = sensorThresholds;
		this.combinations = combinations;
		this.rtHeartRhythmRecords = rtHeartRhythmRecords;
		this.pmFiveRecords = pmFiveRecords;
		this.rtMattressRecords = rtMattressRecords;
		this.gateway2sensors = gateway2sensors;
		this.rtPmFiveRecords = rtPmFiveRecords;
		this.rtRespirationCoughRecords = rtRespirationCoughRecords;
		this.mattressRecords = mattressRecords;
		this.rtOximeterRecords = rtOximeterRecords;
		this.sensor2healthTypes = sensor2healthTypes;
		this.oximeterRecords = oximeterRecords;
		this.rtTempPadRecords = rtTempPadRecords;
		this.tempPadRecords = tempPadRecords;
		this.heartRhythmRecords = heartRhythmRecords;
		this.respirationCoughRecords = respirationCoughRecords;
	}

	@SequenceGenerator(name="sensor_seq", sequenceName="sensor_sensor_id_seq")	@GeneratedValue(generator="sensor_seq")	@Id

	@Column(name = "sensor_id", unique = true, nullable = false)
	public long getSensorId() {
		return this.sensorId;
	}

	public void setSensorId(long sensorId) {
		this.sensorId = sensorId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_type_id", nullable = false)
	public SensorType getSensorType() {
		return this.sensorType;
	}

	public void setSensorType(SensorType sensorType) {
		this.sensorType = sensorType;
	}

	@Column(name = "device_id", nullable = false, length = 128)
	public String getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "sensor_name", nullable = false, length = 256)
	public String getSensorName() {
		return this.sensorName;
	}

	public void setSensorName(String sensorName) {
		this.sensorName = sensorName;
	}
	
	@Column(name = "sensor_name_chinese", length = 256)
	public String getSensorNameChinese() {
		return this.sensorNameChinese;
	}

	public void setSensorNameChinese(String sensorNameChinese) {
		this.sensorNameChinese = sensorNameChinese;
	}

	@Column(name = "sensor_device_status", length = 16)
	public String getSensorDeviceStatus() {
		return this.sensorDeviceStatus;
	}

	public void setSensorDeviceStatus(String sensorDeviceStatus) {
		this.sensorDeviceStatus = sensorDeviceStatus;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<SensorThreshold> getSensorThresholds() {
		return this.sensorThresholds;
	}

	public void setSensorThresholds(Set<SensorThreshold> sensorThresholds) {
		this.sensorThresholds = sensorThresholds;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<Combination> getCombinations() {
		return this.combinations;
	}

	public void setCombinations(Set<Combination> combinations) {
		this.combinations = combinations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<RtHeartRhythmRecord> getRtHeartRhythmRecords() {
		return this.rtHeartRhythmRecords;
	}

	public void setRtHeartRhythmRecords(Set<RtHeartRhythmRecord> rtHeartRhythmRecords) {
		this.rtHeartRhythmRecords = rtHeartRhythmRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<PmFiveRecord> getPmFiveRecords() {
		return this.pmFiveRecords;
	}

	public void setPmFiveRecords(Set<PmFiveRecord> pmFiveRecords) {
		this.pmFiveRecords = pmFiveRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<RtMattressRecord> getRtMattressRecords() {
		return this.rtMattressRecords;
	}

	public void setRtMattressRecords(Set<RtMattressRecord> rtMattressRecords) {
		this.rtMattressRecords = rtMattressRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<Gateway2sensor> getGateway2sensors() {
		return this.gateway2sensors;
	}

	public void setGateway2sensors(Set<Gateway2sensor> gateway2sensors) {
		this.gateway2sensors = gateway2sensors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<RtPmFiveRecord> getRtPmFiveRecords() {
		return this.rtPmFiveRecords;
	}

	public void setRtPmFiveRecords(Set<RtPmFiveRecord> rtPmFiveRecords) {
		this.rtPmFiveRecords = rtPmFiveRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<RtRespirationCoughRecord> getRtRespirationCoughRecords() {
		return this.rtRespirationCoughRecords;
	}

	public void setRtRespirationCoughRecords(Set<RtRespirationCoughRecord> rtRespirationCoughRecords) {
		this.rtRespirationCoughRecords = rtRespirationCoughRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<MattressRecord> getMattressRecords() {
		return this.mattressRecords;
	}

	public void setMattressRecords(Set<MattressRecord> mattressRecords) {
		this.mattressRecords = mattressRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<RtOximeterRecord> getRtOximeterRecords() {
		return this.rtOximeterRecords;
	}

	public void setRtOximeterRecords(Set<RtOximeterRecord> rtOximeterRecords) {
		this.rtOximeterRecords = rtOximeterRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<Sensor2healthType> getSensor2healthTypes() {
		return this.sensor2healthTypes;
	}

	public void setSensor2healthTypes(Set<Sensor2healthType> sensor2healthTypes) {
		this.sensor2healthTypes = sensor2healthTypes;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<OximeterRecord> getOximeterRecords() {
		return this.oximeterRecords;
	}

	public void setOximeterRecords(Set<OximeterRecord> oximeterRecords) {
		this.oximeterRecords = oximeterRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<RtTempPadRecord> getRtTempPadRecords() {
		return this.rtTempPadRecords;
	}

	public void setRtTempPadRecords(Set<RtTempPadRecord> rtTempPadRecords) {
		this.rtTempPadRecords = rtTempPadRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<TempPadRecord> getTempPadRecords() {
		return this.tempPadRecords;
	}

	public void setTempPadRecords(Set<TempPadRecord> tempPadRecords) {
		this.tempPadRecords = tempPadRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<HeartRhythmRecord> getHeartRhythmRecords() {
		return this.heartRhythmRecords;
	}

	public void setHeartRhythmRecords(Set<HeartRhythmRecord> heartRhythmRecords) {
		this.heartRhythmRecords = heartRhythmRecords;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sensor")
	public Set<RespirationCoughRecord> getRespirationCoughRecords() {
		return this.respirationCoughRecords;
	}

	public void setRespirationCoughRecords(Set<RespirationCoughRecord> respirationCoughRecords) {
		this.respirationCoughRecords = respirationCoughRecords;
	}

}
