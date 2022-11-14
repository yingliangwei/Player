package com.example.player.recycler;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.player.base.VideoBase;
import com.example.player.databinding.ItemMainRecyclerBinding;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<VideoBase> videoBases;

    public MainAdapter(List<VideoBase> videoBases) {
        this.videoBases = videoBases;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMainRecyclerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VideoBase videoBase = videoBases.get(position);
        holder.binding.title.setText("视频" + position);
        try {
            holder.binding.duration.setText(videoBase.time + "ms");
            if (videoBase.isMp3) {
                holder.binding.image.setVisibility(View.GONE);
                holder.binding.type.setText("音频");
                holder.binding.title.setText("音频" + position);
            } else {
                holder.binding.image.setImageBitmap(videoBase.bitmap);
            }
            if (videoBase.album!=null){
                holder.binding.title.setText(videoBase.album);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return videoBases.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMainRecyclerBinding binding;

        public ViewHolder(@NonNull ItemMainRecyclerBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
