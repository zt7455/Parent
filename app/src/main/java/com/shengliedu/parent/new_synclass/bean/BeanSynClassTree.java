package com.shengliedu.parent.new_synclass.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zt10 on 2017/2/20.
 */

public class BeanSynClassTree implements Serializable{
//    catalogName: "信息",
//    catalogId: 169,
    public List<BeanSynClass> beanSynClasses;
    public String catalogName;
    public int catalogId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanSynClassTree that = (BeanSynClassTree) o;

        return catalogId == that.catalogId;

    }

    @Override
    public int hashCode() {
        return catalogId;
    }
}
