package com.lixue.aibei.changeskin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lixue.aibei.changeskinlib.SkinManager;
import com.lixue.aibei.changeskinlib.callback.ISkinChangingCallback;
import com.nineoldandroids.view.ViewHelper;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
//    private String mSkinPkgPath = Environment.getExternalStorageDirectory() + File.separator + "night_plugin.apk";
    private String mSkinPkgPath = "/storage/emulated/0/night_plugin.apk";
    private String[] mDatas = new String[]{"Activity", "Service", "Activity", "Service", "Activity", "Service", "Activity", "Service"};
    private ArrayAdapter mAdapter ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SkinManager.getInstance().register(this);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }


    private void initEvents() {
        mListView = (ListView) findViewById(R.id.id_listview);
        mListView.setAdapter(mAdapter = new ArrayAdapter<String>(this, -1, mDatas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item, parent, false);
                }
                SkinManager.getInstance().injectSkin(convertView);
                TextView tv = (TextView) convertView.findViewById(R.id.id_tv_title);
                tv.setText(getItem(position));
                return convertView;
            }
        });


        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;

                if (drawerView.getTag().equals("LEFT")) {

                    float leftScale = 1 - 0.3f * scale;

                    ViewHelper.setScaleX(mMenu, leftScale);
                    ViewHelper.setScaleY(mMenu, leftScale);
                    ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                    ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                    ViewHelper.setPivotX(mContent, 0);
                    ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                    mContent.invalidate();
                    ViewHelper.setScaleX(mContent, rightScale);
                    ViewHelper.setScaleY(mContent, rightScale);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        });
    }

    private void initView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.id_left_menu_container);
        if (fragment == null) {
            fm.beginTransaction().add(R.id.id_left_menu_container, new MenuLeftFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.id_action_plugin_skinchange:
                SkinManager.getInstance().changeSkin(mSkinPkgPath, "com.zhy.plugin", new ISkinChangingCallback() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d("MainActivity","换肤失败");
                    }

                    @Override
                    public void onComplete() {
                        Log.d("MainActivity", "换肤成功");
                    }
                });
                break;
            case R.id.id_action_notify_lv:

                for (int i = 0, n = mDatas.length; i < n; i++)
                {
                    mDatas[i] = mDatas[i] + " changed";
                }
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.id_action_dynamic:
                Intent intent = new Intent(this,TestTagActivity.class);
                startActivity(intent);
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }
}
