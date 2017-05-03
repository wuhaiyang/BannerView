/*
 * Copyright (c) 2017. danlu.com Co.Ltd. All rights reserved.
 */

package cn.anaction.banner.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.Stack;


/**
 * author: wuhaiyang
 * version: 1.0.0
 * since: 2017-05-02 上午10:18
 */
public class BannerView extends RelativeLayout implements ViewPager.OnPageChangeListener {
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    private static final int LWC = LinearLayout.LayoutParams.WRAP_CONTENT;

    private FitViewPager fitViewPager;

    private AutoPlayTask autoPlayTask;

    private boolean isShowPointer = true;

    private int pointerContainerHeight;
    private int pointerParentMarginLeftRight;
    private int pointerSpacing;

    private Drawable pointContainerBackgroundDrawable;
    private int pointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

    private int pointDrawableResId = R.drawable.banner_selector_point_solid;

    private int pageChangeDuration = 800;
    private int autoPlayInterval = 3000;
    private boolean autoPlayAble = true;


    private LinearLayout llRealPointerContainer;

    private ViewPager.OnPageChangeListener pageChangeListener;

    private Adapter adapter;

    public void setPageChangeListener(ViewPager.OnPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initApi(context);
        initCustomAttrs(context, attrs);
        initView(context);
    }

    public void setAdapter(@NonNull Adapter adapter) {
        this.adapter = adapter;
        initIndicator();
        initViewPager();
    }

