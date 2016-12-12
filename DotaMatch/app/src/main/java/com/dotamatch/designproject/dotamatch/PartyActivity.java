package com.dotamatch.designproject.dotamatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PartyActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonStart;

    private TextView textViewUser;

    private ProgressDialog progressDialog;

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    //User user;

    DatabaseReference databaseReference = firebaseDatabase.getReference("/User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        buttonStart = (Button) findViewById(R.id.buttonStart);

        textViewUser = (TextView) findViewById(R.id.textViewUser);

        progressDialog = new ProgressDialog(this);

        textViewUser.setText("Aooke");

        buttonStart.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == buttonStart) {
            progressDialog.setMessage("Searching for members...");
            progressDialog.show();
        }
}


    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
