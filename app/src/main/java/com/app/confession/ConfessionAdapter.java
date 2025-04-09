package com.app.confession;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
        holder.btnLike.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    URL url = new URL("http://10.0.2.2:8080/confession/v1/confession/2/like");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("PATCH");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    // Envia corpo vazio
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write("{}".getBytes("utf-8"));
                    }

                    int status = conn.getResponseCode();
                    Log.d("API", "Status: " + status);

                    if (status == 200 || status == 204) {
                        confession.setLike(confession.getLike() + 1);
                        holder.itemView.post(() -> notifyItemChanged(holder.getAdapterPosition()));
                    }

                    conn.disconnect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    @Override
    public int getItemCount() {
        return confessionList.size();
    }

    public static class ConfessionViewHolder extends RecyclerView.ViewHolder {
        TextView contentText, likesText;
        Button btnLike;
        public ConfessionViewHolder(@NonNull View itemView) {
            super(itemView);
            contentText = itemView.findViewById(R.id.tvContent);
            likesText = itemView.findViewById(R.id.tvLikes);
            btnLike =  itemView.findViewById(R.id.btnLike);
        }
    }

}
