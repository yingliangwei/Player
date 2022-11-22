package com.example.player.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.player.R;
import com.example.player.base.VideoBase;
import com.example.player.databinding.ActivityMainBinding;
import com.example.player.recycler.MainAdapter;
import com.example.player.recycler.RecyclerItemClickListener;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.miraclegarden.library.app.MiracleGardenActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends MiracleGardenActivity<ActivityMainBinding> implements OnPermissionCallback {
    private final List<File> urls = new ArrayList<>();
    public static final List<VideoBase> videoBases = new ArrayList<>();
    private MainAdapter mainAdapter;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoadingDialog();
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Permission.RECORD_AUDIO)
                .permission(Permission.CAMERA)
                .request(this);
        initDataSpecs();
        initMediaMetadataRetriever();
        initView();
    }

    /**
     * 展示加载中窗口
     */
    public void showLoadingDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                return true;
            return false;
        });
        alertDialog.show();
        alertDialog.setContentView(R.layout.base_loading);
        alertDialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 在fragment里调用这个，关闭加载中弹窗
     * 关闭
     */
    public void dismissLoadingDialog() {
        if (null != alertDialog && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * 菜单项被点击时调用，也就是菜单项的监听方法。
         * 通过这几个方法，可以得知，对于Activity，同一时间只能显示和监听一个Menu 对象。 TODO Auto-generated
         * method stub
         */
        if (item.getItemId() == R.id.audio) {
            Intent intent = new Intent(this, MediaRecorderActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initMediaMetadataRetriever() {
    }

    private void initView() {
        RecyclerItemClickListener.OnItemClickListener.Normal normal = new RecyclerItemClickListener.OnItemClickListener.Normal() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra("file", urls.get(position).getAbsolutePath());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        };
        binding.recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, normal));
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mainAdapter = new MainAdapter(videoBases);
        binding.recycler.setAdapter(mainAdapter);
    }

    private void initDataSpecs() {
        urls.clear();
        File path = new File(getCacheDir() + File.separator + "JCamera");
        // 判断SD卡是否存在，并且是否具有读写权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File[] files = path.listFiles();// 读取文件夹下文件
            if (files != null) {
                Collections.addAll(urls, files);
            }
            System.out.println(urls.size());
        }
    }

    @Override
    public void onGranted(List<String> permissions, boolean all) {
        if (all) {
            //XXPermissions.startPermissionActivity(MainActivity.this, permissions);
        }
    }

    @Override
    protected void onResume() {
        videoBases.clear();
        initDataSpecs();
        for (File file : urls) {
            VideoBase videoBase = new VideoBase();
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(file.getAbsolutePath());
            videoBase.bitmap = retriever.getFrameAtTime();
            String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            String mime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            videoBase.time = Long.parseLong(timeString);
            videoBase.mime = mime;
            videoBase.name = file.getName();
            videoBase.album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            videoBases.add(videoBase);
        }
        mainAdapter.notifyDataSetChanged();
        dismissLoadingDialog();
        super.onResume();
    }
}
