package com.lixue.aibei.changeskinlib;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.lixue.aibei.changeskinlib.attr.SkinView;
import com.lixue.aibei.changeskinlib.callback.ISkinChangedListener;
import com.lixue.aibei.changeskinlib.callback.ISkinChangingCallback;
import com.lixue.aibei.changeskinlib.utils.L;
import com.lixue.aibei.changeskinlib.utils.PrefUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/25.
 */
public class SkinManager {
    private Context mContext;
    private Resources mResources;
    private ResourceManager mResourceManager;
    private PrefUtils mPrefUtils;

    private boolean usePlugin;
    /**
     * 换肤资源后缀
     */
    private String mSuffix = "";
    private String mCurPluginPath;
    private String mCurPluginPkg;


    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps = new HashMap<ISkinChangedListener, List<SkinView>>();
    private List<ISkinChangedListener> mSkinChangedListeners = new ArrayList<ISkinChangedListener>();

    private static final SkinManager instance = new SkinManager();
    private SkinManager(){}
    public synchronized static SkinManager getInstance(){
        return instance;
    }


    public void init(Context context){
        mContext = context.getApplicationContext();
        mPrefUtils = new PrefUtils(mContext);
        String pluginPath = mPrefUtils.getpluginPath();
        String pkgName = mPrefUtils.getPluginPkgName();
        mSuffix = mPrefUtils.getSuffix();
        try{
            loadPlugin(pluginPath,pkgName,mSuffix);
            mCurPluginPath = pluginPath;
            mCurPluginPkg = pkgName;
        }catch (Exception e){
            mPrefUtils.clear();
            e.printStackTrace();
        }
    }

    /**加载插件**/
    public void loadPlugin(String pluginPath,String pkgName,String suffix){
        try {
            //将一个apk中的资源加载到Resources中
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,pluginPath);

            Resources superRes = mContext.getResources();
            mResources = new Resources(assetManager,superRes.getDisplayMetrics(),superRes.getConfiguration());
            L.e("插件中的Resources:"+mResources.toString());
            mResourceManager = new ResourceManager(mResources,pkgName,suffix);
            L.e("ResourceManager:"+mResourceManager.toString());
            usePlugin = true;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPluginParams(String skinPath, String skinPkgName) {
        if (TextUtils.isEmpty(skinPath) || TextUtils.isEmpty(skinPkgName)) {
            return false;
        }
        return true;
    }

    private void checkPluginParamsThrow(String skinPath,String pkgName){
        if(!checkPluginParams(skinPath,pkgName)){
            throw new IllegalArgumentException("skinPluginPath or skinPkgName can not be empty !");
        }
    }
    public void removeAnySkin(){
        clearPluginInfo();
        notifyChangedListeners();
    }

    public boolean needChangeSkin(){
        return usePlugin || !TextUtils.isEmpty(mSuffix);
    }

    public ResourceManager getResourceManager() {
        if (!usePlugin) {
            mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName(), mSuffix);
        }
        return mResourceManager;
    }

    /**
     * 换肤
     *
     * @param stuffix
     */
    public void changeSkin(String stuffix){
        clearPluginInfo();
        mSuffix = stuffix;
        mPrefUtils.putPluginSuffix(stuffix);
        notifyChangedListeners();
    }

    private void clearPluginInfo(){
        mCurPluginPath = null;
        mCurPluginPkg = null;
        mSuffix = null;
        usePlugin = false;
        if (mPrefUtils != null) mPrefUtils.clear();

    }

    private void notifyChangedListeners(){
        for (ISkinChangedListener listener : mSkinChangedListeners) {
            listener.onSkinChanged();
        }
    }

    private void updatePluginInfo(String skinPluginPath, String pkgName, String suffix) {
        mPrefUtils.putPluginPath(skinPluginPath);
        mPrefUtils.putPluginPkg(pkgName);
        mPrefUtils.putPluginSuffix(suffix);
        mCurPluginPkg = pkgName;
        mCurPluginPath = skinPluginPath;
        mSuffix = suffix;
    }


    public void changeSkin(final String skinPluginPath, final String pkgName, ISkinChangingCallback callback) {
        changeSkin(skinPluginPath, pkgName, "", callback);
    }

    /**
     * 根据suffix选择插件内某套皮肤，默认为""
     *
     * @param skinPluginPath
     * @param pkgName
     * @param suffix
     * @param callback
     */
    public void changeSkin(final String skinPluginPath, final String pkgName, final String suffix, ISkinChangingCallback callback) {
        if (callback == null)
            callback = ISkinChangingCallback.DEFAULT_SKIN_CHANGING_CALLBACK;
        final ISkinChangingCallback skinChangingCallback = callback;

        skinChangingCallback.onStart();
        checkPluginParamsThrow(skinPluginPath, pkgName);

        if (skinPluginPath.equals(mCurPluginPath) && pkgName.equals(mCurPluginPkg)) {
            return;
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    loadPlugin(skinPluginPath, pkgName, suffix);
                } catch (Exception e) {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                try {
                    updatePluginInfo(skinPluginPath, pkgName, suffix);
                    notifyChangedListeners();
                    skinChangingCallback.onComplete();
                } catch (Exception e) {
                    e.printStackTrace();
                    skinChangingCallback.onError(e);
                }

            }
        }.execute();
    }


    public void addSkinView(ISkinChangedListener listener, List<SkinView> skinViews) {
        mSkinViewMaps.put(listener, skinViews);
    }

    public List<SkinView> getSkinViews(ISkinChangedListener listener) {
        return mSkinViewMaps.get(listener);
    }


    public void apply(ISkinChangedListener listener) {
        List<SkinView> skinViews = getSkinViews(listener);

        if (skinViews == null) return;
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }

    public void addChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.add(listener);
    }


    public void removeChangedListener(ISkinChangedListener listener) {
        mSkinChangedListeners.remove(listener);
        mSkinViewMaps.remove(listener);
    }

}
