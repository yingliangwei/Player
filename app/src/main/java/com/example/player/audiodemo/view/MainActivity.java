package com.example.player.audiodemo.view;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.player.R;
import com.example.player.audiodemo.MainContract;
import com.example.player.audiodemo.MainPresenter;
import com.example.player.audiodemo.view.widget.RecordAudioButton;
import com.example.player.recycler.RecyclerItemClickListener;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MainActivity<T extends MainContract.Presenter> extends AppCompatActivity implements MainContract.View {

    LinearLayout mRoot;
    RecyclerView mRvMsg;//消息列表
    RecordAudioButton mBtnVoice;//底部录制按钮

    private Context mContext;
    private VideoAdapter mAdapter = new VideoAdapter();//适配器
    private RecordVoicePopWindow mRecordVoicePopWindow;//提示
    private MainContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mPresenter = new MainPresenter<MainContract.View>(this, this);
        setContentView(R.layout.activity_main1);
        requestPermission();//请求麦克风权限
        initView();//初始化布局
        mPresenter.init();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //android.R.id.home对应应用程序图标的id
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mRoot = findViewById(R.id.root);
        mRvMsg = findViewById(R.id.rvMsg);
        mBtnVoice = findViewById(R.id.btnVoice);
        mBtnVoice.setOnVoiceButtonCallBack(new RecordAudioButton.OnVoiceButtonCallBack() {
            @Override
            public void onStartRecord() {
                mPresenter.startRecord();
            }

            @Override
            public void onStopRecord() {
                mPresenter.stopRecord();
            }

            @Override
            public void onWillCancelRecord() {
                mPresenter.willCancelRecord();
            }

            @Override
            public void onContinueRecord() {
                mPresenter.continueRecord();
            }
        });
        mRvMsg.setLayoutManager(new LinearLayoutManager(mContext));
        RecyclerItemClickListener.OnItemClickListener.Normal normal=new RecyclerItemClickListener.OnItemClickListener.Normal() {
            @Override
            public void onItemClick(View view, int position) {
                mPresenter.startPlayRecord(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        };
        mRvMsg.addOnItemTouchListener(new RecyclerItemClickListener(this,normal));
        mRvMsg.setAdapter(mAdapter);
    }

    private void requestPermission() {

       /* PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK)
                .request();*/
    }

    @Override
    public void showList(List<File> list) {
        mAdapter.setNewData(list);
    }

    @Override
    public void showNormalTipView() {
        if (mRecordVoicePopWindow == null) {
            mRecordVoicePopWindow = new RecordVoicePopWindow(mContext);
        }
        mRecordVoicePopWindow.showAsDropDown(mRoot);
    }

    @Override
    public void showTimeOutTipView(int remainder) {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showTimeOutTipView(remainder);
        }
    }

    @Override
    public void showRecordingTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showRecordingTipView();
        }
    }

    @Override
    public void showRecordTooShortTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showRecordTooShortTipView();
        }
    }

    @Override
    public void showCancelTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.showCancelTipView();
        }
    }

    @Override
    public void hideTipView() {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.dismiss();
        }
    }

    @Override
    public void updateCurrentVolume(int db) {
        if (mRecordVoicePopWindow != null) {
            mRecordVoicePopWindow.updateCurrentVolume(db);
        }
    }

    @Override
    public void startPlayAnim(int position) {
        mAdapter.startPlayAnim(position);
    }

    @Override
    public void stopPlayAnim() {
        mAdapter.stopPlayAnim();
    }
}
