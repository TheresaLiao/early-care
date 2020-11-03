package org.itri.view.humanhealth.hibernate;
// Generated 2020/10/29 �U�� 04:58:12 by Hibernate Tools 4.0.0.Final
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Gateway generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "gateway")
public class Gateway implements java.io.Serializable {

	private long gatewayId;
	private String gatewayMac;
	private String gatewayDeviceStatus;
	private int updateFreqSec;
	private Date lastActive;
	private Set<Gateway2sensor> gateway2sensors = new HashSet<Gateway2sensor>(0);
	private Set<Patient> patients = new HashSet<Patient>(0);

	public Gateway() {
	}

	public Gateway(long gatewayId, String gatewayMac, int updateFreqSec) {
		this.gatewayId = gatewayId;
		this.gatewayMac = gatewayMac;
		this.updateFreqSec = updateFreqSec;
	}

	public Gateway(long gatewayId, String gatewayMac, String gatewayDeviceStatus, int updateFreqSec, Date lastActive,
			Set<Gateway2sensor> gateway2sensors, Set<Patient> patients) {
		this.gatewayId = gatewayId;
		this.gatewayMac = gatewayMac;
		this.gatewayDeviceStatus = gatewayDeviceStatus;
		this.updateFreqSec = updateFreqSec;
		this.lastActive = lastActive;
		this.gateway2sensors = gateway2sensors;
		this.patients = patients;
	}

	@SequenceGenerator(name="gateway_seq", sequenceName="gateway_gateway_id_seq")	@GeneratedValue(generator="gateway_seq")	@Id

	@Column(name = "gateway_id", unique = true, nullable = false)
	public long getGatewayId() {
		return this.gatewayId;
	}

	public void setGatewayId(long gatewayId) {
		this.gatewayId = gatewayId;
	}

	@Column(name = "gateway_mac", nullable = false, length = 128)
	public String getGatewayMac() {
		return this.gatewayMac;
	}

	public void setGatewayMac(String gatewayMac) {
		this.gatewayMac = gatewayMac;
	}

	@Column(name = "gateway_device_status", length = 16)
	public String getGatewayDeviceStatus() {
		return this.gatewayDeviceStatus;
	}

	public void setGatewayDeviceStatus(String gatewayDeviceStatus) {
		this.gatewayDeviceStatus = gatewayDeviceStatus;
	}

	@Column(name = "update_freq_sec", nullable = false)
	public int getUpdateFreqSec() {
		return this.updateFreqSec;
	}

	public void setUpdateFreqSec(int updateFreqSec) {
		this.updateFreqSec = updateFreqSec;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_active", length = 29)
	public Date getLastActive() {
		return this.lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gateway")
	public Set<Gateway2sensor> getGateway2sensors() {
		return this.gateway2sensors;
	}

	public void setGateway2sensors(Set<Gateway2sensor> gateway2sensors) {
		this.gateway2sensors = gateway2sensors;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gateway")
	public Set<Patient> getPatients() {
		return this.patients;
	}

	public void setPatients(Set<Patient> patients) {
		this.patients = patients;
	}

}
