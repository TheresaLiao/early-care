package org.itri.view.login;

import java.io.Serializable;

import org.itri.view.login.dao.UserCredential;
import org.itri.view.login.dao.UserInfoServiceHibernateImpl;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

public class AuthenticationServiceImpl implements AuthenticationService, Serializable {
	private static final long serialVersionUID = 1L;

	UserInfoService userInfoService = new UserInfoServiceHibernateImpl();

	public boolean login(String username, String pd) {
		// Check user name
		User user = userInfoService.findUser(username, pd);
		if (user == null) {
			System.out.println("user is null");
			return false;
		}

		// set username
		Session sess = Sessions.getCurrent();
		UserCredential cre = new UserCredential(user.getAccount(), user.getPassword(), user.getPatientId());
		sess.setAttribute("userCredential", cre);
		return true;
	}

	public void logout() {
		Session sess = Sessions.getCurrent();
		sess.removeAttribute("userCredential");
	}

	public UserCredential getUserCredential() {
		Session sess = Sessions.getCurrent();
		UserCredential cre = (UserCredential) sess.getAttribute("userCredential");
		if (cre == null) {
			cre = new UserCredential();
			sess.setAttribute("userCredential", cre);
		}
		return cre;
	}

}
