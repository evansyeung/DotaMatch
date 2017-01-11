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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextDotaName;
    private EditText editTextMMR;

    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;

    //Gets current user
    private FirebaseUser currentUser;


    private Spinner roleSpinner;

    ArrayAdapter<CharSequence> roleAdapter;

    private Button buttonSubmit;

    private String role;

    private boolean everythingFilled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        //Initialize firebase database object
        databaseReference = FirebaseDatabase.getInstance().getReference("User");

        //Initialize firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        editTextDotaName = (EditText) findViewById(R.id.editTextDotaName);
        editTextMMR = (EditText) findViewById(R.id.editTextMMR);

        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(this);

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
    }

    private void saveUserInformation() {
        everythingFilled = false;

        String DotaName = editTextDotaName.getText().toString().trim();
        String mmrTemp = editTextMMR.getText().toString().trim();

        if(TextUtils.isEmpty(DotaName)) {
            //email is empty
            Toast.makeText(this, "Please enter your Dota Name", Toast.LENGTH_SHORT).show();
            //Stop function exectuion
            return;
        }

        if(TextUtils.isEmpty(mmrTemp)) {
            //password is empty
            Toast.makeText(this, "Please enter your MMR", Toast.LENGTH_SHORT).show();
            //Stop function exectuion
            return;
        }

        if(role.equals("")) {
            //role not selected
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            //Stop function exectuion
            return;
        }

        User userInformation = new User(DotaName, Integer.valueOf(mmrTemp), role, 5);

        //Store user information into Firebase Database
        //Get unique id of login of current user
        databaseReference.child(currentUser.getUid()).setValue(userInformation);

        Toast.makeText(this, "Saving Information...", Toast.LENGTH_LONG).show();

        everythingFilled = true;
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSubmit) {
            saveUserInformation();
            if(everythingFilled) {
                finish();
                startActivity(new Intent(this, ProfileActivity.class));
            }
        }
    }
}