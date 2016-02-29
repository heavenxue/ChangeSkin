package com.lixue.aibei.changeskinlib.attr;

import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public class SkinView {
    private List<SkinAttr> skinAttrs;
    private View view;

    public SkinView(View view, List<SkinAttr> skinAttrs){
        this.view = view;
        this.skinAttrs = skinAttrs;
    }

    public void apply(){
        if (skinAttrs != null && view != null){
            for (SkinAttr attr : skinAttrs){
                attr.apply(view);
            }
        }
    }
}
