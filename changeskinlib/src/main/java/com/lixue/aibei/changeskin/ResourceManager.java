package com.lixue.aibei.changeskin;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.lixue.aibei.changeskin.utils.L;

/**
 * Created by Administrator on 2016/2/25.
 */
public class ResourceManager {
    private static final String DEFTYPE_DRAWABLE = "drawable";
    private static final String DEFTYPE_COLOR = "color";
    private String striff;//��׺
    private String mPackgeName;//�������
    private Resources res;
    public ResourceManager(Resources res,String mPackgeName,String striff){
        this.mPackgeName = mPackgeName;
        this.res = res;
        if (striff == null) striff = "";
        this.striff = striff;
    }

    /**�������ֵõ�drawable**/
    public Drawable getDrawableByName(String name){
        name = appendstuffix(name);
        L.e("getDrawableByName(name),name:" + name);
        try{
            return res.getDrawable(res.getIdentifier(name,DEFTYPE_DRAWABLE,mPackgeName));
        }catch (Resources.NotFoundException e){
            try{
                return res.getDrawable(res.getIdentifier(name,DEFTYPE_COLOR,mPackgeName));
            }catch (Resources.NotFoundException ex){
                ex.printStackTrace();
                return null;
            }

        }
    }
    /**�������ֵõ�color**/
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

    /**�������ֵõ�colorstatelist**/
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

    /**������׷���Ϻ�׺**/
    private String appendstuffix(String str){
        if (!TextUtils.isEmpty(str)){
            return str + "_" +striff;
        }else{
            return str;
        }

    }
}
