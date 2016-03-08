package com.lixue.aibei.changeskinlib.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lixue.aibei.changeskinlib.ResourceManager;
import com.lixue.aibei.changeskinlib.SkinManager;
import com.lixue.aibei.changeskinlib.utils.L;

/**
 * Created by Administrator on 2016/2/25.
 */
public enum SkinAttrType {
    BACKGROUND("background") {
        @Override
        public void apply(View view, String resName) {
            L.e("background,resName:" + resName);
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable == null) return;
            view.setBackgroundDrawable(drawable);
        }
    },COLOR("textColor"){
        @Override
        public void apply(View view, String resName) {
            L.e("textColor,resName:" + resName);
            ColorStateList colorlist = getResourceManager().getColorStateList(resName);
            if (colorlist == null) return;
            ((TextView) view).setTextColor(colorlist);
        }
    },SRC("src"){
        @Override
        public void apply(View view, String resName) {
            L.e("src,resName:" + resName);
            Drawable drawable = getResourceManager().getDrawableByName(resName);
            if (drawable == null) return;
            ((ImageView) view).setImageDrawable(drawable);
        }
    },DIVIDER("divider")
    {
        @Override
        public void apply(View view, String resName) {
        if (view instanceof ListView) {
            L.e("divider,resName:" + resName);
            Drawable divider = getResourceManager().getDrawableByName(resName);
            if (divider == null) return;
            ((ListView) view).setDivider(divider);
        }
    }
    };
    private String attrType;
    SkinAttrType(String attrType){
        this.attrType = attrType;
    }
    public String getAttrType(){
        return attrType;
    }
    public abstract void apply(View view, String resName);

    public ResourceManager getResourceManager() {
        return SkinManager.getInstance().getResourceManager();

    }
}
