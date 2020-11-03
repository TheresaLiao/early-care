package org.itri.view.humanhealth.hibernate;
// Generated 2020/10/29 �U�� 04:58:12 by Hibernate Tools 4.0.0.Final
import javax.persistence.GeneratedValue;import javax.persistence.SequenceGenerator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Gateway2sensor generated by hbm2java
 */
@SuppressWarnings("serial")@Entity
@Table(name = "gateway2sensor")
public class Gateway2sensor implements java.io.Serializable {

	private long gateway2sensorId;
	private Gateway gateway;
	private Sensor sensor;

	public Gateway2sensor() {
	}

	public Gateway2sensor(Gateway gateway, Sensor sensor) {
		this.gateway = gateway;
		this.sensor = sensor;
	}
	
	public Gateway2sensor(long gateway2sensorId, Gateway gateway, Sensor sensor) {
		this.gateway2sensorId = gateway2sensorId;
		this.gateway = gateway;
		this.sensor = sensor;
	}

	@SequenceGenerator(name="gateway2sensor_seq", sequenceName="gateway2sensor_gateway2sensor_id_seq")	@GeneratedValue(generator="gateway2sensor_seq")	@Id

	@Column(name = "gateway2sensor_id", unique = true, nullable = false)
	public long getGateway2sensorId() {
		return this.gateway2sensorId;
	}

	public void setGateway2sensorId(long gateway2sensorId) {
		this.gateway2sensorId = gateway2sensorId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "gateway_id", nullable = false)
	public Gateway getGateway() {
		return this.gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sensor_id", nullable = false)
	public Sensor getSensor() {
		return this.sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

}
