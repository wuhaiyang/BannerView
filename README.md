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
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-banner/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cn.bingoogolapple/bga-banner) bga-banner 后面的「latestVersion」指的是左边这个 maven-central 徽章后面的「数字」，请自行替换。

```groovy
dependencies {
    compile 'cn.bingoogolapple:bga-banner:latestVersion@aar'
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

### 3.在 Activity 或者 Fragment 中配置 BannerView 的数据源

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


