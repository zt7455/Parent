package com.shengliedu.parent.bean;

import java.io.Serializable;
import java.util.List;

public class HomeWorkBean implements Serializable {

	public String id = "";
	public String name = "";
	public String class_ = "";
	public String order = "";
	public String part = "";
	public String created_at = "";
	public String updated_at = "";
	public String group = "";
	public String show = "";
	public List<HomeWork> homework;
	public Teacher teacher;
	public String homeworkComment = "";
	public String parentComment = "";
}
