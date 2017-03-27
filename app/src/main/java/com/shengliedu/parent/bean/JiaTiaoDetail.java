package com.shengliedu.parent.bean;

import java.util.List;

public class JiaTiaoDetail {
	public String id;
	public Long stime;
	public Long etime;
	public int type;
	public String content;
	public String classroom;
	public Long addTime;
	public String approve;
	public String approveTime;
	public int part;
	public String semester;
	public ChildInfo child;
	public UserInfo parent;
	public Teacher teacher;
	public List<BeanJie> scopeList;
}
