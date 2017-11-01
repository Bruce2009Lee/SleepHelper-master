package com.example.developerhaoz.sleephelper.common.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.developerhaoz.sleephelper.R;

/**
 * Created by Administrator on 2017/10/24.
 */

public class ActivityTestDemo extends Activity {

    // 定义一个访问图片的数组
    int[] images = new int[] {
            R.drawable.meizhi_1,
            R.drawable.meizhi_2,
            R.drawable.meizhi_3};
    int nowImg = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_1);

        // 获取LinearLayout布局容器
        LinearLayout main = (LinearLayout) findViewById(R.id.root);

        // 程序创建ImageView组件
        final ImageView image = new ImageView(this);
        // 将ImageView组件添加到LinearLayout布局容器中
        main.addView(image);
        // 初始化时显示第一张图片
        image.setImageResource(images[0]);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 改变ImageView里显示的图片
                image.setImageResource(images[++nowImg % images.length]);
            }
        });
    }


}
