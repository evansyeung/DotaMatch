package com.dotamatch.designproject.dotamatch;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
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
import java.util.concurrent.TimeUnit;

public class PartyActivity extends AppCompatActivity implements View.OnClickListener {

    private Party party = new Party();

    private Button buttonStart;
    private Button buttonSearch;
    private Button buttonSubmit;

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

    private TextView textViewTimer;

    private RatingBar ratingBarUser1;
    private RatingBar ratingBarUser2;
    private RatingBar ratingBarUser3;
    private RatingBar ratingBarUser4;
    private RatingBar ratingBarUser5;

    //RNG used to decide role priority
    private Random rand = new Random();

    private boolean inGame = false;
    private boolean gameEnd = false;
    private boolean user2Rated;
    private boolean user3Rated;
    private boolean user4Rated;
    private boolean user5Rated;

    //Counter for numberPickerPlayer max value
    private int remaining = 4;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private FirebaseUser fbUser = firebaseAuth.getCurrentUser();

    private DatabaseReference databaseReference = firebaseDatabase.getReference("/User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);

        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

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

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);

        ratingBarUser1 = (RatingBar) findViewById(R.id.ratingBarUser1);
        ratingBarUser2 = (RatingBar) findViewById(R.id.ratingBarUser2);
        ratingBarUser3 = (RatingBar) findViewById(R.id.ratingBarUser3);
        ratingBarUser4 = (RatingBar) findViewById(R.id.ratingBarUser4);
        ratingBarUser5 = (RatingBar) findViewById(R.id.ratingBarUser5);

        progressDialog = new ProgressDialog(this);

        setTitle("Party Lobby");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child(fbUser.getUid()).getValue(User.class);
                party.leader = user;
                textViewUser1.setText(party.leader.DotaName);
                textViewUser1Role.setText(party.leader.role);
                ratingBarUser1.setRating(party.leader.getRatingAverage());
                party.leader_Key = dataSnapshot.child(fbUser.getUid()).getKey();
                setPartyRole(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        buttonStart.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }

    //Sets party's role based on user's role
    void setPartyRole(User user) {

        switch(user.role) {
            case "Solo":
                party.solo = true;
                party.numberOfMembers += 1;
                break;
            case "Carry":
                party.carry = true;
                party.numberOfMembers += 1;
                break;
            case "Offlaner":
                party.offlaner = true;
                party.numberOfMembers += 1;
                break;
            case "Jungler":
                party.jungler = true;
                party.numberOfMembers += 1;
                break;
            case "Support":
                party.support = true;
                party.numberOfMembers += 1;
                break;
            case "Fill":

                if (!party.solo) {
                    party.solo = true;
                    party.numberOfMembers += 1;
                    break;
                }
                else if (!party.carry) {
                    party.carry = true;
                    party.numberOfMembers += 1;
                    break;
                }
                else if (!party.offlaner) {
                    party.offlaner = true;
                    party.numberOfMembers += 1;
                    break;
                }
                else if (!party.jungler) {
                    party.jungler = true;
                    party.numberOfMembers += 1;
                    break;
                }
                else if (!party.support) {
                    party.support = true;
                    party.numberOfMembers += 1;
                    break;
                }
        }
    }