    private void initIndicator() {
        if (null != llRealPointerContainer && isShowPointer) {
            llRealPointerContainer.removeAllViews();
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LWC, LWC);
            lp.setMargins(0, 0, pointerSpacing, 0);
            ImageView imageView;
            for (int i = 0; i < adapter.getCount(); i++) {
                imageView = new ImageView(getContext());
                if (i == adapter.getCount() - 1) {
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(LWC, LWC));
                } else {
                    imageView.setLayoutParams(lp);
                }
                imageView.setImageResource(pointDrawableResId);
                llRealPointerContainer.addView(imageView);
            }
        }
    }

    private void initView(Context context) {
        if (isShowPointer) {
            // 指示器最外层父容器 RelativeLayout
            RelativeLayout rlPointContainer = new RelativeLayout(context);
            if (Build.VERSION.SDK_INT >= 16) {
                rlPointContainer.setBackground(pointContainerBackgroundDrawable);
            } else {
                rlPointContainer.setBackgroundDrawable(pointContainerBackgroundDrawable);
            }
            rlPointContainer.setPadding(pointerParentMarginLeftRight, 0, pointerParentMarginLeftRight, 0);
            LayoutParams pointContainerLp = new LayoutParams(RMP, pointerContainerHeight);
            if ((pointGravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.TOP) {
                pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                pointContainerLp.addRule(ALIGN_PARENT_BOTTOM);
            }
            addView(rlPointContainer, pointContainerLp);

            // 指示器一级父容器 LinearLayout
            LayoutParams indicatorLp = new LayoutParams(RWC, RWC);
            indicatorLp.addRule(RelativeLayout.CENTER_VERTICAL);
//            indicatorLp.addRule(CENTER_VERTICAL);
            llRealPointerContainer = new LinearLayout(context);
            llRealPointerContainer.setOrientation(LinearLayout.HORIZONTAL);
            llRealPointerContainer.setGravity(Gravity.CENTER_VERTICAL);

            // 处理指示器原点显示在哪个位置上
            int horizontalGravity = pointGravity & Gravity.HORIZONTAL_GRAVITY_MASK;
            if (horizontalGravity == Gravity.LEFT) {
                // 居左显示
                indicatorLp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            } else if (horizontalGravity == Gravity.CENTER_HORIZONTAL) {
                // 居中显示
                indicatorLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            } else if (horizontalGravity == Gravity.RIGHT) {
                // 居右显示
                indicatorLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            }

            rlPointContainer.addView(llRealPointerContainer, indicatorLp);
        }
    }

    /**
     * 设置页码切换过程的时间长度
     *
     * @param duration 页码切换过程的时间长度
     */
    public void setPageChangeDuration(int duration) {
        if (duration >= 0 && duration <= 3000) {
            pageChangeDuration = duration;
            if (fitViewPager != null) {
                fitViewPager.setPageChangeDuration(duration);
            }
        }
    }


    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerView);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.BannerView_bvPageChangeDuration) {
            pageChangeDuration = typedArray.getInteger(attr, pageChangeDuration);
        } else if (attr == R.styleable.BannerView_bvIsShowPointer) {
            isShowPointer = typedArray.getBoolean(attr, isShowPointer);
        } else if (attr == R.styleable.BannerView_bvPointerContainerHeight) {
            pointerContainerHeight = typedArray.getDimensionPixelSize(attr, pointerContainerHeight);
        } else if (attr == R.styleable.BannerView_bvPointerParentLeftRight) {
            pointerParentMarginLeftRight = typedArray.getDimensionPixelSize(attr, pointerParentMarginLeftRight);
        } else if (attr == R.styleable.BannerView_bvPointerSpacing) {
            pointerSpacing = typedArray.getDimensionPixelSize(attr, pointerSpacing);
        } else if (attr == R.styleable.BannerView_bvPointerGravity) {
            pointGravity = typedArray.getInt(attr, pointGravity);
        } else if (attr == R.styleable.BannerView_bvPointerAutoPlayAble) {
            autoPlayAble = typedArray.getBoolean(attr, autoPlayAble);
        } else if (attr == R.styleable.BannerView_bvPointerPlayInterval) {
            autoPlayInterval = typedArray.getInt(attr, autoPlayInterval);
        } else if (attr == R.styleable.BannerView_bvPointerDrawable) {
            pointDrawableResId = typedArray.getResourceId(attr, pointDrawableResId);
        } else if (attr == R.styleable.BannerView_bvPointerContainerBackground) {
            pointContainerBackgroundDrawable = typedArray.getDrawable(attr);
        }
    }

    private void initApi(Context context) {
        autoPlayTask = new AutoPlayTask(this);

        pointerContainerHeight = Utils.dp2px(context, 30);
        pointerParentMarginLeftRight = Utils.dp2px(context, 10);
        pointerSpacing = Utils.dp2px(context, 5);

        pointContainerBackgroundDrawable = new ColorDrawable(Color.parseColor("#44aaaaaa"));
    }

    private void initViewPager() {
        if (null != fitViewPager && this.equals(fitViewPager.getParent())) {
            this.removeView(fitViewPager);
            fitViewPager = null;
        }
        fitViewPager = new FitViewPager(getContext());
        fitViewPager.setOffscreenPageLimit(1);
        fitViewPager.setAdapter(new PageAdapter(adapter.isSameType()));
        fitViewPager.addOnPageChangeListener(this);
        fitViewPager.setOverScrollMode(OVER_SCROLL_NEVER);
//        fitViewPager.setPageTransformer(true, BGAPageTransformer.getPageTransformer(mTransitionEffect));
        setPageChangeDuration(pageChangeDuration);
        LayoutParams layoutParams = new LayoutParams(RMP, RMP);
        addView(fitViewPager, 0, layoutParams);

        if (autoPlayAble) {
            int zeroItem = Integer.MAX_VALUE / 2 - (Integer.MAX_VALUE / 2) % adapter.getCount();
            fitViewPager.setCurrentItem(zeroItem);

            startAutoPlay();
        } else {
            switchToPoint(0);
        }
    }

    private void switchToPoint(int newCurrentPoint) {
        if (llRealPointerContainer != null) {
            if (adapter.getCount() > 0 && newCurrentPoint < adapter.getCount()) {
                for (int i = 0; i < llRealPointerContainer.getChildCount(); i++) {
                    llRealPointerContainer.getChildAt(i).setEnabled(i == newCurrentPoint);
                    // 处理指示器选中和未选中状态图片尺寸不相等
                    llRealPointerContainer.getChildAt(i).requestLayout();
                }
            }
        }
    }

    private class PageAdapter extends PagerAdapter {

        private Stack<WeakReference<View>> cacheViews;

        private boolean isSameType = true;

        public PageAdapter(boolean isSameType) {
            this.isSameType = isSameType;
            if (isSameType) {
                cacheViews = new Stack<>();
            }
        }

        @Override
        public int getCount() {
            return autoPlayAble ? Integer.MAX_VALUE : null != adapter ? adapter.getCount() : 0;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = null;
            final int finalPos = position % adapter.getCount();
            if (isSameType) {
                WeakReference<View> refView = null;
                while (cacheViews.size() > 0) {
                    refView = cacheViews.pop();
                    view = refView.get();
                }
                if (null == view) {
                    view = adapter.initView(container.getContext());
                } else {
                    refView.clear();
                }
            } else {
                view = adapter.initView(container.getContext());
            }
            adapter.bindView(view, finalPos);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            if (isSameType) {
                cacheViews.push(new WeakReference<>((View) object));
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (null != pageChangeListener) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (null != pageChangeListener) {
            pageChangeListener.onPageSelected(position);
        }
        position = position % adapter.getCount();
        switchToPoint(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (null != pageChangeListener) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (autoPlayAble) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private static class AutoPlayTask implements Runnable {
        private final WeakReference<BannerView> bannerView;

        public AutoPlayTask(BannerView bannerView) {
            this.bannerView = new WeakReference<>(bannerView);
        }

        @Override
        public void run() {
            BannerView bannerView = this.bannerView.get();
            if (null != bannerView) {
                bannerView.switchToNextPage();
                bannerView.startAutoPlay();
            }
        }
    }

    public void setAutoPlayAble(boolean autoPlayAble) {
        this.autoPlayAble = autoPlayAble;

        stopAutoPlay();

        if (fitViewPager != null && fitViewPager.getAdapter() != null) {
            fitViewPager.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * 设置自动轮播的时间间隔
     *
     * @param autoPlayInterval
     */
    public void setAutoPlayInterval(int autoPlayInterval) {
        this.autoPlayInterval = autoPlayInterval;
    }


    public void startAutoPlay() {
        stopAutoPlay();
        if (autoPlayAble) {
            postDelayed(autoPlayTask, autoPlayInterval);
        }
    }

    public void stopAutoPlay() {
        if (autoPlayTask != null) {
            removeCallbacks(autoPlayTask);
        }
    }

    private void switchToNextPage() {
        if (fitViewPager != null) {
            fitViewPager.setCurrentItem(fitViewPager.getCurrentItem() + 1);
        }
    }


    public interface Adapter {

        View initView(Context context);

        int getCount();

        void bindView(View view, int pos);

        boolean isSameType();
    }
}
