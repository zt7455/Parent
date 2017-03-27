package com.shengliedu.parent.activity.bean;

import java.io.Serializable;

/**
 * Created by zt10 on 2017/2/20.
 */

public class SynClass implements Serializable{

//    id: 85,
//    date: 1488158432,
//    classroom: 141,
//    courseware: 0,
//    user: 40165,
//    hour: 4700,
//    timetable: 0,
//    hwComplete: 0,
//    hwQuality: 0,
//    subject: 3,
//    note: "",
//    behavior: 1,
//    behaviorNote: "",
//    evaluateScore: 0,
//    semester: 30000,
//    school: 2,
//    publishTime: null,
//    closeTime: null,
//    name: "第4700课时"
    public int id;
    public String name;
    public String answer;
    public String dateStr;
    public int classroom;
    public int subject;
    public int user;
    public int hour;
    public long date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SynClass synClass = (SynClass) o;

        return dateStr != null ? dateStr.equals(synClass.dateStr) : synClass.dateStr == null;

    }

    @Override
    public int hashCode() {
        return dateStr != null ? dateStr.hashCode() : 0;
    }
}
