package com.example.karunafightersdoctors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editHospName, editNodalName, editNodalDesig, editEmailAdd, editPhoneNum, editAddress, editCity, editState;
    EditText editPass;
    Button buttonSubmit;
    TextView textLogin;
    String formattedDate;
    RadioGroup radioGroup;
    RadioButton radioButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editHospName = (EditText) findViewById(R.id.editHospital);
        editNodalName = (EditText) findViewById(R.id.editPersonName);
        editNodalDesig = (EditText) findViewById(R.id.editNodalDesig);
        editEmailAdd = (EditText) findViewById(R.id.editemailAddress);
        editPhoneNum = (EditText) findViewById(R.id.editPhoneNumber);
        editAddress = (EditText) findViewById(R.id.editAddress);
        editCity = (EditText) findViewById(R.id.editCity);
        editState = (EditText) findViewById(R.id.editState);
        editPass = (EditText) findViewById(R.id.editPassword);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        textLogin = (TextView) findViewById(R.id.textLogin);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        mAuth = FirebaseAuth.getInstance();

        buttonSubmit.setOnClickListener(this);
        textLogin.setOnClickListener(this);


    }
    private void registerUser() {

        final String hospName = editHospName.getText().toString().trim();
        final String nodalPersonName = editNodalName.getText().toString().trim();
        final String nodalPErsonDes = editNodalDesig.getText().toString().trim();
        final String email = editEmailAdd.getText().toString().trim();
        final String phone = editPhoneNum.getText().toString().trim();
        final String password = editPass.getText().toString().trim();
        final String address = editAddress.getText().toString().trim();
        final String state = editState.getText().toString().trim();
        final String city = editCity.getText().toString().trim();

        if (hospName.isEmpty()) {
            editHospName.setError("Name is required");
            editHospName.requestFocus();
            return;
        }
        if (nodalPersonName.isEmpty()) {
            editNodalName.setError("Name is required");
            editNodalName.requestFocus();
            return;
        }
        if (nodalPErsonDes.isEmpty()) {
            editNodalDesig.setError("Name is required");
            editNodalDesig.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            editPhoneNum.setError("Name is required");
            editPhoneNum.requestFocus();
            return;
        }
        if (address.isEmpty()) {
            editAddress.setError("Name is required");
            editAddress.requestFocus();
            return;
        }
        if (city.isEmpty()) {
            editCity.setError("Name is required");
            editCity.requestFocus();
            return;
        }

        if (state.isEmpty()) {
            editState.setError("Name is required");
            editState.requestFocus();
            return;
        }



        if (email.isEmpty()) {
            editEmailAdd.setError("Email is required");
            editEmailAdd.requestFocus();
            return;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmailAdd.setError("Enter valid email address");
            editEmailAdd.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPass.setError("Password is required");
            editPass.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPass.setError("Enter valid password");
            editPass.requestFocus();
            return;
        }



        if (phone.length() != 10) {
            editPhoneNum.setError("Enter valid phone number");
            editPhoneNum.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Users user = new Users(
                                                hospName,
                                                nodalPersonName,
                                                nodalPErsonDes,
                                                email,
                                                phone,
                                                address,
                                                city,
                                                state,
                                                password
                                        );
                                        Calendar c = Calendar.getInstance();
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        formattedDate = df.format(c.getTime());

                                        FirebaseDatabase.getInstance().getReference("Users").child("Hospitals")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUpActivity.this, "registration_success: and check your email address", Toast.LENGTH_LONG).show();
                                                } else {
                                                    //display a failure message
//                                                    Toast.makeText(SignUpActivity.this, "registration_Failure", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
//                                        Intent intent = new Intent(SignUpActivity.this,Booking.class);

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }

                                }
                            });




                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSubmit:
                registerUser();
                break;

            case R.id.textLogin:

                startActivity (new Intent(this, LoginActivity.class));

                break;
        }
    }



}