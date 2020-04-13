package com.example.karunafightersdoctors;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Report extends AppCompatActivity {

    TextView textEnterPatientName, textEnterPatientId, textHospital, textDoctor, textResult;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");



        textEnterPatientName = (TextView) findViewById(R.id.textEnterPatientName);
        textEnterPatientId = (TextView) findViewById(R.id.textEnterPatientId);
        textDoctor = (TextView) findViewById(R.id.textDoctorName);
        textHospital = (TextView) findViewById(R.id.textHospitalName);
        textResult = (TextView) findViewById(R.id.textResults);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                try {
                    ReportClass reportClass = dataSnapshot.getValue(ReportClass.class);

                    textEnterPatientName.setText("Patient Name: "+reportClass.patients.patientName);
                    textEnterPatientId.setText("Patient Id: "+reportClass.patients.patientId);
                    textDoctor.setText("Doctor Name: "+reportClass.doctors.getDoctName());
                    textHospital.setText("Hospital: "+reportClass.users.nameOFHosp);

                    if(reportClass.patients.res==0){
                        textResult.setText("Negative");
                    }
                    else
                    {
                        textResult.setText("Positive");
                    }
                }catch (NullPointerException e){

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e("TAG", "Failed to read value.", error.toException());
            }
        });



    }
}
