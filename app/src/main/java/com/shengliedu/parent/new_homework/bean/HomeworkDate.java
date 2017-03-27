package com.shengliedu.parent.new_homework.bean;

/**
 * Created by zt10 on 2017/2/21.
 */

public class HomeworkDate {
//    id: 159249,
//    activity: 536,
//    student: 47250,
//    left: 3,
//    comment: 0,
//    hasView: 0,
//    example: 0,
//    note: null,
//    parentComment: 0,
//    commentNote: null,
//    date: 1487645649
    public long date;
    public int activity;
    public int student;
    public int left;
    public String dateStr;
    public int nooversize;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HomeworkDate that = (HomeworkDate) o;

        return dateStr.equals(that.dateStr);

    }

    @Override
    public int hashCode() {
        return dateStr.hashCode();
    }
}
