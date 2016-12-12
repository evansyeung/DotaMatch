package com.dotamatch.designproject.dotamatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewUserEmail;

    private Button buttonPlay;
    private Button buttonMessage;
    private Button buttonPreference;
    private Button buttonLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);

        textViewUserEmail.setText("Welcome " + user.getEmail());

        buttonPlay = (Button) findViewById(R.id.buttonPlay);
        buttonMessage = (Button) findViewById(R.id.buttonMessage);
        buttonPreference = (Button) findViewById(R.id.buttonPreference);
        buttonLogOut = (Button) findViewById(R.id.buttonLogOut);

        buttonPlay.setOnClickListener(this);
        buttonMessage.setOnClickListener(this);
        buttonPreference.setOnClickListener(this);
        buttonLogOut.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonPlay) {
            finish();
            startActivity(new Intent(this, PartyActivity.class));
        }

        if(view == buttonMessage) {
            finish();
            startActivity(new Intent(this, ChatRoomActivity.class));
        }

        if(view == buttonPreference) {
            finish();
            startActivity(new Intent(this, PreferenceActivity.class));
        }

        if(view == buttonLogOut) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
