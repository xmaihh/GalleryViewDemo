# GalleryViewDemo

- [RecyclerView实现GalleryView](https://github.com/xmaihh/GalleryViewDemo#recyclerview%E5%AE%9E%E7%8E%B0galleryview%E6%95%88%E6%9E%9C%E5%8F%AF%E4%BC%AA%E6%97%A0%E9%99%90%E6%97%A0%E9%99%90%E5%B7%A6%E6%BB%91%E5%8F%B3%E6%BB%91)
- [ViewPager实现GalleryView](https://github.com/xmaihh/GalleryViewDemo#viewpager%E5%AE%9E%E7%8E%B0galleryview%E6%95%88%E6%9E%9C%E5%8F%AF%E6%97%A0%E9%99%90%E5%B7%A6%E6%BB%91%E5%8F%B3%E6%BB%91)

1. 导包
```
implementation 'com.android.support:design:28.0.0'
implementation 'com.android.support:recyclerview-v7:28.0.0'
```
一个是ViewPager所在包,另一个是RecyclerView所在包

## RecyclerView实现GalleryView效果可(伪无限)无限左滑右滑
先上效果图

![RecyclerView实现的GalleryView效果](https://github.com/xmaihh/GalleryViewDemo/blob/master/app/arts/recyclerviewgallery.gif)
![ViewPager实现的GalleryView效果](https://github.com/xmaihh/GalleryViewDemo/blob/master/app/arts/viewpagergallery.gif)

要点：
  1. 在有限的数据里面, 实现无限个Item,也就是可循环
  2. 在第一次显示的时候, 就可以左滑
  3. 滑动Item被放大

用RecyclerView实现GalleryView效果已经有BCsl大神的[BCsl/GalleryLayoutManager](https://github.com/BCsl/GalleryLayoutManager)使用自定义 LayoutManager 实现 Android 中 Gallery 或者 ViewPager 控件的效果
美滋滋:-P
[传送门在这里](https://github.com/BCsl/GalleryLayoutManager)
支持垂直和水平两个方向，支持 RecycleView 的试图回收机制
### 在有限的数据里面,实现无限个Item
在RecyclerView.Adapter的方法中:
```
@Override
public int getItemCount() {
    return Integer.MAX_VALUE;
}
```
Integer.MAX_VALUE，即2147483647(2^32-1),距离真正的无限大还是有点差距的，不过效果也可以
### 第一次显示的时候实现左滑
只需要在一开始的时候,产生一定的偏移量就可以左滑了
```
@Override
public void setAdapter(Adapter adapter) {
    super.setAdapter(adapter);
    scrollToPosition(getAdapter().getItemRawCount() * 10000);//开始时的偏移量
}
```
RecyclerView有4个滑动方法:
```
scrollBy(x,y)
scrollToPosition(int position)
smoothScrollToPosition(int position)
scrollToPositionWithOffset(position,0)
```
smoothScrollToPosition 其实可以理解成一个模拟的滑动操作，会回调那个滑动监听的回调方法,有滑动效果。而 scrollToPosition 相当于直接把你想要的东西再重绘到界面上，不带滑动效果。
smoothScrollToPosition(position)和scrollToPosition(position)效果基本相似，也是把你想显示的项显示出来，只要那一项现在看得到了，那它就罢工了，
不同的是smoothScrollToPosition是平滑到你想显示的项，而scrollToPosition是直接定位显示。
scrollToPositionWithOffset(position,0)可以定位到指定项如果该项可以置顶就将其置顶显示，第二个参数可以决定 距离顶部的offset 偏移量
scrollBy(x, y)这个方法是自己去控制移动的距离，单位是像素,所以在使用scrollBy(x, y)需要自己去计算移动的高度或宽度

如果使用BCsl大BCsl/GalleryLayoutManager的自定义 LayoutManager 实现的 Gallery 可使用以下方法初始化偏移量:
```
  GalleryLayoutManager manager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
  manager.attach(recyclerView, 1000000);
```
### 滑动Item放大
实现GalleryLayoutManager.ItemTransformer的方法重写即可
```
    @Override
    public void transformItem(GalleryLayoutManager layoutManager, View item, float fraction) {
        //以圆心进行缩放
        item.setPivotX(item.getWidth() / 2.0f);
        item.setPivotY(item.getHeight() / 2.0f);
        float scale = 1 - 0.433f * Math.abs(fraction); // 0.433f是放大的View面积和缩小的View面积的比值
        item.setScaleX(scale);
        item.setScaleY(scale);
    }
 ```
## ViewPager 实现GalleryView效果
继承JakeWharton/salvage大封装的可用于复用的PagerAdapter[RecyclingPagerAdapter](https://github.com/JakeWharton/salvage/blob/master/salvage/src/main/java/com/jakewharton/salvage/RecyclingPagerAdapter.java)
支持View回收,美滋滋:-P

## ViewPager实现GalleryView效果可无限左滑右滑

要点：
  1. 在有限的数据里面, 实现无限个Item,也就是可循环
  2. 在第一次显示的时候, 就可以左滑
  3. 滑动的Item被放大
ViewPager这里用到JakeWharton大实现的支持view的回收机制PagerAdapter [RecyclingPagerAdapter](
https://github.com/JakeWharton/salvage/blob/master/salvage/src/main/java/com/jakewharton/salvage/RecyclingPagerAdapter.java)继承这个PagerAdapter就可以实现类似RecyclerView的回收机制了
### 在有限的数据,实现循环
![viewpagergallery.png](https://github.com/xmaihh/GalleryViewDemo/blob/master/app/arts/viewpagergallery.png)
1. 在 ViewPager 的首尾多添加一个 View，监听 ViewPager 滚动事件，当滑到边界时，设置当前 position 为中间的某个 item，不过这种方式容易出现页面闪动导致滑动不连贯，这是因为 ViewPager#setCurrentItem(item)是需要时间来完成测量及绘制的

```
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
```
2. 在ViewPagerAdapter的方法中:
实现起来较为简单。需要注意的是，我们需要设置 ViewPager 的初始 position
   ```
   @Override
   public int getItemCount() {
       return Integer.MAX_VALUE;
   }
   ```
### 在第一次显示的时候, 就可以左滑
这个简单只需要在一开始的时候,产生一定的偏移量就可以左滑了
```
/**这里需要将setOffscreenPageLimit的值设置成数据源的总个数，设置ViewPager缓存页面数量,最小可设置成屏幕可见的个数**/
mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount());
/**设置ViewPager位置**/
mViewPager.setCurrentItem(1);
```
### 滑动的Item被放大
1. 需在根节点设置android:clipChildren为false即可，默认为true
可以通过android:layout_gravity控制超出的部分如何显示。
android:clipChildren的意思：是否限制子View在其范围内
需要在父节点和ViewPager界面设置android:clipChildren属性

2. setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer))方法
通过创建一个类实现ViewPager.PageTransformer然后重写transformPage方法来实现切换效果
```
    /**
     * 核心就是实现transformPage(View page, float position)这个方法
     **/
    @Override
    public void transformPage(View page, float position) {
        //以圆心进行缩放
        page.setPivotX(page.getWidth() / 2.0f);
        page.setPivotY(page.getHeight() / 2.0f);
        float scale = 1 - 0.433f * Math.abs(position);
        page.setScaleX(scale);
        page.setScaleY(scale);


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            page.getParent().requestLayout();
        }
    }
}
```