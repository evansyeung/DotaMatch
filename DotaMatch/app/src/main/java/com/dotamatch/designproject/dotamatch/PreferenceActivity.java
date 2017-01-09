package com.dotamatch.designproject.dotamatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

    private FirebaseUser currentUser;

    private EditText editTextDotaName;
    private EditText editTextMMR;
    private EditText editTextRole;

    private RatingBar ratingBarRating;

    private Button buttonSave;
    private Button buttonUpdate;

    private String[] roles = {"disabler", "initiator", "jungler", "support", "durable", "nuker", "pusher", "escape"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        //Initialize firebase database object
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        //Initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        editTextDotaName = (EditText) findViewById(R.id.editTextDotaName);
        editTextMMR = (EditText) findViewById(R.id.editTextMMR);
        editTextRole = (EditText) findViewById(R.id.editTextRole);

        ratingBarRating = (RatingBar) findViewById(R.id.ratingBarRating);

        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

        ratingBarRating.setOnClickListener(this);

        buttonSave.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
    }

    private void saveUserInformation() {
        String DotaName = editTextDotaName.getText().toString().trim();
        String mmrTemp = editTextMMR.getText().toString().trim();
        int mmr = Integer.valueOf(mmrTemp);
        String role = editTextRole.getText().toString().trim();
        float rating = ratingBarRating.getRating();

        User userInformation = new User(DotaName, mmr, role, rating);

        //Store user information into Firebase Database
        //Get unique id of login of current user

        databaseReference.child(currentUser.getUid()).setValue(userInformation);

        Toast.makeText(this, "Information Save...", Toast.LENGTH_LONG).show();
    }

    private void updateUserInformation() {
        String DotaName = editTextDotaName.getText().toString().trim();
        String mmrTemp = editTextMMR.getText().toString().trim();
        String role = editTextRole.getText().toString().trim();
        //float rating = ratingBarRating.getRating();

        boolean changesMade = false;
        boolean bool_DotaName = TextUtils.isEmpty(DotaName);
        boolean bool_mmrTemp = TextUtils.isEmpty(mmrTemp);
        boolean bool_Role = TextUtils.isEmpty(role);

        if(bool_DotaName && bool_mmrTemp && bool_Role) {   //No changes to attributes were made
            Toast.makeText(this, "No changes were made", Toast.LENGTH_SHORT).show();
            return;
        }else if(!bool_DotaName){
            if(DotaName.length() <= 15) {   //Check if name is within range
                databaseReference.child(currentUser.getUid()).child("DotaName").setValue(DotaName);
                changesMade = true;
            }
            else
                Toast.makeText(this, "Invalid name, please re-enter", Toast.LENGTH_SHORT).show();
        }else if(!bool_mmrTemp) {
            Integer mmr = Integer.valueOf(mmrTemp);
            if(mmr > 0 && mmr < 8000) { //Check for valid MMR
                databaseReference.child(currentUser.getUid()).child("MMR").setValue(mmr);
                changesMade = true;
            }
            else
                Toast.makeText(this, "Invalid MMR, please re-enter MMR", Toast.LENGTH_SHORT).show();
        }else if(!bool_Role) {  //Check for valid role
            boolean match = false;
            for(int i=0; i < roles.length; i++) {
                if(role.toLowerCase() == roles[i]) {
                    match = true;
                }
            }

            if(match) {
                databaseReference.child(currentUser.getUid()).child("role").setValue(role);
                changesMade = true;
            }
            else
                Toast.makeText(this, "Invalid role, please re-enter role", Toast.LENGTH_SHORT).show();
        }

       //Update message if changes were made
       if(changesMade) {
           Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSave) {
            saveUserInformation();
        }
        if(view == buttonUpdate) {
            updateUserInformation();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
