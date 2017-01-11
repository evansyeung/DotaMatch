package com.dotamatch.designproject.dotamatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PreferenceActivity extends AppCompatActivity implements View.OnClickListener{

    //Reference store data to Firebase database
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    //Gets current user
    private FirebaseUser currentUser;

    private EditText editTextDotaName;
    private EditText editTextMMR;

    private Spinner roleSpinner;

    ArrayAdapter<CharSequence> roleAdapter;

    private RatingBar ratingBarRating;

    private Button buttonUpdate;

    private String role;

    private boolean changesMade;
    private boolean error;

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

        ratingBarRating = (RatingBar) findViewById(R.id.ratingBarRating);

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);

        ratingBarRating.setOnClickListener(this);

        buttonUpdate.setOnClickListener(this);

        //Role dropdown menu setup
        roleSpinner = (Spinner) findViewById(R.id.roleSpinner);
        roleAdapter = ArrayAdapter.createFromResource(this, R.array.role_names, android.R.layout.simple_spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                role = parent.getItemAtPosition(position).toString();
                if(!role.equals("")) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " role is selected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(currentUser.getUid()).getValue(User.class);
                ratingBarRating.setRating(user.rating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    void updateDotaName(String DotaName) {
        if(DotaName.length() <= 15) {   //Check if name is within range
            if(!error) {
                databaseReference.child(currentUser.getUid()).child("DotaName").setValue(DotaName);
                changesMade = true;
            }
        }
        else {
            Toast.makeText(this, "Invalid name, please re-enter", Toast.LENGTH_SHORT).show();
            error = true;
        }
    }

    void updateMMR(Integer mmr) {
        if(mmr > 0 && mmr < 8000) { //Check for valid MMR
            if(!error) {
                databaseReference.child(currentUser.getUid()).child("MMR").setValue(mmr);
                changesMade = true;
            }
        }
        else {
            Toast.makeText(this, "Invalid MMR, please re-enter MMR", Toast.LENGTH_SHORT).show();
            error = true;
        }
    }

    void updateRole(String role) {
        if(!error) {
            databaseReference.child(currentUser.getUid()).child("role").setValue(role);
            changesMade = true;
        }
    }

    private void updateUserInformation() {
        String DotaName = editTextDotaName.getText().toString().trim();
        String mmrTemp = editTextMMR.getText().toString().trim();

        boolean bool_DotaName = TextUtils.isEmpty(DotaName);
        boolean bool_mmrTemp = TextUtils.isEmpty(mmrTemp);
        error = false;
        changesMade = false;

        if(bool_DotaName && bool_mmrTemp && role.equals("")) {   //No changes to attributes were made
            Toast.makeText(this, "No changes were made", Toast.LENGTH_SHORT).show();
            return;
        }else if(!bool_DotaName && !bool_mmrTemp && !role.equals("")) { //Changes were made to DotaName, MMR, and Role
            Integer mmr = Integer.valueOf(mmrTemp);
            updateDotaName(DotaName);
            updateMMR(mmr);
            updateRole(role);
        }else if(!bool_DotaName && !bool_mmrTemp) { //Changes were made to DotaName and MMR
            Integer mmr = Integer.valueOf(mmrTemp);
            updateDotaName(DotaName);
            updateMMR(mmr);
        }else if(!bool_DotaName && !role.equals("")) {  //Changes were made to DotaName and Role
            updateDotaName(DotaName);
            updateRole(role);
        }else if(!bool_mmrTemp && !role.equals("")) {   //Changes were made to MMR and Role
            Integer mmr = Integer.valueOf(mmrTemp);
            updateMMR(mmr);
            updateRole(role);
        }else if(!bool_DotaName){   //Change was made to DotaName
            updateDotaName(DotaName);
        }else if(!bool_mmrTemp) {   //Change were made to MMR
            Integer mmr = Integer.valueOf(mmrTemp);
            updateMMR(mmr);
        }else if(!role.equals("")) {    //Change was made to Role
            updateRole(role);
        }

       //Update message if changes were made
       if(changesMade) {
           Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onClick(View view) {
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