    String checkOpenRoles() {
        //Randomize role priority, does not cover all possible combinations
        //Combinations were randomly chosen
        int n = rand.nextInt(5) + 1;

        switch(n) {
            case 1:
                if (!party.carry)
                    return "Carry";
                else if (!party.solo)
                    return "Solo";
                else if (!party.offlaner)
                    return "Offlaner";
                else if (!party.jungler)
                    return "Jungler";
                else if(!party.support)
                    return "Support";
                else
                    break;
            case 2:
                if (!party.jungler)
                    return "Jungler";
                else if (!party.offlaner)
                    return "Offlaner";
                else if (!party.solo)
                    return "Solo";
                else if (!party.support)
                    return "Support";
                else if(!party.carry)
                    return "Carry";
                else
                    break;
            case 3:
                if (!party.support)
                    return "Support";
                else if (!party.offlaner)
                    return "Offlaner";
                else if (!party.carry)
                    return "Carry";
                else if (!party.jungler)
                    return "Jungler";
                else if(!party.solo)
                    return "Solo";
                else
                    break;
            case 4:
                if (!party.offlaner)
                    return "Offlaner";
                else if (!party.solo)
                    return "Solo";
                else if (!party.carry)
                    return "Carry";
                else if (!party.support)
                    return "Support";
                else if(!party.jungler)
                    return "Jungler";
                else
                    break;
            case 5:
                if (!party.solo)
                    return "Solo";
                else if (!party.support)
                    return "Support";
                else if (!party.jungler)
                    return "Jungler";
                else if (!party.carry)
                    return "Carry";
                else if(!party.offlaner)
                    return "Offlaner";
                else
                    break;
        }
        return "";
    }

    //Searches for members based on roles available
    void searchForMembers(Iterator<DataSnapshot> iter, String role) {
        User userTemp;
        while(iter.hasNext()) {
            DataSnapshot child = iter.next();

            if(party.user2 == null) {    //Search for 2nd member
                if(!child.getKey().equals(party.leader_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.getRatingAverage() >= numberPickerRating.getValue()) {
                        party.user2 = userTemp;
                        party.user2_Key = child.getKey();
                        setPartyRole(userTemp);

                        textViewUser2.setText(party.user2.DotaName);
                        textViewUser2Role.setText(party.user2.role);
                        ratingBarUser2.setRating(party.user2.getRatingAverage());
                        return;
                    }
                }
            }else if(party.user3 == null) {  //Search for 3rd member
                if(!child.getKey().equals(party.leader_Key) && !child.getKey().equals(party.user2_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.getRatingAverage() >= numberPickerRating.getValue()) {;
                        party.user3 = userTemp;
                        party.user3_Key = child.getKey();
                        setPartyRole(userTemp);

                        textViewUser3.setText(party.user3.DotaName);
                        textViewUser3Role.setText(party.user3.role);
                        ratingBarUser3.setRating(party.user3.getRatingAverage());
                        return;
                    }
                }
            }else if(party.user4 == null) {  //Search for 4th member
                if(!child.getKey().equals(party.leader_Key) && !child.getKey().equals(party.user2_Key) && !child.getKey().equals(party.user3_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.getRatingAverage() >= numberPickerRating.getValue()) {
                        party.user4 = userTemp;
                        party.user4_Key = child.getKey();
                        setPartyRole(userTemp);

                        textViewUser4.setText(party.user4.DotaName);
                        textViewUser4Role.setText(party.user4.role);
                        ratingBarUser4.setRating(party.user4.getRatingAverage());
                        return;
                    }
                }
            }else if(party.user5 == null) {  //Search for 5th member
                if(!child.getKey().equals(party.leader_Key) && !child.getKey().equals(party.user2_Key) && !child.getKey().equals(party.user3_Key) && !child.getKey().equals(party.user4_Key)) {
                    userTemp = child.getValue(User.class);
                    if ((userTemp.role.equals(role) || userTemp.role.equals("Fill")) && userTemp.getRatingAverage() >= numberPickerRating.getValue()) {
                        party.user5 = userTemp;
                        party.user5_Key = child.getKey();
                        setPartyRole(userTemp);

                        textViewUser5.setText(party.user5.DotaName);
                        textViewUser5Role.setText(party.user5.role);
                        ratingBarUser5.setRating(party.user5.getRatingAverage());
                        return;
                    }
                }
            }
        }
    }

