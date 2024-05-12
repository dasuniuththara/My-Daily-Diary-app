 package com.Dasuni.mydiary;

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
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

 import com.Dasuni.mydiary.R;
 import com.Dasuni.mydiary.R;


 import java.text.BreakIterator;
 import java.text.SimpleDateFormat;
 import java.time.LocalDate;
 import java.time.format.DateTimeFormatter;
 import java.util.Calendar;
 import java.util.Date;

 public class AddNew extends AppCompatActivity {

     private ImageView icn_back, icn_camera;
     private ImageButton icn_calender;
     private EditText editText_date , editText_title, editText_description, editText_time;
     private Button btn_add_image, btn_save;

     private Context context;
     Uri selectedImageUri;

     // constant to compare the activity result code
     int SELECT_PICTURE = 200;

     private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        context = this;

        String date;

        icn_back = findViewById(R.id.icn_back);
        icn_camera = findViewById(R.id.icn_camera);
        icn_calender = findViewById(R.id.icn_calender);
        editText_date = findViewById(R.id.editText_date);
        editText_title = findViewById(R.id.editText_title);
        editText_description = findViewById(R.id.editText_description);
        editText_time = findViewById(R.id.editText_time);
        btn_add_image = findViewById(R.id.btn_add_image);
        btn_save = findViewById(R.id.btn_save);


        //back to home
        icn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        });

        //Set date
        LocalDate localdate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = formatter.format(localdate);
        editText_date.setText(formattedDate);
        date =formattedDate;

        //set current time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

        // for current date and time and calling a simple date format in it.
        String currentTime = sdf.format(new Date());
        editText_time.setText(currentTime);

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
                                editText_date.setText(year + " - " + String.format("%02d", (month + 1)) + " - " + String.format("%02d", dayOfMonth) );

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

        // handle the Choose Image button to trigger
        btn_add_image.setOnClickListener(new View.OnClickListener() {
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

        //db
        dbManager = new DBManager(this);
        dbManager.open();

        //save page
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText_title.getText().toString();
                String description = editText_description.getText().toString();
                String date = editText_date.getText().toString();
                String time = editText_time.getText().toString();

                if(title.matches("")){
                    Toast.makeText(getApplicationContext(), "Title is empty",Toast.LENGTH_LONG).show();
                }else if(description.matches("")){
                    Toast.makeText(getApplicationContext(), "Description is empty",Toast.LENGTH_LONG).show();
                }else{
                    if(selectedImageUri!=null){
                        final String imageURI = selectedImageUri.toString();
                        dbManager.insert(date,time,title,description, imageURI);
                    }else{
                        dbManager.insert(date,time,title,description, null);
                    }
                    //show toast msg
                    Toast.makeText(getApplicationContext(), "Diary added successfully",Toast.LENGTH_LONG).show();
                    //navigate to diary list
                    Intent INT=new Intent(AddNew.this, ViewDiaries.class);
                    startActivity(INT);
                }
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
                     icn_camera.setImageURI(selectedImageUri);
                 }
             }
         }
     }
}