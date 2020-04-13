package com.example.karunafighterspatients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editEmail, editPassword;
    Button buttonSubmit;
    TextView textSignUp, textForgot;
    FirebaseAuth mAuth;
    public static final String TAG="TAG";

    public static String newConifrmed;
    public static String totalConfirmed;
    public static String newDeaths;
    public static String totalDeaths;
    public static String newRecovered;
    public static String totalRecovered;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // to get instance for firebaseAuth
        mAuth = FirebaseAuth.getInstance();

        new GetCaronaDetails().execute();
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPass);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        textSignUp = (TextView) findViewById(R.id.textSignUp);
        textForgot = (TextView) findViewById(R.id.textForgot);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"heheifhiefh",Toast.LENGTH_LONG).show();
                userlogin();
            }
        });
        textSignUp.setOnClickListener(this);
    }

    //method for user Login
    private void userlogin() {
        // to get email from user and store it in variable called email
        String email = editEmail.getText().toString().trim();

        // to get Password from user and store it in variable called Password
        String PassWord = editPassword.getText().toString().trim();

        //to check editText of email should not be empty
        //if it is empty then
        if (email.isEmpty()) {
            //set an error
            editEmail.setError("Email is required");
            //and highlight that box
            editEmail.requestFocus();
            return;
        }

        //to match the pattern of email address
        //if it not matches then
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //set an error
            editEmail.setError("Please enter a valid email address");
            // and highlight that box
            editEmail.requestFocus();
            return;
        }


        //to check editText of password that should not be empty
        //if it is empty then

        if (PassWord.isEmpty()) {
            //set an error
            editPassword.setError("Password is required");
            //it will focus on password
            editPassword.requestFocus();
            return;
        }

        //length of password should be under six characters
        if (PassWord.length() < 6) {
            editPassword.setError("Minimum length of password required is 6");
            editPassword.requestFocus();
            return;
        }

        //for login

        //for sign in with valid email address and Password
        mAuth.signInWithEmailAndPassword(email, PassWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //if login is successful then

                    if (mAuth.getCurrentUser().isEmailVerified()) {

                        Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LoginActivity.this, AnalyticsActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_LONG).show();

                    }


                } else {
                    //else
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.textSignUp:

                startActivity(new Intent(this, SignUpActivity.class));

                break;
//            case R.id.buttonLogin:
//                userlogin();
//                break;

        }


    }
    public class GetCaronaDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
//            pDialog = new ProgressDialog(CameraActivity.this);
//            pDialog.setMessage("Please wait...");
//            pDialog.setCancelable(false);
//            pDialog.show();

        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String url1 = "https://api.covid19api.com/summary";


            // Making a request to url and getting response
            String jsonStrAqi = sh.makeServiceCall(url1);


            Log.e(TAG,"Response from url: "+jsonStrAqi);


            if (jsonStrAqi !=null) {
                try {


                    JSONObject jsonObject2 = new JSONObject(jsonStrAqi);
                    JSONArray jsonObject1 = jsonObject2.getJSONArray("Countries");

                    for(int i=0;i<jsonObject1.length();i++) {

                        JSONObject c = jsonObject1.getJSONObject(i);
                        if(c.getString("Country").equals("India")) {
                            newConifrmed = c.getString("NewConfirmed");
                            totalConfirmed = c.getString("TotalConfirmed");
                            newDeaths = c.getString("NewDeaths");
                            totalDeaths = c.getString("TotalDeaths");
                            newRecovered = c.getString("NewRecovered");
                            totalRecovered = c.getString("TotalRecovered");
                        }



                        Log.e(TAG, "NewConfirmed: "+newConifrmed+"\ntotal confirmed: " + totalConfirmed + "\nnewDeaths: " + newDeaths + "\ntotalDeaths: " + totalDeaths
                                +"\nNew Recoverd: "+newRecovered+"\nTotal Recovered: "+totalRecovered);
                    }


//                    Log.e(TAG, "New Confirmed: "+jsonObject2.getString("NewConfirmed"));



                }
                catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
//            if (pDialog.isShowing())
//                pDialog.dismiss();

        }

    }
}
