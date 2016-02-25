package com.lixue.aibei.changeskin;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Administrator on 2016/2/25.
 */
public class SkinManager {
    private Context mContext;
    private Resources mResources;
    private ResourceManager mResourceManager;
//    private PrefUtils mPrefUtils;

    private boolean usePlugin;
    /**
     * »»·ô×ÊÔ´ºó×º
     */
    private String mSuffix = "";
    private String mCurPluginPath;
    private String mCurPluginPkg;


//    private Map<ISkinChangedListener, List<SkinView>> mSkinViewMaps = new HashMap<ISkinChangedListener, List<SkinView>>();
//    private List<ISkinChangedListener> mSkinChangedListeners = new ArrayList<ISkinChangedListener>();

    private static final SkinManager instance = new SkinManager();
    private SkinManager(){}
    public synchronized static SkinManager getInstance(){
        return instance;
    }
}
