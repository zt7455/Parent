package com.shengliedu.parent.bean;

import java.util.List;


public class ClassRoom  {
	public int id;
	public String name;
	public int grade;
	public int subject;
	public int classroom_type;
	public int type;
	public int editionVersion;
	public List<ClassRoom> classrooms;
	public List<ClassRoom> subjects;
	@Override
	public String toString() {
		return "ClassRoom [id=" + id + ", name=" + name + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClassRoom other = (ClassRoom) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
}
