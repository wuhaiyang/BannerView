:running:BannerLib:running:
============

## 目录
* [功能介绍](#功能介绍)
* [效果图与示例 apk](#效果图与示例-apk)
* [使用](#使用)
* [自定义属性说明](#自定义属性说明)
* [License](#license)

## 功能介绍
- [x] 引导界面导航&广告等效果
- [x] 支持自定义指示器位置
- [x] 支持自定义图片指示器背景
- [x] 支持监听 item 点击事件

## 使用
### 1.添加 Gradle 依赖
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.anaction.banner/library/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.anaction.banner/library)  后面的「latestVersion」指的是左边这个 maven-central 徽章后面的「数字」，请自行替换。

```groovy
dependencies {
    compile 'cn.anaction.banner:library:lastVersion@aar'
}
```
### 2.在布局文件中添加 BannerView

```xml
<cn.anaction.banner.library.BannerView
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="140dp"
        app:bvIsShowPointer="true"
        app:bvPageChangeDuration="800"
        app:bvPointerPlayInterval="2000"
        app:bvPointerAutoPlayAble="true"
        app:bvPointerContainerHeight="20dp"
        app:bvPointerGravity="right|bottom"
        app:bvPointerSpacing="8dp"/>
```
### 3.在 Activity 或者 Fragment 
```java

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

```

## 自定义属性说明

```xml
 <declare-styleable name="BannerView">
 			<!--viewPager 切换时长-->
        <attr name="bvPageChangeDuration" format="integer"/>
        <!--是否显示指示器-->
        <attr name="bvIsShowPointer" format="boolean"/>
         <!--指示器容器高度-->
        <attr name="bvPointerContainerHeight" format="dimension"/>
        <!--指示器左右margin-->
        <attr name="bvPointerParentLeftRight" format="dimension"/>
        <!--指示器之间的间距-->
        <attr name="bvPointerSpacing" format="dimension"/>

        <!-- 指示点容器背景 -->
        <attr name="bvPointerContainerBackground" format="reference|color"/>
        <!-- 指示点背景 -->
        <attr name="bvPointerDrawable" format="reference"/>
        <!-- 指示器的位置 -->
        <attr name="bvPointerGravity">
            <flag name="top" value="0x30"/>
            <flag name="bottom" value="0x50"/>
            <flag name="left" value="0x03"/>
            <flag name="right" value="0x05"/>
            <flag name="center_horizontal" value="0x01"/>
        </attr>
        <!-- 是否开启自动轮播 -->
        <attr name="bvPointerAutoPlayAble" format="boolean"/>
        <!-- 自动轮播的时间间隔 -->
        <attr name="bvPointerPlayInterval" format="integer"/>
    </declare-styleable>
```
## License

    Copyright 2017 andaction

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


