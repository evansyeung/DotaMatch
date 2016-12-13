package com.dotamatch.designproject.dotamatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PartyActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonStart;
    private Button buttonSearch;
    private Button buttonSaveRating;

    private TextView textViewUser1;

    private RatingBar ratingBarUser1;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    DatabaseReference databaseReference = firebaseDatabase.getReference("/User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSaveRating = (Button) findViewById(R.id.buttonSaveRating);

        textViewUser1 = (TextView) findViewById(R.id.textViewUser1);

        ratingBarUser1 = (RatingBar) findViewById(R.id.ratingBarUser1);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        setTitle("Party Lobby");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser temp = firebaseAuth.getCurrentUser();
                User user = dataSnapshot.child(temp.getUid()).getValue(User.class);
                textViewUser1.setText(user.DotaName);
                ratingBarUser1.setRating(user.rating);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        buttonStart.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonSaveRating.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonStart) {
            progressDialog.setMessage("Game in progress...");
            progressDialog.show();
        }

        if(view == buttonSearch) {
            Toast.makeText(this, "Searching for members...", Toast.LENGTH_LONG).show();
        }

        if(view == buttonSaveRating) {
            Toast.makeText(this, "Saving ratings...", Toast.LENGTH_SHORT).show();
        }
}

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
