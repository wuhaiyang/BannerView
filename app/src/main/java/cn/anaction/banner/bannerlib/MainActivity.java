/*
 * Copyright (c) 2017. danlu.com Co.Ltd. All rights reserved.
 */

package cn.anaction.banner.bannerlib;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.anaction.banner.library.BannerView;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerView = (BannerView) findViewById(R.id.banner);

        final List<String> picUrls = new ArrayList<>();
        picUrls.add("http://image.danlu.com/2016/10/28/00/15/1477584955666.jpg");
        picUrls.add("http://image.danlu.com/2016/10/28/00/16/1477584986344.jpg");
        picUrls.add("https://imgsa.baidu.com/baike/c0%3Dbaike150%2C5%2C5%2C150%2C50/sign=c412befaea50352aa56c2d5a322a9097/2e2eb9389b504fc22b830148e6dde71191ef6d9b.jpg");
        picUrls.add("https://imgsa.baidu.com/baike/c0%3Dbaike180%2C5%2C5%2C180%2C60/sign=27fb079719d8bc3ed2050e98e3e2cd7b/d53f8794a4c27d1e996189691bd5ad6edcc438d3.jpg");
        bannerView.setAdapter(new BannerView.Adapter() {

            @Override
            public View initView(Context context) {
                ImageView imageView = new ImageView(context);
                return imageView;
            }

            @Override
            public int getCount() {
                return picUrls.size();
            }

            @Override
            public void bindView(View view, final int pos) {
                ImageView imageView = (ImageView) view;
                String url = picUrls.get(pos);
                Glide
                        .with(MainActivity.this)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .crossFade()
                        .into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.w("@@@@ L63", "MainActivity:onClick() -> " + "pos = " + pos);
                    }
                });
            }

            @Override
            public boolean isSameType() {
                return true;
            }
        });
    }
}
