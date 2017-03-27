package com.shengliedu.parent.util;

import com.shengliedu.parent.R;
import com.shengliedu.parent.bean.ConsoleMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConsoleMenus {
	private static ConsoleMenu consoleMenu1 = new ConsoleMenu(100,
			R.mipmap.menu_console_01, "作业");
	private static ConsoleMenu consoleMenu2 = new ConsoleMenu(200,
			R.mipmap.menu_console_02, "同步课堂");
	private static ConsoleMenu consoleMenu3 = new ConsoleMenu(300,
			R.mipmap.menu_console_03, "请假条");
	private static ConsoleMenu consoleMenu4 = new ConsoleMenu(400,
			R.mipmap.menu_console_04, "教学资源");
//	private static ConsoleMenu consoleMenu5 = new ConsoleMenu(500,
//			R.mipmap.menu_console_05, "通知");
	private static ConsoleMenu consoleMenu6 = new ConsoleMenu(600,
			R.mipmap.menu_console_06, "三图一表");
	private static ConsoleMenu consoleMenu7 = new ConsoleMenu(700,
			R.mipmap.menu_console_07, "各科成绩");
	private static ConsoleMenu consoleMenu8 = new ConsoleMenu(800,
			R.mipmap.menu_console_08, "课堂回答");
	private static ConsoleMenu consoleMenu9 = new ConsoleMenu(900,
			R.mipmap.menu_console_09, "在校纪律");
//	private static ConsoleMenu consoleMenu10 = new ConsoleMenu(1000,
//			R.mipmap.menu_console_10, "作业表现");
	private static ConsoleMenu consoleMenu11 = new ConsoleMenu(1100,
			R.mipmap.menu_console_11, "异动报告");
	private static ConsoleMenu consoleMenu12 = new ConsoleMenu(1200,
			R.mipmap.menu_console_12, "出勤情况");
	private static ConsoleMenu consoleMenu13 = new ConsoleMenu(1300,
			R.mipmap.menu_console_13, "知识短板");
//	private static ConsoleMenu consoleMenu14 = new ConsoleMenu(1400,
//			R.mipmap.menu_console_14, "错题集");
	private static List<ConsoleMenu> list = new ArrayList<ConsoleMenu>();

	public static List<ConsoleMenu> init() {
		list.clear();
		list.add(consoleMenu1);
		list.add(consoleMenu2);
		list.add(consoleMenu3);
		list.add(consoleMenu4);
//		list.add(consoleMenu5);
		list.add(consoleMenu6);
		list.add(consoleMenu7);
		list.add(consoleMenu8);
		list.add(consoleMenu9);
//		list.add(consoleMenu10);
		list.add(consoleMenu11);
		list.add(consoleMenu12);
		list.add(consoleMenu13);
//		list.add(consoleMenu14);
		return list;
	}
	public static List<ConsoleMenu> removeListDuplicateObject(List<ConsoleMenu> list1,List<ConsoleMenu> list2) {
		list1.addAll(list2);
		Set<ConsoleMenu> set=new HashSet<ConsoleMenu>(list1);
		list1.clear();
		list1.addAll(set);
		Collections.sort(list1,comparator);
		return list1;
	}
	
	 private static Comparator<ConsoleMenu> comparator = new Comparator<ConsoleMenu>() {
         public int compare(ConsoleMenu s1, ConsoleMenu s2) {
                 return s1.number - s2.number;
         }
     };
}
