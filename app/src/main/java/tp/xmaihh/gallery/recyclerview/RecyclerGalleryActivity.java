package tp.xmaihh.gallery.recyclerview;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import tp.xmaihh.gallery.R;

public class RecyclerGalleryActivity extends AppCompatActivity implements GalleryLayoutManager.OnItemSelectedListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_gallery_view);
        hideBottomUIMenu();
        //*
        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        GalleryLayoutManager manager = new GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL);
        manager.attach(recyclerView, 1000000);
        // 设置滑动缩放效果
        manager.setItemTransformer(new Transformer());
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        manager.setOnItemSelectedListener(this);
        adapter.setmOnItemClickListener(new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                // 支持手动点击滑动切换
                recyclerView.smoothScrollToPosition(Integer.valueOf(data));
            }
        });
    }

    @Override
    public void onItemSelected(RecyclerView recyclerView, View item, int position) {

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
