package com.shengliedu.parent.bean;

import java.util.List;

/**
 * @author zt
 */
public class UserInfo {
//	  "remember_token": "L1yRKwWQbhxavmejGBjs1SGgaA9aAj0F1pqD1F7qXVambU4j3YBwTVXn1V4X",
//      "sex": 1,
//      "birth": "1980-01-01",
//      "hometown": "杨各庄镇包各庄",
//      "image": "data/user/images/default.png",
//      "idcard": "130203198001120342",
//      "inSchoolDate": "2014-09-01",
//      "nfcId": "57299904",
//      "type": 1,
//      "password": "$2y$10$NqMoDwuWp6YQDH7tctUjdOvH8JrMyxFPN5aadCPK94ae3tqy2gFpW",
//      "nation": "汉",
//      "id": 70,
//      "updated_at": "2015-10-29 18:54:40",
//      "email": "",
//      "name": "70",
//      "created_at": "0000-00-00 00:00:00",
//      "realname": "白小香",
//      "votenumber": 0,
//      "opassword": "123456"
	public String id;
	public String name;
	public String realname;
	public String image;
	public String hometown;
	public int sex;
	public int type;
	public String nation;
	public String nickname;
	public String signature; 
	public List<GradeSubjectArr> gradeSubjectArr;
	public List<IdName> gradeDic;
	public List<IdName> subjectDic;	
	public List<IdName> editionVersionArr;
	public List<IdName> schoolstageArr;
	public List<Semester> semesterArr;
}
