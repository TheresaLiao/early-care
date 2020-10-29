package org.itri.view.login;

import java.util.Map;

import org.itri.view.login.dao.UserCredential;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

public class AuthenticationInit implements Initiator {

	// services
	AuthenticationService authService = new AuthenticationServiceImpl();

	public void doInit(Page page, Map<String, Object> args) throws Exception {

		UserCredential cre = authService.getUserCredential();
		if (cre == null || cre.isAnonymous()) {
			System.out.println("cre == null || cre.isAnonymous()");
			Executions.sendRedirect("/login.zul");
			return;
		}
	}
}