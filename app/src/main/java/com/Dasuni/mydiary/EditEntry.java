package com.Dasuni.mydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.Dasuni.mydiary.R;

import java.text.BreakIterator;
import java.util.Calendar;

public class EditEntry extends AppCompatActivity {
    private Context context;

    TextView selectedDateText;
    TextView selectedTimeText;
    EditText editTextTitle;
    EditText editTextDescription;
    ImageButton icn_calender;
    Button btnSave;
    Button btnDelete;
    Button btnImage;
    ImageView imgPreview;

    Uri selectedImageUri;

    //db
    private DBManager dbManager;

    private long _id;

    int SELECT_PICTURE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        context = this;

        //UI Components
        selectedDateText = findViewById(R.id.edit_date);
        selectedTimeText = findViewById(R.id.edit_time);
        icn_calender = findViewById(R.id.edit_icn_calender);
        editTextTitle = findViewById(R.id.edit_title);
        btnImage = findViewById(R.id.btn_edit_image);
        imgPreview = findViewById(R.id.edit_image_icon);
        editTextDescription = findViewById(R.id.edit_description);
        btnSave = findViewById(R.id.edit_btn_save);
        btnDelete = findViewById(R.id.btn_delete);

        dbManager = new DBManager(this);
        dbManager.open();

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        String image = intent.getStringExtra("image");

        _id = Long.parseLong(id);

        //set data from db to UI
        selectedDateText.setText(date);
        selectedTimeText.setText(time);
        editTextTitle.setText(title);
        editTextDescription.setText(desc);

        try{
            if (image!=null) {
                //convert to URI
                selectedImageUri = Uri.parse(image);

                // Load the image using Glide
                Glide.with(this)
                        .load(selectedImageUri)
                        .placeholder(R.drawable.camera_icon_foreground)
                        .into(imgPreview);
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_LONG).show();
        }

        //Date Picker
        icn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                BreakIterator todayDate;
                                selectedDateText.setText(year + " - " + String.format("%02d", (month + 1)) + " - " + String.format("%02d", dayOfMonth) );

                                String date = year + " - " + (month + 1) + " - " + dayOfMonth;
                            }
                        },
                        year, month, day
                );
                //disable future dates
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create an instance of the intent of the type image
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it with the returned requestCode
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
            }
        });
        //update
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //variables
                final String date = selectedDateText.getText().toString();
                final String time = selectedTimeText.getText().toString();
                final String title = editTextTitle.getText().toString();
                final String desc = editTextDescription.getText().toString();
                final String imageURI = selectedImageUri.toString();

                dbManager.update(_id,date,time,title,desc, imageURI);

                //show toast msg
                Toast.makeText(getApplicationContext(), "Diary updated successfully",Toast.LENGTH_LONG).show();

                //navigate to diary list
                Intent INT=new Intent(EditEntry.this, ViewDiaries.class);
                startActivity(INT);
            }
        });

        //delete
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbManager.delete(_id);

                //show toast msg
                Toast.makeText(getApplicationContext(), "Diary deleted successfully",Toast.LENGTH_LONG).show();

                //navigate to diary list
                Intent INT=new Intent(EditEntry.this, ViewDiaries.class);
                startActivity(INT);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    //imgPreview.setImageURI(selectedImageUri);
                    Glide.with(this)
                            .load(selectedImageUri)
                            .placeholder(R.drawable.camera_icon_foreground)
                            .into(imgPreview);
                }
            }
        }
    }
}