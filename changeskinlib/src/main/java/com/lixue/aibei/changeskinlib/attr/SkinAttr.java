package com.lixue.aibei.changeskinlib.attr;

import android.view.View;

/**
 * Created by Administrator on 2016/2/25.
 */
public class SkinAttr {
    private String resName;
    private SkinAttrType attrType;

    public SkinAttr(SkinAttrType skinAttrType,String resName){
        this.resName = resName;
        this.attrType = skinAttrType;
    }

    public void apply(View view){
        attrType.apply(view,resName);
    }
}
