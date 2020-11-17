package org.itri.view.navigation;

import java.util.*;

public class NavDao {
	private static List<Menu> menuList = new LinkedList<>();

	static {
		initMenus();
	}

	static public void initMenus() {

		Menu menuHuamanChartSet = new Menu("�Y���f�H", "z-icon-home");
		menuHuamanChartSet.setPath(NavigationMdel.DASHBOARD_HUMANCHARTSET_ZUL);
		menuList.add(menuHuamanChartSet);

		Menu menuCreatePatient = new Menu("�s�W�f�H", "z-icon-home");
		menuCreatePatient.setPath(NavigationMdel.DASHBOARD_CREATEPATIENT_ZUL);
		menuList.add(menuCreatePatient);

	}

	static public List<Menu> queryMenu() {
		return menuList;
	}
}
