/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package org.itri.view.login.dao;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserCredential implements Serializable {
	private static final long serialVersionUID = 1L;

	private String account;
	private String name;
	private long patientId;
//	private String gatewayDeviceStatus;

	Set<String> roles = new HashSet<String>();

	public UserCredential(String account, String name, long patientId) {
		this.account = account;
		this.name = name;
		this.patientId = patientId;
//		this.gatewayDeviceStatus = gatewayDeviceStatus;
	}

	public UserCredential() {
		this.account = "anonymous";
		this.name = "Anonymous";
		roles.add("anonymous");
	}

	public boolean isAnonymous() {
		return hasRole("anonymous") || "anonymous".equals(account);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasRole(String role) {
		return roles.contains(role);
	}

	public void addRole(String role) {
		roles.add(role);
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
