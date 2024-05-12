package com.Dasuni.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.Dasuni.mydiary.R;

public class ViewDiaries extends AppCompatActivity {
    private DBManager dbManager;

    private ListView listView;

    private SimpleCursorAdapter adapter;

    final String[] from = new String[] {
            DatabaseHelper.DATE,
            DatabaseHelper.TIME,
            DatabaseHelper._ID,
            DatabaseHelper.IMAGE,
            DatabaseHelper.TITLE,
            DatabaseHelper.DESCRIPTION
    };

    final int[] to = new int[] { R.id.txtdate, R.id.txttime,R.id.txtid,R.id.txtimage,R.id.txttitle, R.id.txtdiscription  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_diaries);

        //view entry items
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.ListV);

        adapter = new SimpleCursorAdapter(this, R.layout.onepageview, cursor, from, to, 0);
        adapter.notifyDataSetChanged();


        listView.setAdapter(adapter);

        //OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView titleText = (TextView) view.findViewById(R.id.txttitle);
                TextView descriptionText = (TextView) view.findViewById(R.id.txtdiscription);
                TextView dateText = (TextView) view.findViewById(R.id.txtdate);
                TextView timeText = (TextView) view.findViewById(R.id.txttime);
                TextView idText = (TextView) view.findViewById(R.id.txtid);
                TextView imageText = (TextView) view.findViewById(R.id.txtimage);

                String title = titleText.getText().toString();
                String description = descriptionText.getText().toString();
                String date = dateText.getText().toString();
                String time = timeText.getText().toString();
                String id = idText.getText().toString();
                String image = imageText.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), EditEntry.class);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("desc", description);
                modify_intent.putExtra("date", date);
                modify_intent.putExtra("time", time);
                modify_intent.putExtra("image", image);

                startActivity(modify_intent);
            }
        });
    }
}