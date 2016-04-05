package com.lixue.aibei.changeskinlib;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.lixue.aibei.changeskinlib.utils.L;

/**
 * Created by Administrator on 2016/2/25.
 */
public class ResourceManager {
    private static final String DEFTYPE_DRAWABLE = "drawable";
    private static final String DEFTYPE_COLOR = "color";
    private String striff;//后缀
    private String mPackgeName;//包名
    private Resources res;
    public ResourceManager(Resources res,String mPackgeName,String striff){
        this.mPackgeName = mPackgeName;
        this.res = res;
        if (striff == null) striff = "";
        this.striff = striff;
    }

    /**通过名字得到drawable**/
    public Drawable getDrawableByName(String name){
        try{
            name = appendstuffix(name);
            L.e("getDrawableByName(name),name:" + name);
            return res.getDrawable(res.getIdentifier(name,DEFTYPE_DRAWABLE,mPackgeName));
        }catch (Resources.NotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
    /**通过名字得到color**/
    public int getColor(String  name){
        try{
            name = appendstuffix(name);
            L.e("name = " + name);
            return res.getColor(res.getIdentifier(name, DEFTYPE_COLOR, mPackgeName));
        }catch (Resources.NotFoundException e){
            e.printStackTrace();
            return -1;
        }
    }

    /**通过名字得到colorstatelist**/
    public ColorStateList getColorStateList(String name) {
        try {
            name = appendstuffix(name);
            L.e("name = " + name);
            return res.getColorStateList(res.getIdentifier(name, DEFTYPE_COLOR, mPackgeName));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**追加后缀**/
    private String appendstuffix(String str){
        if (!TextUtils.isEmpty(striff)){
            return str + "_" + striff;
        }else{
            return str;
        }
    }
}