    void updateRating() {
        if(party.user2 != null && !user2Rated) {
            party.user2.ratings.add(ratingBarUser2.getRating());
            databaseReference.child(party.user2_Key).child("ratings").setValue(party.user2.ratings);
            databaseReference.child(party.user2_Key).child("ratingAverage").setValue(party.user2.getRatingAverage());
            user2Rated = true;
            ratingBarUser2.setIsIndicator(true);
        }
        if(party.user3 != null && !user3Rated) {
            party.user3.ratings.add(ratingBarUser3.getRating());
            databaseReference.child(party.user3_Key).child("ratings").setValue(party.user3.ratings);
            databaseReference.child(party.user3_Key).child("ratingAverage").setValue(party.user3.getRatingAverage());
            user3Rated = true;
            ratingBarUser3.setIsIndicator(true);
        }
        if(party.user4 != null && !user4Rated) {
            party.user4.ratings.add(ratingBarUser4.getRating());
            databaseReference.child(party.user4_Key).child("ratings").setValue(party.user4.ratings);
            databaseReference.child(party.user4_Key).child("ratingAverage").setValue(party.user4.getRatingAverage());
            user4Rated = true;
            ratingBarUser4.setIsIndicator(true);
        }
        if(party.user5 != null && !user5Rated) {
            party.user5.ratings.add(ratingBarUser5.getRating());
            databaseReference.child(party.user5_Key).child("ratings").setValue(party.user5.ratings);
            databaseReference.child(party.user5_Key).child("ratingAverage").setValue(party.user5.getRatingAverage());
            user5Rated = true;
            ratingBarUser5.setIsIndicator(true);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == buttonStart) {
            System.out.println("Party #: " + party.numberOfMembers);
            inGame = true;
            progressDialog.setMessage("Game in progress");
            progressDialog.show();

            new CountDownTimer(10000, 1000) {   //60000 milliseconds = 1min, set to 1min for testing purposes

                public void onTick(long millisUntilFinished) {
                    textViewTimer.setText(""+String.format("%d : %d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }

                public void onFinish() {
                    inGame = false;
                    gameEnd = true;
                    progressDialog.dismiss();

                    if(party.numberOfMembers != 1)
                        textViewTimer.setText("Rate members!");
                    else
                        textViewTimer.setText("Game End");

                    user2Rated = false;
                    user3Rated = false;
                    user4Rated = false;
                    user5Rated = false;

                    //Allow users to rate each other after game has ended
                    ratingBarUser2.setIsIndicator(false);
                    ratingBarUser3.setIsIndicator(false);
                    ratingBarUser4.setIsIndicator(false);
                    ratingBarUser5.setIsIndicator(false);
                }
            }.start();
        }

        if(view == buttonSearch) {
            if(party.numberOfMembers < 5 && !inGame) {
                Toast.makeText(this, "Searching for members", Toast.LENGTH_LONG).show();

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    //Used to get the numberPickerPlayer value before maxValue is changed on numberPickerPlayer
                    int numberTemp = numberPickerPlayer.getValue();

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<DataSnapshot> children = new ArrayList<DataSnapshot>();
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            children.add(child);
                        }
                        Iterator<DataSnapshot> iter = children.iterator();

                        for(int i = 0; i < numberTemp; i++) {
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

        if(view == buttonSubmit) {
            if(user2Rated || user3Rated || user4Rated || user5Rated) {
                Toast.makeText(this, "Ratings already submitted", Toast.LENGTH_SHORT).show();
            }else if(!inGame && gameEnd) {
                Toast.makeText(this, "Saving ratings", Toast.LENGTH_SHORT).show();
                updateRating();
            }else if(party.numberOfMembers == 1) {
                Toast.makeText(this, "No party members to rate!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this, "Game has not ended", Toast.LENGTH_SHORT).show();
        }
}

    @Override
    public void onBackPressed() {
        if (party.numberOfMembers > 1 || inGame) {
            //Displays alert dialog box to confirm the user wants to leave the party
            AlertDialog.Builder leaveAlert = new AlertDialog.Builder(this);
            leaveAlert.setMessage("Are you sure you want to leave?")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                            startActivity(new Intent(PartyActivity.this, ProfileActivity.class));
                        }
                    })
                    .setNegativeButton("Stay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Do nothing stay in party activity
                            dialog.dismiss();
                        }
                    })
                    .create();
            leaveAlert.show();
        }
        else {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
}
