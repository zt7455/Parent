package com.shengliedu.parent.util;

import java.util.List;

import com.shengliedu.parent.bean.ClassRoom;

public class ClassRoomTools {
	public static String getClassRoomName(int id,List<ClassRoom> classes) {
		for (int i = 0; i < classes.size(); i++) {
			ClassRoom classRoom=classes.get(i);
			if (classRoom.id==id) {
				return classRoom.name;
			}
		}
		return null;
	}
}
