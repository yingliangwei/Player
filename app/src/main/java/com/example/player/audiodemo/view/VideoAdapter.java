package com.example.player.audiodemo.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.player.audiodemo.view.widget.VoiceImageView;
import com.example.player.databinding.CommunityAdapterChatListRightVoiceBinding;

import java.io.File;
import java.util.List;

/**
 * 适配器
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<File> list;
    private int mCurrentPlayAnimPosition = -1;//当前播放动画的位置

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CommunityAdapterChatListRightVoiceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder helper, int position) {
        VoiceImageView ivAudio = helper.binding.ivVoice;
        if (mCurrentPlayAnimPosition == helper.getLayoutPosition()) {
            ivAudio.startPlay();
        } else {
            ivAudio.stopPlay();
        }
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }


    /**
     * 开始播放动画
     *
     * @param position
     */
    public void startPlayAnim(int position) {
        mCurrentPlayAnimPosition = position;
        notifyDataSetChanged();
    }

    /**
     * 停止播放动画
     */
    public void stopPlayAnim() {
        mCurrentPlayAnimPosition = -1;
        notifyDataSetChanged();
    }

    public void setNewData(List<File> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CommunityAdapterChatListRightVoiceBinding binding;

        public ViewHolder(@NonNull CommunityAdapterChatListRightVoiceBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
