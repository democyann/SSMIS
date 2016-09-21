package com.systop.common.modules.desktop;

import java.util.ArrayList;
import java.util.List;

/**
 * 桌面常量类
 * @author ZhouLihui
 *
 */
public final class DesktopConstants {

	private DesktopConstants() {
	}

	public static final String SKIN_JLAYOUT = "jlayout";
	/**
	 * 系统角色列表
	 */
	public static final List<String> MENU_COLORS = new ArrayList<String>(12);
	
	static {
		MENU_COLORS.add("#55ABDC");
		MENU_COLORS.add("#FF971E");
		MENU_COLORS.add("#A0C750");
		MENU_COLORS.add("#6A75D1");
		MENU_COLORS.add("#C66964");
		MENU_COLORS.add("#B352A3");
		MENU_COLORS.add("#EAC100");
		MENU_COLORS.add("#7F638C");
		MENU_COLORS.add("#616130");
		MENU_COLORS.add("#ADADAD");
		MENU_COLORS.add("#63C53C");
		MENU_COLORS.add("#FF0080");
	}
}
