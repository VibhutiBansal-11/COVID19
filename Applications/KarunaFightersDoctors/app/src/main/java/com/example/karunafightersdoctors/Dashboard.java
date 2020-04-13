package com.example.karunafightersdoctors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity {

    Button addDoctors, addMedicalAssistance, addPAtients,listOfDoctors;
    String title="Require Medical Assistance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        addDoctors = (Button) findViewById(R.id.buttonAdding);
        addMedicalAssistance = (Button) findViewById(R.id.buttonMedicalAssistance);
        addPAtients = (Button) findViewById(R.id.buttonAddingPatient);
        listOfDoctors = (Button) findViewById(R.id.buttonListOfDoctors);

        addDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,AddDoctor.class );
                startActivity(i);
            }
        });
        listOfDoctors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, DoctorsList.class);
                startActivity(i);
            }
        });

        addPAtients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this, AddPatient.class);
                startActivity(i);
            }
        });

        addMedicalAssistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"ram@gmail.com"};
        String[] CC = {"hospital@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Title");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "What you want");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Dashboard.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
