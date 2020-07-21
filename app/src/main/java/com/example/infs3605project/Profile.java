package com.example.infs3605project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Profile extends AppCompatActivity {

    Button profileBtn;
    TextView mUserName, mUserEmail;
    ImageView mProfilePic;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUserName = findViewById(R.id.userName);
        mUserEmail = findViewById(R.id.userEmail);
        mProfilePic = findViewById(R.id.profilePic);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        Log.d("PROFILE", "USERID: " + userId);

        //assign retrieved data using getProperties

        DocumentReference dr = fStore.collection("Users").document(userId);
        dr.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                //update the data
                mUserEmail.setText(documentSnapshot.getString("email"));
                mUserName.setText(documentSnapshot.getString("firstName"));
            }
        });



    }
}
