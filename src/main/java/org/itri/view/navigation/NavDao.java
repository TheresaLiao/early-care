package org.itri.view.navigation;

import java.util.*;

public class NavDao {
	private static List<Menu> menuList = new LinkedList<>();

	static {
		initMenus();
	}

	static public void initMenus() {

		Menu menuHuamanChartSet = new Menu("�����Y���f�H", "z-icon-home");
		menuHuamanChartSet.setPath(NavigationMdel.DASHBOARD_HUMANCHARTSET_ZUL);
		menuList.add(menuHuamanChartSet);

//		Menu menuHuamanPersonChart = new Menu("�f�H�ӧO��T", "z-icon-home");
//		menuHuamanPersonChart.setPath(NavigationMdel.DASHBOARD_HUMANCHARTSET_PERSON_ZUL);
//		menuList.add(menuHuamanPersonChart);

	}

	static public List<Menu> queryMenu() {
		return menuList;
	}
}
