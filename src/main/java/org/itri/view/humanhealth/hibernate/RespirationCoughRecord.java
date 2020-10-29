package org.itri.view.humanhealth.hibernate;
// Generated 2020/4/24 �U�� 08:59:27 by Hibernate Tools 4.0.0.Final
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;import javax.persistence.GenerationType;
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
 * RespirationCoughRecord generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "respiration_cough_record")
public class RespirationCoughRecord implements java.io.Serializable {

	private long respirationCoughRecordId;
	private Patient patient;
	private Sensor sensor;
	private String respiratoryRateData;
	private String coughRateData;
	private String respiratoryRateStatus;
	private String coughRateStatus;
	private Date timeCreated;

	public RespirationCoughRecord() {
	}

	public RespirationCoughRecord(long respirationCoughRecordId, Patient patient, Sensor sensor,
			String respiratoryRateData, String coughRateData, String respiratoryRateStatus, String coughRateStatus,
			Date timeCreated) {
		this.respirationCoughRecordId = respirationCoughRecordId;
		this.patient = patient;
		this.sensor = sensor;
		this.respiratoryRateData = respiratoryRateData;
		this.coughRateData = coughRateData;
		this.respiratoryRateStatus = respiratoryRateStatus;
		this.coughRateStatus = coughRateStatus;
		this.timeCreated = timeCreated;
	}

	@SequenceGenerator(name="respiration_cough_record_seq", sequenceName="respiration_cough_record_respiration_cough_record_id_seq", allocationSize=1)	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="respiration_cough_record_seq")	@Id

	@Column(name = "respiration_cough_record_id", unique = true, nullable = false)
	public long getRespirationCoughRecordId() {
		return this.respirationCoughRecordId;
	}

	public void setRespirationCoughRecordId(long respirationCoughRecordId) {
		this.respirationCoughRecordId = respirationCoughRecordId;
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

	@Column(name = "respiratory_rate_data", nullable = false, length = 64)
	public String getRespiratoryRateData() {
		return this.respiratoryRateData;
	}

	public void setRespiratoryRateData(String respiratoryRateData) {
		this.respiratoryRateData = respiratoryRateData;
	}

	@Column(name = "cough_rate_data", nullable = false, length = 64)
	public String getCoughRateData() {
		return this.coughRateData;
	}

	public void setCoughRateData(String coughRateData) {
		this.coughRateData = coughRateData;
	}

	@Column(name = "respiratory_rate_status", nullable = false, length = 32)
	public String getRespiratoryRateStatus() {
		return this.respiratoryRateStatus;
	}

	public void setRespiratoryRateStatus(String respiratoryRateStatus) {
		this.respiratoryRateStatus = respiratoryRateStatus;
	}

	@Column(name = "cough_rate_status", nullable = false, length = 32)
	public String getCoughRateStatus() {
		return this.coughRateStatus;
	}

	public void setCoughRateStatus(String coughRateStatus) {
		this.coughRateStatus = coughRateStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time_created", nullable = false, length = 29)
	public Date getTimeCreated() {
		return this.timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

}
