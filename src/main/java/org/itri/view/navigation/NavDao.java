package org.itri.view.navigation;

import java.util.*;

public class NavDao {
	private static List<Menu> menuList = new LinkedList<>();

	static {
		initMenus();
	}

	static public void initMenus() {

		Menu menuCreatePatient = new Menu("新增病人", "z-icon-home");
		menuCreatePatient.setPath(NavigationMdel.DASHBOARD_CREATEPATIENT_ZUL);
		menuList.add(menuCreatePatient);

		Menu menuCreateRoom = new Menu("新增病床", "z-icon-home");
		menuCreateRoom.setPath(NavigationMdel.DASHBOARD_CREATEROOM_ZUL);
		menuList.add(menuCreateRoom);

//		Menu menuHuamanChartSet = new Menu("嚴重病床", "z-icon-home");
//		menuHuamanChartSet.setPath(NavigationMdel.DASHBOARD_HUMANCHARTSET_ZUL);
//		menuList.add(menuHuamanChartSet);

		Menu menuHuamanChartSet2 = new Menu("嚴重病床", "z-icon-home");
		menuHuamanChartSet2.setPath(NavigationMdel.DASHBOARD_HUMANCHARTSET_ZUL2);
		menuList.add(menuHuamanChartSet2);
	}

	static public List<Menu> queryMenu() {
		return menuList;
	}
}
