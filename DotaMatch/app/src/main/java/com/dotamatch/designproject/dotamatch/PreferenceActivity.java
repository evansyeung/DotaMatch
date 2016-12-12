package com.dotamatch.designproject.dotamatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PreferenceActivity extends AppCompatActivity implements View.OnClickListener{

    //To store data to Firebase database
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    private EditText editTextDotaName;
    private EditText editTextMMR;
    private EditText editTextRole;

    private RatingBar ratingBarRating;

    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        //Initialize firebase database object
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        //Initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        editTextDotaName = (EditText) findViewById(R.id.editTextDotaName);
        editTextMMR = (EditText) findViewById(R.id.editTextMMR);
        editTextRole = (EditText) findViewById(R.id.editTextRole);

        ratingBarRating = (RatingBar) findViewById(R.id.ratingBarRating);

        buttonSave = (Button) findViewById(R.id.buttonSave);

        ratingBarRating.setOnClickListener(this);

        buttonSave.setOnClickListener(this);
    }

    private void saveUserInformation() {
        String DotaName = editTextDotaName.getText().toString().trim();
        String mmrTemp = editTextMMR.getText().toString().trim();
        int mmr = Integer.valueOf(mmrTemp);
        String role = editTextRole.getText().toString().trim();
        float rating = ratingBarRating.getRating();

        User userInformation = new User(DotaName, mmr, role, rating);

        //To store user into Firebase Database
        //Get unique id of log in user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        databaseReference.child(user.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information Save...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSave) {
            saveUserInformation();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}