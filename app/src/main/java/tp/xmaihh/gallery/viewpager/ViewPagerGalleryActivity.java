package tp.xmaihh.gallery.viewpager;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import tp.xmaihh.gallery.R;

public class ViewPagerGalleryActivity extends AppCompatActivity {
    private ViewPagerAdapter mPagerAdapter;
    private GalleryViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager_gallery_view);
        hideBottomUIMenu();
        //*
        init();
    }

    private void init() {
        mViewPager = findViewById(R.id.viewpager);
        /**调节ViewPager的滑动速度**/
//        mViewPager.setSpeedScroller(300);
        /**给ViewPager设置缩放动画，这里通过PageTransformer来实现**/
        mViewPager.setPageTransformer(true, new ScalePageTransformer());
        /**
         * 需要将整个页面的事件分发给ViewPager，不然的话只有ViewPager中间的view能滑动，其他的都不能滑动，
         * 这是肯定的，因为ViewPager总体布局就是中间那一块大小，其他的子布局都跑到ViewPager外面来了
         */
        findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });

        mPagerAdapter = new ViewPagerAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);
        /**这里需要将setOffscreenPageLimit的值设置成数据源的总个数，如果不加这句话，会导致左右切换异常；**/
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());

        // 自定义view的宽度,控制一屏显示的个数
        ViewGroup.LayoutParams layoutParams = mViewPager.getLayoutParams();
        int width = this.getResources().getDisplayMetrics().widthPixels;
        layoutParams.width = width / 3;
        mViewPager.setLayoutParams(layoutParams);
        mViewPager.setCurrentItem(1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position < 1) {
                    //如果item位置小于1，也就是滑动到第0个item的位置时，则直接跳转到倒数第二个view处，并关闭跳转动画
                    mViewPager.setCurrentItem(mPagerAdapter.getCount() - 2, false);
                } else if (position > mPagerAdapter.getCount() - 2) {

                    //同理如果item位置大于倒数第二个view的位置，也就是滑动到最后一个item的位置时，则直接跳转到第二个view处，并关闭跳转动画
                    mViewPager.setCurrentItem(1, false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
