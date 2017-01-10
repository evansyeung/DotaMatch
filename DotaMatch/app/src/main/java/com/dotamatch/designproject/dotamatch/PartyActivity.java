package com.dotamatch.designproject.dotamatch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
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

import java.util.ArrayList;
import java.util.Iterator;

public class PartyActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonStart;
    private Button buttonSearch;
    private Button buttonSaveRating;

    private NumberPicker numberPickerRating;

    private TextView textViewUser1;
    private TextView textViewUser2;
    private TextView textViewUser3;
    private TextView textViewUser4;
    private TextView textViewUser5;

    private TextView textViewUser1Role;
    private TextView textViewUser2Role;
    private TextView textViewUser3Role;
    private TextView textViewUser4Role;
    private TextView textViewUser5Role;

    private RatingBar ratingBarUser1;
    private RatingBar ratingBarUser2;
    private RatingBar ratingBarUser3;
    private RatingBar ratingBarUser4;
    private RatingBar ratingBarUser5;

    private String uid1;
    private String uid2;
    private String uid3;
    private String uid4;
    private String uid5;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private FirebaseUser temp = firebaseAuth.getCurrentUser();

    private DatabaseReference databaseReferencePartyLeader = firebaseDatabase.getReference("/User");
    private DatabaseReference databaseReferenceMembers = firebaseDatabase.getReference("/User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSaveRating = (Button) findViewById(R.id.buttonSaveRating);

        numberPickerRating = (NumberPicker) findViewById(R.id.numberPickerRating);
        numberPickerRating.setMaxValue(5);
        numberPickerRating.setMinValue(1);
        numberPickerRating.setWrapSelectorWheel(false);

        textViewUser1 = (TextView) findViewById(R.id.textViewUser1);
        textViewUser2 = (TextView) findViewById(R.id.textViewUser2);
        textViewUser3 = (TextView) findViewById(R.id.textViewUser3);
        textViewUser4 = (TextView) findViewById(R.id.textViewUser4);
        textViewUser5 = (TextView) findViewById(R.id.textViewUser5);

        textViewUser1Role = (TextView) findViewById(R.id.textViewUser1Role);
        textViewUser2Role = (TextView) findViewById(R.id.textViewUser2Role);
        textViewUser3Role = (TextView) findViewById(R.id.textViewUser3Role);
        textViewUser4Role = (TextView) findViewById(R.id.textViewUser4Role);
        textViewUser5Role = (TextView) findViewById(R.id.textViewUser5Role);

        ratingBarUser1 = (RatingBar) findViewById(R.id.ratingBarUser1);
        ratingBarUser2 = (RatingBar) findViewById(R.id.ratingBarUser2);
        ratingBarUser3 = (RatingBar) findViewById(R.id.ratingBarUser3);
        ratingBarUser4 = (RatingBar) findViewById(R.id.ratingBarUser4);
        ratingBarUser5 = (RatingBar) findViewById(R.id.ratingBarUser5);

        progressDialog = new ProgressDialog(this);

        setTitle("Party Lobby");

        databaseReferencePartyLeader.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(temp.getUid()).getValue(User.class);
                textViewUser1.setText(user.DotaName);
                textViewUser1Role.setText(user.role);
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

            databaseReferenceMembers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    //Create & fill arraylist to hold all children
                    ArrayList<DataSnapshot> children = new ArrayList<DataSnapshot>();
                    for(DataSnapshot child : dataSnapshot.getChildren()) {
                        children.add(child);
                    }

                    uid1 = temp.getUid();

                    Iterator<DataSnapshot> i = children.iterator();

                    while (i.hasNext()) {
                        DataSnapshot child = i.next();
                        if (child.getKey() != uid1) {
                            User user = child.getValue(User.class);
                            if (user.rating >= numberPickerRating.getValue()) {
                                textViewUser2.setText(user.DotaName);
                                textViewUser2Role.setText(user.role);
                                ratingBarUser2.setRating(user.rating);
                                uid2 = child.getKey();
                            }
                        }
                    }

                    Iterator<DataSnapshot> j = children.iterator();

                    while (j.hasNext()) {
                        DataSnapshot child = j.next();
                        User user1 = child.getValue(User.class);
                        if (child.getKey() != uid1 && child.getKey() != uid2 && user1.rating >= numberPickerRating.getValue()) {
                            textViewUser3.setText(user1.DotaName);
                            textViewUser3Role.setText(user1.role);
                            ratingBarUser3.setRating(user1.rating);
                            uid3 = child.getKey();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
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
