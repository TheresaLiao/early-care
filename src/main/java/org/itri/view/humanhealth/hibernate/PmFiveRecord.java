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
 * PmFiveRecord generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "pm_five_record")
public class PmFiveRecord implements java.io.Serializable {

	private long pmFiveRecordId;
	private Sensor sensor;
	private String pmFiveData;
	private String pmFiveStatus;
	private Date timeCreated;

	public PmFiveRecord() {
	}

	public PmFiveRecord(long pmFiveRecordId, Sensor sensor, String pmFiveData, String pmFiveStatus, Date timeCreated) {
		this.pmFiveRecordId = pmFiveRecordId;
		this.sensor = sensor;
		this.pmFiveData = pmFiveData;
		this.pmFiveStatus = pmFiveStatus;
		this.timeCreated = timeCreated;
	}

	@SequenceGenerator(name="pm_five_record_seq", sequenceName="pm_five_record_pm_five_record_id_seq")	@GeneratedValue(generator="pm_five_record_seq")	@Id

	@Column(name = "pm_five_record_id", unique = true, nullable = false)
	public long getPmFiveRecordId() {
		return this.pmFiveRecordId;
	}

	public void setPmFiveRecordId(long pmFiveRecordId) {
		this.pmFiveRecordId = pmFiveRecordId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_id", nullable = false)
	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	@Column(name = "pm_five_data", nullable = false, length = 64)
	public String getPmFiveData() {
		return this.pmFiveData;
	}

	public void setPmFiveData(String pmFiveData) {
		this.pmFiveData = pmFiveData;
	}

	@Column(name = "pm_five_status", nullable = false, length = 32)
	public String getPmFiveStatus() {
		return this.pmFiveStatus;
	}

	public void setPmFiveStatus(String pmFiveStatus) {
		this.pmFiveStatus = pmFiveStatus;
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
