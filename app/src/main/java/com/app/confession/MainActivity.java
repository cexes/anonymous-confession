package com.app.confession;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Confession> confessionList = new ArrayList<>();
    private ConfessionAdapter confessionAdapter;
    private RecyclerView recyclerView;
    private TextView confession_text;
    private Button send_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        confession_text = findViewById(R.id.etConfession);
        send_btn = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.rvConfessions);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        confessionAdapter = new ConfessionAdapter(confessionList);
        recyclerView.setAdapter(confessionAdapter);

        setText();
        sendConfession();
        getAllConfession();
    }

    public void setText() {
        confession_text.setOnClickListener(v -> confession_text.setText(""));
    }

    public void sendConfession() {
        send_btn.setOnClickListener(v -> {
            String confession = confession_text.getText().toString();
            if (confession.isEmpty()) return;

            new Thread(() -> {
                try {
                    JSONObject json = new JSONObject();
                    json.put("content", confession);

                    String jsonString = json.toString();
                    Log.d("JSON", jsonString);

                    URL url = new URL("");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonString.getBytes("utf-8");
                        os.write(input, 0, input.length);
                    }

                    int status = conn.getResponseCode();
                    Log.d("API", "Status: " + status);

                    conn.disconnect();


                    getAllConfession();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    public void getAllConfession() {
        new Thread(() -> {
            try {
                URL url = new URL("");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                InputStream inputStream = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                String json = response.toString();
                JSONArray jsonArray = new JSONArray(json);
                List<Confession> updatedConfessions = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Confession confession = new Confession();
                    confession.setContent(obj.getString("content"));
                    confession.setLike(obj.optInt("like", 0));
                    updatedConfessions.add(confession);
                }

                runOnUiThread(() -> {
                    confessionList.clear();
                    confessionList.addAll(updatedConfessions);
                    confessionAdapter.notifyDataSetChanged();

                    recyclerView.scrollToPosition(confessionList.size() - 1);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
