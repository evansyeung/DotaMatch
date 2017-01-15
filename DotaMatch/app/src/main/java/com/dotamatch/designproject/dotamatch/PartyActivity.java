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
import java.util.Random;

public class PartyActivity extends AppCompatActivity implements View.OnClickListener {

    private Party party = new Party();

    private Button buttonStart;
    private Button buttonSearch;
    private Button buttonSaveRating;

    //Use in matchmaking to determine if a player's rating is >= selected rating
    private NumberPicker numberPickerRating;
    //Used to search for desired number of players
    private NumberPicker numberPickerPlayer;

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

    Random rand = new Random();

    //Counter for numberPickerPlayer max
    private int remaining = 4;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private FirebaseUser fbUser = firebaseAuth.getCurrentUser();

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

        numberPickerPlayer = (NumberPicker) findViewById(R.id.numberPickerPlayer);
        numberPickerPlayer.setMaxValue(4);
        numberPickerPlayer.setMinValue(1);
        numberPickerPlayer.setWrapSelectorWheel(false);

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
                User user = dataSnapshot.child(fbUser.getUid()).getValue(User.class);
                party.leader = user;
                textViewUser1.setText(party.leader.DotaName);
                textViewUser1Role.setText(party.leader.role);
                ratingBarUser1.setRating(party.leader.rating);
                party.leader_Key = dataSnapshot.child(fbUser.getUid()).getKey();
                setPartyRole(user);

                //System.out.println("UID: " +  dataSnapshot.child(fbUser.getUid()).getKey());
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

    //Sets party's role based on user's role
    void setPartyRole(User user) {
        if (user.role.equals("Solo")) {
            party.solo = true;
            party.numberOfMembers += 1;
        } else if (user.role.equals("Carry")) {
            party.carry = true;
            party.numberOfMembers += 1;
        } else if (user.role.equals("Offlaner")) {
            party.offlaner = true;
            party.numberOfMembers += 1;
        } else if (user.role.equals("Jungler")) {
            party.jungler = true;
            party.numberOfMembers += 1;
        } else if (user.role.equals("Support")) {
            party.support = true;
            party.numberOfMembers += 1;
        } else if (user.role.equals("Fill")) {
            if (!party.solo)
                party.solo = true;
            else if (!party.carry)
                party.carry = true;
            else if (!party.offlaner)
                party.offlaner = true;
            else if (!party.jungler)
                party.jungler = true;
            else if (!party.support)
                party.support = true;

            party.numberOfMembers += 1;
        }
    }

    String checkOpenRoles() {
        //Randomize role priority, does not cover all possible combinations
        //Combinations were randomly chosen
        int n = rand.nextInt(5) + 1;

        if (n == 1) {
            if (!party.carry)
                return "Carry";
            else if (!party.solo)
                return "Solo";
            else if (!party.offlaner)
                return "Offlaner";
            else if (!party.jungler)
                return "Jungler";
            else
                return "Support";
        } else if (n == 2) {
            if (!party.jungler)
                return "Jungler";
            else if (!party.offlaner)
                return "Offlaner";
            else if (!party.solo)
                return "Solo";
            else if (!party.support)
                return "Support";
            else
                return "Carry";
        } else if (n == 3) {
            if (!party.support)
                return "Support";
            else if (!party.offlaner)
                return "Offlaner";
            else if (!party.carry)
                return "Carry";
            else if (!party.jungler)
                return "Jungler";
            else
                return "Solo";
        } else if (n == 4) {
            if (!party.offlaner)
                return "Offlaner";
            else if (!party.solo)
                return "Solo";
            else if (!party.carry)
                return "Carry";
            else if (!party.support)
                return "Support";
            else
                return "Jungler";
        } else if (n == 5) {
            if (!party.solo)
                return "Solo";
            else if (!party.support)
                return "Support";
            else if (!party.offlaner)
                return "Offlaner";
            else if (!party.carry)
                return "Carry";
            else
                return "Offlaner";
        }
        return "";
    }

