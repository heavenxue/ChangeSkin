package com.lixue.aibei.changeskin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lixue.aibei.changeskinlib.SkinManager;

public class TestTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_tag);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SkinManager.getInstance().register(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void addNewView(View view) {
        //建议通过xml inflater
        TextView tv = new TextView(this);
        tv.setTag("skin:item_text_color:textColor");
        tv.setTextColor(getResources().getColorStateList(R.color.item_text_color));
        tv.setText("dymaic add!");

        ((ViewGroup) findViewById(R.id.id_container)).addView(tv);
        SkinManager.getInstance().injectSkin(tv);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregister(this);
    }

}
