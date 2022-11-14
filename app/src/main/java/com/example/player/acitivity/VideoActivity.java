package com.example.player.acitivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.player.databinding.ActivityVideoBinding;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.miraclegarden.library.app.MiracleGardenActivity;

public class VideoActivity extends MiracleGardenActivity<ActivityVideoBinding> {
    private String url;
    private ExoPlayer player;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        url = getIntent().getStringExtra("url");
        if (url == null) {
            return;
        }
        initExoPlayer(url);
    }


    private void initExoPlayer(String url) {
        player = new ExoPlayer.Builder(this).build();
        binding.exoPlay.setPlayer(player);
        try {
            /*RawResourceDataSource rawSource = new RawResourceDataSource(this);
            rawSource.open(new DataSpec(RawResourceDataSource.buildRawResourceUri(R.raw.player)));
            player.setMediaItem(MediaItem.fromUri(Objects.requireNonNull(rawSource.getUri())));*/
           /* MediaItem mediaItem = MediaItem.fromUri("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
            MediaItem mediaItem2 = MediaItem.fromUri("http://vjs.zencdn.net/v/oceans.mp4");
            player.addMediaItem(mediaItem);
            player.addMediaItem(mediaItem2);*/
            MediaItem mediaItem = MediaItem.fromUri(url);
            player.addMediaItem(mediaItem);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player.prepare();
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.pause();
    }
}
