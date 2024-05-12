package com.Dasuni.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Dasuni.mydiary.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button addNewEntry;
    private Button viewDiaries;
    private Button settings;
    private Context context;

    TextView txtWelcome;
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        addNewEntry = findViewById(R.id.btnANE);
        viewDiaries = findViewById(R.id.btnVD);
        settings = findViewById(R.id.btnSett);
        txtWelcome = findViewById(R.id.Welcome);
        imgView = findViewById(R.id.img_logo);

        // Retrieving the value using its keys the file name
        SharedPreferences sh = getSharedPreferences("SharedPref", MODE_PRIVATE);

        // The value will be default as empty string
        String username = sh.getString("username", "Dasuni");

        // set username
        txtWelcome.setText("Welcome " + username);

        //set background image
        if(isNetworkAvailable()){
            String url = "https://source.unsplash.com/random";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(url).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(getApplicationContext(), "Failed to load background image",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String imageUrl = response.request().url().toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Picasso.get().load(imageUrl).into(imgView);
                        }
                    });

                }
            });
        }

        addNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AddNew.class);
                startActivity(intent);
            }
        });

        viewDiaries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewDiaries.class);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Settings.class);
                startActivity(intent);
            }
        });
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}