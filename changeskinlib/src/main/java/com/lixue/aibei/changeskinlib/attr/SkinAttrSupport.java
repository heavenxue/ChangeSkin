package com.lixue.aibei.changeskinlib.attr;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.lixue.aibei.changeskinlib.R;
import com.lixue.aibei.changeskinlib.constant.SkinConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public class SkinAttrSupport {

    /**通过属性名称得到type**/
    private static SkinAttrType getSupportAttrType(String attrName){
        for (SkinAttrType type : SkinAttrType.values()){
            if (type.getAttrType().equals(attrName)){
                return type;
            }
        }
        return null;
    }

    /**得到换肤视图**/
    public static List<SkinView> getSkinViews(Activity activity){
        List<SkinView> skinViews = new ArrayList<>();
        ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
        addSkinViews(content,skinViews);
        return skinViews;
    }

    /**换肤视图累加到list中**/
    public static void addSkinViews(View view,List<SkinView> skinViews){
        SkinView skinView = getSkinView(view);
        if (skinView != null) skinViews.add(skinView);

        if (view instanceof ViewGroup) {
            ViewGroup container = (ViewGroup) view;

            for (int i = 0, n = container.getChildCount(); i < n; i++) {
                View child = container.getChildAt(i);
                addSkinViews(child, skinViews);
            }
        }

    }

    /**获取换肤视图**/
    public static SkinView getSkinView(View view){
        Object tag = view.getTag(R.id.skin_tag_id);
        if (tag == null){
            tag = view.getTag();
        }
        if(tag == null) return null;
        if (!(tag instanceof String)) return  null;
        String tagStr = (String)tag;
        List<SkinAttr> skinAttrs = parseTag(tagStr);
        if (!skinAttrs.isEmpty()) {
            changeViewTag(view);
            return new SkinView(view, skinAttrs);
        }
        return null;
    }

    /**skin:left_menu_icon:src|skin:color_red:textColor**/
    private static List<SkinAttr> parseTag(String tagStr) {
        List<SkinAttr> skinAttrs = new ArrayList<SkinAttr>();
        if (TextUtils.isEmpty(tagStr)) return skinAttrs;

        String[] items = tagStr.split("[|]");
        for (String item : items) {
            if (!item.startsWith(SkinConfig.SKIN_PREFIX))
                continue;
            String[] resItems = item.split(":");
            if (resItems.length != 3)
                continue;

            String resName = resItems[1];
            String resType = resItems[2];

            SkinAttrType attrType = getSupportAttrType(resType);
            if (attrType == null) continue;
            SkinAttr attr = new SkinAttr(attrType, resName);
            skinAttrs.add(attr);
        }
        return skinAttrs;
    }

    /**设置view的tag，指定一个id的tag**/
    private static void changeViewTag(View view){
        Object tag = view.getTag(R.id.skin_tag_id);
        if (tag == null) {
            tag = view.getTag();
            view.setTag(R.id.skin_tag_id, tag);
            view.setTag(null);
        }
    }
}
