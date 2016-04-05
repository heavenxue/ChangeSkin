package com.lixue.aibei.changeskinlib;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.lixue.aibei.changeskinlib.attr.SkinAttrSupport;
import com.lixue.aibei.changeskinlib.attr.SkinView;
import com.lixue.aibei.changeskinlib.callback.ISkinChangingCallback;
import com.lixue.aibei.changeskinlib.utils.L;
import com.lixue.aibei.changeskinlib.utils.PrefUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

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


    private List<Activity> mActivities = new ArrayList<Activity>();
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

    /**创建Resource对象：**/
    public Resources getBundleResource(Context context, String apkPath) {
        AssetManager assetManager = createAssetManager(apkPath);
        return new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
    }

    /**创建AssetManager**/
    private AssetManager createAssetManager(String apkPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            AssetManager.class.getDeclaredMethod("addAssetPath", String.class).invoke(assetManager, apkPath);
            return assetManager;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return null;
    }

    public ResourceManager getResourceManager() {
        if (!usePlugin) {
            mResourceManager = new ResourceManager(mContext.getResources(), mContext.getPackageName(), mSuffix);
        }
        return mResourceManager;
    }

    public Resources getmResources(){
     if (!usePlugin){mResources = mContext.getResources();}
        return mResources;
    }

    public String getPkgName(){
        if (!usePlugin){
            mCurPluginPkg = mContext.getPackageName();
        }
        return mCurPluginPkg;
    }

    private boolean checkPluginParams(String skinPath, String skinPkgName) {
        if (TextUtils.isEmpty(skinPath) || TextUtils.isEmpty(skinPkgName)) {
            return false;
        }
        File file = new File(skinPath);
        if (!file.exists())
            return false;

        PackageInfo info = getPackageInfo(skinPath);
        if (!info.packageName.equals(skinPkgName))
            return false;
        return true;
    }

    private PackageInfo getPackageInfo(String skinPluginPath) {
        PackageManager pm = mContext.getPackageManager();
        return pm.getPackageArchiveInfo(skinPluginPath, PackageManager.GET_ACTIVITIES);
    }

    private void checkPluginParamsThrow(String skinPath,String pkgName){
        if(!checkPluginParams(skinPath,pkgName)){
            throw new IllegalArgumentException("skinPluginPath or skinPkgName can not be empty !");
        }
    }
    public void removeAnySkin(){
        L.e("removeAnySkin");
        clearPluginInfo();
        notifyChangedListeners();
    }

    public boolean needChangeSkin(){
        return usePlugin || !TextUtils.isEmpty(mSuffix);
    }

    /**
     *  应用内换肤，传入资源区别的后缀
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
        try{
            checkPluginParamsThrow(skinPluginPath, pkgName);
        }catch (IllegalFormatException e){
            skinChangingCallback.onError(new RuntimeException("checkPlugin occur error"));
            return;
        }

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    loadPlugin(skinPluginPath, pkgName, suffix);
                    return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            @Override
            protected void onPostExecute(Integer res) {
                if (res == 0){
                    skinChangingCallback.onError(new RuntimeException("loadPlugin occur error"));
                    return;
                }
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


    public void apply(Activity activity) {
        List<SkinView> skinViews = SkinAttrSupport.getSkinViews(activity);

        if (skinViews == null) return;
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }

    public void register(final Activity activity) {
        mActivities.add(activity);

//        activity.findViewById(android.R.id.content).post(new Runnable() {
//            @Override
//            public void run() {
//                apply(activity);
//            }
//        });
    }

    public void unregister(Activity activity) {
        mActivities.remove(activity);
    }

    public void notifyChangedListeners() {

        for (Activity activity : mActivities) {
            apply(activity);
        }
    }

    /**
     * apply for dynamic construct view
     *
     * @param view
     */
    public void injectSkin(View view) {
        List<SkinView> skinViews = new ArrayList<SkinView>();
        SkinAttrSupport.addSkinViews(view, skinViews);
        for (SkinView skinView : skinViews) {
            skinView.apply();
        }
    }
}
