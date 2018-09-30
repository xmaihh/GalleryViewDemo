package tp.xmaihh.gallery.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import tp.xmaihh.gallery.R;
import tp.xmaihh.gallery.viewpager.salvage.RecyclingPagerAdapter;

public class ViewPagerAdapter extends RecyclingPagerAdapter {
    private Integer[] mImgs = {R.drawable.img_06, R.drawable.img_01, R.drawable.img_02,
            R.drawable.img_03, R.drawable.img_04, R.drawable.img_05, R.drawable.img_06, R.drawable.img_01};
    private Context mContext;

    public ViewPagerAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        ImageView imageView = null;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setTag(position % mImgs.length);
        imageView.setImageResource(mImgs[position % mImgs.length]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击的位置是:::" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return imageView;
    }

    @Override
    public int getCount() {
        return mImgs.length == 0 ? 0 : mImgs.length;
//        return Integer.MAX_VALUE;
    }


    /**
     * 该方法返回结果默认为1.0，其效果为ViewPager的Item占满整个ViewPager控件的宽度，
     * 如果我们将返回的结果重写为小于1的数，则Item会相对默认效果变小，
     * 两边的Item也会相应地靠近过来，从而来到屏幕可见的区域，实现了我们想要的效果
     */

//    @Override
//    public float getPageWidth(int position) {
//        return 1.0f;
//    }

    private int mChildCount = 0;//Viewpager中子View的数量

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
