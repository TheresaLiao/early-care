/* 
	Description:
		ZK Essentials
	History:
		Created by dennis

Copyright (C) 2012 Potix Corporation. All Rights Reserved.
*/
package org.itri.view.login;

public interface UserInfoService {

	/** find user by account **/
	public User findUser(String account, String pd);

//	/** update user **/
//	public User updateUser(User user);
}
