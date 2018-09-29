package tp.xmaihh.gallery.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import tp.xmaihh.gallery.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
    private Integer[] mImgs = {R.drawable.img_01, R.drawable.img_02,
            R.drawable.img_03, R.drawable.img_04, R.drawable.img_05, R.drawable.img_06};
    private Context mContext;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override

    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_gallery_view, null);
        // 自定义view的宽度,控制一屏显示的个数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        params.width = width / 3;   //这里每屏显示3个<将屏幕平均分为3份
        view.setLayoutParams(params);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        ImageView imageView = holder.itemView.findViewById(R.id.img);
        imageView.setImageResource(mImgs[position % mImgs.length]);
        TextView textView = holder.itemView.findViewById(R.id.text);
        textView.setText("img_0" + (position % mImgs.length));
        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                recycler.smoothScrollToPosition(position); 支持点击每一项滑动切换
                if (mOnItemClickListener != null) {
                    //注意这里使用getTag方法获取数据
                    mOnItemClickListener.onItemClick(v, v.getTag().toString());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    //定义一个接口
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }
}
