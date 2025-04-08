package com.app.confession;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConfessionAdapter extends RecyclerView.Adapter<ConfessionAdapter.ConfessionViewHolder> {

    private List<Confession> confessionList;

    public ConfessionAdapter(List<Confession> confessionList) {
        this.confessionList = confessionList;
    }

    @NonNull
    @Override
    public ConfessionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_confession, parent, false);
        return new ConfessionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfessionViewHolder holder, int position) {
        Confession confession = confessionList.get(position);
        holder.contentText.setText(confession.getContent());
        holder.likesText.setText("Likes: " + confession.getLike());
    }

    @Override
    public int getItemCount() {
        return confessionList.size();
    }

    public static class ConfessionViewHolder extends RecyclerView.ViewHolder {
        TextView contentText, likesText;

        public ConfessionViewHolder(@NonNull View itemView) {
            super(itemView);
            contentText = itemView.findViewById(R.id.tvContent);
            likesText = itemView.findViewById(R.id.tvLikes);
        }
    }
}
