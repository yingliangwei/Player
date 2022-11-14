package com.example.player.utils;


import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

class VideoUtils {

    /**
     * 给出url，获取视频的第一帧
     */
    static String getVideoThumbnail(String url, String name) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(url, new HashMap());
            retriever.setDataSource(new FileDescriptor());
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return saveBitmap(bitmap, name);
    }

    /**
     * 获取本地视频的第一帧
     */
    static String getLocalVideoThumbnail(String filePath, String name) {
        Bitmap bitmap = null;
        //MediaMetadataRetriever 是android中定义好的一个类，提供了统一
        //的接口，用于从输入的媒体文件中取得帧和元数据；
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            //根据文件路径获取缩略图
            retriever.setDataSource(filePath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return saveBitmap(bitmap, name);
    }

    /**
     * 保存方法
     */
    private static String saveBitmap(Bitmap bitmap, String picName) {
        String path = "";
        File file = new File(Environment.getExternalStorageDirectory().getPath(), picName);
        if (!file.exists()) file.mkdir();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            path = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
