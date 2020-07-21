package com.example.infs3605project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText mEmail, mPassword, mFirstName, mLastName, mConfirmPassword, mDegree, mBirthYear, mFindOutInfo;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    FirebaseFirestore fStore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialise
        mEmail = findViewById(R.id.eMail);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirmPassword);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mLoginBtn = findViewById(R.id.createText);
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);
        mBirthYear = findViewById(R.id.birthYear);
        mFindOutInfo = findViewById(R.id.findOutInfo);
        mDegree = findViewById(R.id.degree);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        } //checks if the user is already logged in so they don't have to see the registration

        mRegisterBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final String email = mEmail.getText().toString().trim(); //collect email text and trim
                String password = mPassword.getText().toString().trim();
                String confirmPassword = mConfirmPassword.getText().toString().trim();

                final String firstName = mFirstName.getText().toString();
                final String lastName = mLastName.getText().toString();
                final String birthYear = mBirthYear.getText().toString();
                final String findOutInfo = mFindOutInfo.getText().toString();
                final String degree = mDegree.getText().toString();

                if(TextUtils.isEmpty(email)){ //checks to make sure email is not empty
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)){ //checks to make sure password is not empty
                    mPassword.setError("Password is required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must 6 or more characters");
                    return;
                }

                if(!password.equals(confirmPassword)) { // if
                    mConfirmPassword.setError("Passwords do not match");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //REGISTER THE USER IN FIREBASE

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //in here you check if the registration is successful or not
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"User Created.", Toast.LENGTH_SHORT).show();
                            //store data to fireStore
                            userID = fAuth.getCurrentUser().getUid(); //getting user ID of currently registered user
                            DocumentReference documentReference = fStore.collection("Users").document(userID); //using USERID to create document
                            //create user data using hashmap
                            Map<String, Object> user = new HashMap<>();
                            user.put("firstName", firstName);
                            user.put("lastName", lastName);
                            user.put("email", email);
                            user.put("birthYear", birthYear);
                            user.put("degree", degree);
                            user.put("how did you find out about CyberGrad?", findOutInfo);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("REGISTER", "onSuccess: user profile is created for " + userID);
                                }
                            }) .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("REGISTER", "onFailure: " + e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),MainActivity.class)); //take users to the main activity
                        }else{
                            Toast.makeText(Register.this,"An Error has occurred. User not created" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //for if user already has an account

        mLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(getApplicationContext(),Login.class)); //takes the user to the login page
            }
        });

    }
}
