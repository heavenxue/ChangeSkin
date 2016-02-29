package com.lixue.aibei.changeskinlib.attr;

import android.content.Context;
import android.util.AttributeSet;

import com.lixue.aibei.changeskinlib.constant.SkinConfig;
import com.lixue.aibei.changeskinlib.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/26.
 */
public class SkinAttrSupport {
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

    private static SkinAttrType getSupportAttrType(String name){
        for(SkinAttrType st : SkinAttrType.values()){
            if (st.getAttrType().equals(name)){
                return st;
            }
        }
        return null;
    }
}