    //Searches for members based on roles available
    void searchForMembers(Iterator<DataSnapshot> iter, String role) {
        User userTemp;
        while(iter.hasNext()) {
            DataSnapshot child = iter.next();

            if(party.numberOfMembers == 1) {    //Search for 2nd member
                if(!child.getKey().equals(party.leader_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.rating >= numberPickerRating.getValue()) {
                         party.user2 = userTemp;
                        textViewUser2.setText(party.user2.DotaName);
                        textViewUser2Role.setText(party.user2.role);
                        ratingBarUser2.setRating(party.user2.rating);
                        party.user2_Key = child.getKey();
                        setPartyRole(userTemp);
                        return;
                    }
                }
            }else if(party.numberOfMembers == 2) {  //Search for 3rd member
                if(!child.getKey().equals(party.leader_Key) && !child.getKey().equals(party.user2_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.rating >= numberPickerRating.getValue()) {;
                        party.user3 = userTemp;
                        textViewUser3.setText(party.user3.DotaName);
                        textViewUser3Role.setText(party.user3.role);
                        ratingBarUser3.setRating(party.user3.rating);
                        party.user3_Key = child.getKey();
                        setPartyRole(userTemp);
                        return;
                    }
                }
            }else if(party.numberOfMembers == 3) {  //Search for 4th member
                if(!child.getKey().equals(party.leader_Key) && !child.getKey().equals(party.user2_Key) && !child.getKey().equals(party.user3_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.rating >= numberPickerRating.getValue()) {
                        party.user4 = userTemp;
                        textViewUser4.setText(party.user4.DotaName);
                        textViewUser4Role.setText(party.user4.role);
                        ratingBarUser4.setRating(party.user4.rating);
                        party.user4_Key = child.getKey();
                        setPartyRole(userTemp);
                        return;
                    }
                }
            }else if(party.numberOfMembers == 4) {  //Search for 5th member
                if(!child.getKey().equals(party.leader_Key) && !child.getKey().equals(party.user2_Key) && !child.getKey().equals(party.user3_Key) && !child.getKey().equals(party.user4_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.rating >= numberPickerRating.getValue()) {
                        party.user5 = userTemp;
                        textViewUser5.setText(party.user5.DotaName);
                        textViewUser5Role.setText(party.user5.role);
                        ratingBarUser5.setRating(party.user5.rating);
                        party.user5_Key = child.getKey();
                        setPartyRole(userTemp);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view == buttonStart) {
            progressDialog.setMessage("Game in progress...");
            progressDialog.show();
        }

        if(view == buttonSearch) {
            if(party.numberOfMembers < 5 ) {
                Toast.makeText(this, "Searching for members...", Toast.LENGTH_LONG).show();

                databaseReferenceMembers.addListenerForSingleValueEvent(new ValueEventListener() {
                    //Used to get the numberPickerPlayer value before maxValue is changed on numberPickerPlayer
                    int numberTemp = numberPickerPlayer.getValue();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<DataSnapshot> children = new ArrayList<DataSnapshot>();
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            children.add(child);
                        }

                        Iterator<DataSnapshot> iter = children.iterator();
                        System.out.println("#: " + numberPickerPlayer.getValue());
                        for(int j = 0; j < numberTemp; j++) {
                            searchForMembers(iter, checkOpenRoles());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            else if(party.numberOfMembers >= 5)
                Toast.makeText(this, "Party is full", Toast.LENGTH_LONG).show();

            //Updates numberPickerPlayer to display remaining party slots left
            remaining -= numberPickerPlayer.getValue();
            numberPickerPlayer.setMaxValue(remaining);
            numberPickerPlayer.setMinValue(1);
        }

        if(view == buttonSaveRating) {
            Toast.makeText(this, "Saving ratings...", Toast.LENGTH_SHORT).show();

/*
            System.out.println("# of members: " + party.numberOfMembers);
            System.out.println("Carry: " + party.carry);
            System.out.println("Solo: " + party.solo);
            System.out.println("Offlaner: " + party.offlaner);
            System.out.println("Jungler: " + party.jungler);
            System.out.println("Support: " + party.support);
            */

        }
}

    @Override
    public void onBackPressed() {
        //Make an alert dialog box
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
