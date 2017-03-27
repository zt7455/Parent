package com.shengliedu.parent.bean;

import java.io.Serializable;
import java.util.List;


public class Semesters {
	public int id;
	public String startdate;
	public String enddate;
	public String name;
	public int state;
	public String part;
	public List<Chengji> subjectArr;
}
