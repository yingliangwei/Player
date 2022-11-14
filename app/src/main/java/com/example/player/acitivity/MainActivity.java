package com.example.player.acitivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends MiracleGardenActivity<ActivityMainBinding> implements OnPermissionCallback {
    private final List<String> urls = new ArrayList<>();
    private final List<VideoBase> videoBases = new ArrayList<>();
    private MainAdapter mainAdapter;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoadingDialog();
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .permission(Permission.RECORD_AUDIO)
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
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                    return true;
                return false;
            }
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
            Intent intent = new Intent(this, com.example.player.audiodemo.view.MainActivity.class);
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
        new Thread(() -> {
            for (String url : urls) {
                try {
                    VideoBase videoBase = new VideoBase();
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    retriever.setDataSource(url, new HashMap());
                    videoBase.bitmap = retriever.getFrameAtTime();
                    String timeString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    String mime = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
                    videoBase.time = Long.parseLong(timeString);
                    videoBase.mime = mime;
                    videoBase.album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    if (url.contains("mp3")) {
                        videoBase.isMp3 = true;
                    }
                    videoBases.add(videoBase);
                    retriever.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                mainAdapter.notifyDataSetChanged();
                dismissLoadingDialog();
            });
        }).start();
    }

    private void initView() {
        RecyclerItemClickListener.OnItemClickListener.Normal normal = new RecyclerItemClickListener.OnItemClickListener.Normal() {
            @Override
            public void onItemClick(View view, int position) {
                String url = urls.get(position);
                if (url.contains("mp3")) {
                    return;
                }
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                intent.putExtra("url", url);
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
        //urls.add("https://vdn3.vzuu.com/HD/2fa45ebe-a096-11ea-9903-92eb25be4f16.mp4?disable_local_cache=1&bu=http-da4bec50&c=avc.0.0&f=mp4&expiration=1668342927&auth_key=1668342927-0-0-259a936fd884f92a8410a79a12920901&v=tx&pu=da4bec50");
        urls.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        urls.add("http://vjs.zencdn.net/v/oceans.mp4");
        urls.add("https://www.xzmp3.com/down/93827b74a196.mp3");
    }

    @Override
    public void onGranted(List<String> permissions, boolean all) {
        if (all) {
            //XXPermissions.startPermissionActivity(MainActivity.this, permissions);
        }
    }
}
