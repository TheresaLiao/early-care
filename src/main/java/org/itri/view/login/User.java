/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package org.itri.view.login;

import java.io.Serializable;
import java.util.Date;

/**
 * User entity
 */
public class User implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private long patientId;
	private String account;
	private String password;
//	private String gatewayDeviceStatus;

	public User() {
	}

	public User(String account, String password) {
		this.account = account;
		this.password = password;
	}

	public String getAccount() {
		return account;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		return true;
	}

	public static User clone(User user) {
		try {
			return (User) user.clone();
		} catch (CloneNotSupportedException e) {
			// not possible
		}
		return null;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

//	public String getGatewayDeviceStatus() {
//		return gatewayDeviceStatus;
//	}
//
//	public void setGatewayDeviceStatus(String gatewayDeviceStatus) {
//		this.gatewayDeviceStatus = gatewayDeviceStatus;
//	}

	

}
