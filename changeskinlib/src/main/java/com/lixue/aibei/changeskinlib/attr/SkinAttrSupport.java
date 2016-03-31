package com.lixue.aibei.changeskinlib.attr;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.lixue.aibei.changeskinlib.R;
import com.lixue.aibei.changeskinlib.constant.SkinConfig;
import com.lixue.aibei.changeskinlib.utils.L;

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
    }

    /**获取换肤视图**/
    public static SkinView getSkinView(View view){
        Object tag = view.getTag(R.id.skin_tag_id);
        if (tag == null){
            tag = view.getTag();
        }
        return null;
    }

    public static List<SkinAttr> getSkinAttrs(AttributeSet attrs,Context context){
        List<SkinAttr> skinAttrs = new ArrayList<>();
        SkinAttr skinAttr = null;
        for(int i = 0 ;i < attrs.getAttributeCount();i++){
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
            SkinAttrType skinAttrType = getSupportAttrType(attrName);
            if (skinAttrType == null) continue;
            if (attrValue.startsWith("@")){
                int id = Integer.parseInt(attrValue.substring(1));
                String entryName = context.getResources().getResourceEntryName(id);
                L.e("entryName = " + entryName);
                if (entryName.startsWith(SkinConfig.ATTR_PREFIX)){
                    skinAttr = new SkinAttr(skinAttrType,entryName);
                    skinAttrs.add(skinAttr);
                }
            }
        }
        return skinAttrs;
    }
}
