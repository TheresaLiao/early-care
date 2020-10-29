package org.itri.view.admin;

import java.io.UnsupportedEncodingException;

import java.security.NoSuchAlgorithmException;

import org.itri.view.login.AuthenticationService;
import org.itri.view.login.AuthenticationServiceImpl;
import org.itri.view.login.dao.UserCredential;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

public class LoginController extends SelectorComposer<Component> {

	AuthenticationService authService = new AuthenticationServiceImpl();
	private String deviceConnectionErrorNum = "3";

	@Wire
	Textbox account;
	@Wire
	Textbox password;
	@Wire
	Label errorLbl;

	@Listen("onClick=#login; onOK=#loginWin")
	public void doLogin() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println("doLogin");

		String username = account.getValue();
		String pd = password.getValue();

		// check empty
		if (username.isEmpty() || pd.isEmpty()) {
			errorLbl.setValue("帳密不可為空白！");
			return;
		}

		// check login
		errorLbl.setValue("");
		if (!authService.login(username, pd)) {
			System.out.println("account or password are not correct.");
			errorLbl.setValue("帳密錯誤！");
			return;
		}

		// check GatewayDeviceStatus connect
		UserCredential cre = authService.getUserCredential();
		if (!cre.getGatewayDeviceStatus().equals(deviceConnectionErrorNum)) {
			Messagebox.show("Gateway連接失敗，請聯繫系統人員。", "Warning", 1, "ERROR");
			return;
		}

		Executions.sendRedirect("/humanCare.zul");
	}
}
