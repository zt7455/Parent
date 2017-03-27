package com.shengliedu.parent.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zt10 on 2017/2/20.
 */

public class HomeworkTree implements Serializable{
//    part: 3,
//    subject: 3
    public List<Homework> homeworks;
    public String subjectpart;
    public String unitSectionHour;
    public int subject;
    public int part;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HomeworkTree that = (HomeworkTree) o;

        if (subject != that.subject) return false;
        return part == that.part;

    }

    @Override
    public int hashCode() {
        int result = subject;
        result = 31 * result + part;
        return result;
    }
}
