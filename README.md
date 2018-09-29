# GalleryViewDemo

1. 导包
```
implementation 'com.android.support:design:28.0.0'
implementation 'com.android.support:recyclerview-v7:28.0.0'
```
一个是ViewPager所在包,另一个是RecyclerView所在包

## RecyclerView实现GalleryView效果可(伪无限)无限左滑右滑
先上效果图
[](https://github.com/xmaihh/GalleryViewDemo/blob/master/app/arts/recyclerviewgallery.gif)

要点：
  1. 在有限的数据里面, 实现无限个Item
  2. 在第一次显示的时候, 就可以左滑
  3. 选中的Item被放大

用RecyclerView实现GalleryView效果已经有BCsl大神的BCsl/GalleryLayoutManager使用自定义 LayoutManager 实现 Android 中 Gallery 或者 ViewPager 控件的效果
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
### 选中的Item放大
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