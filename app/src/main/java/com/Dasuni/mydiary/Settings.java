package com.Dasuni.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.Dasuni.mydiary.R;

public class Settings extends AppCompatActivity {
    EditText editTextUsername;
    Button btnSaveUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextUsername = findViewById(R.id.editTextUser);
        btnSaveUser= findViewById(R.id.btn_saveuser);

        // Retrieving the value using its keys the file name
        SharedPreferences sh = getSharedPreferences("SharedPref", MODE_PRIVATE);

        // The value will be default as empty string
        String username = sh.getString("username", "Dasuni");

        editTextUsername.setText(username);

        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating an Editor object to edit(write to the file)
                SharedPreferences.Editor myEdit = sh.edit();

                // Storing the key and its value as the data fetched from edittext
                myEdit.putString("username", editTextUsername.getText().toString());

                // commit to apply those changes
                myEdit.commit();
                Toast.makeText(getApplicationContext(), "Username saved successfully", Toast.LENGTH_LONG).show();

            }
        });
    }
}