package com.example.developerhaoz.sleephelper.common.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.Toast;

import com.example.developerhaoz.sleephelper.R;

/**
 * Created by Administrator on 2017/10/23.
 */

public class ActivityTabLayoutMain extends Activity {

    TabLayout mTabLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_main);


        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_1);

//        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 1"));
//
//        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 2"));
//
//        mTabLayout.addTab(mTabLayout.newTab().setText("Tab 3"));

        mTabLayout.addOnTabSelectedListener(listener);

    }

    private TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //选择的tab
            Log.e("TT","onTabSelected:" + tab.getText().toString());
            Toast.makeText(getApplication().getBaseContext(), tab.getText().toString() + "is selected",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            //离开的那个tab
            Log.e("TT","onTabUnselected" + tab.getText().toString());
//            Toast.makeText(getApplication().getBaseContext(), tab.getText().toString() + "is unselected",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            //再次选择tab
            Log.e("TT","onTabReselected" + tab.getText().toString());
//            Toast.makeText(getApplication().getBaseContext(), tab.getText().toString() + "is reselected",Toast.LENGTH_SHORT).show();
        }
    };

}
