package com.example.karunafightersdoctors;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddDoctor extends AppCompatActivity {

    ImageView imageView;
    EditText editDoctorName, editSpecialisation, editDocID, editEmailID;

    Button buttonSubmit, buttonDashboard, buttonList;

    private int PICK_IMAGE_REQUEST = 1;

    private StorageReference mStorage;
    private Uri file;
    String timeStamp;
    public String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        mStorage = FirebaseStorage.getInstance().getReference();

        imageView = (ImageView) findViewById(R.id.imageDoctor);

        editDoctorName = (EditText) findViewById(R.id.editdoctorName);
        editDocID = (EditText) findViewById(R.id.editDocId);
        editSpecialisation = (EditText) findViewById(R.id.editSpecialisation);
        editEmailID = (EditText) findViewById(R.id.editDocEmail);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        buttonList = (Button) findViewById(R.id.buttonListOfDoctors);
        buttonDashboard = (Button) findViewById(R.id.buttonDashboard);

        buttonDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddDoctor.this, Dashboard.class);
                startActivity(i);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
//                uploadFile();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDoctorDetails();
//                uploadFile();

            }
        });
        buttonList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddDoctor.this, DoctorsList.class);
                startActivity(i);
            }
        });
    }

    public void addDoctorDetails(){
        final String doctorName = editDoctorName.getText().toString().trim();
        final String specialisation = editSpecialisation.getText().toString().trim();
        final String doctorId = editDocID.getText().toString().trim();
        final String emailid = editEmailID.getText().toString().trim();

        if (doctorName.isEmpty()) {
            editDoctorName.setError("Name is required");
            editDoctorName.requestFocus();
            return;
        }
        if (specialisation.isEmpty()) {
            editSpecialisation.setError("Name is required");
            editSpecialisation.requestFocus();
            return;
        }
        if (doctorId.isEmpty()) {
            editDocID.setError("Name is required");
            editDocID.requestFocus();
            return;
        }
        if (emailid.isEmpty()) {
            editEmailID.setError("Name is required");
            editEmailID.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()) {
            editEmailID.setError("Enter valid email address");
            editEmailID.requestFocus();
            return;
        }

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formattedDate = df.format(c.getTime());

        Doctors doc = new Doctors(doctorName, specialisation, doctorId, emailid);

        FirebaseDatabase.getInstance().getReference("Users").child("Hospitals").child("Doctors")
                .child(formattedDate)
                .setValue(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddDoctor.this, "Successfully added", Toast.LENGTH_LONG).show();
                } else {
                    //display a failure message
//                                                    Toast.makeText(SignUpActivity.this, "registration_Failure", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void uploadFile() {

    }




}
