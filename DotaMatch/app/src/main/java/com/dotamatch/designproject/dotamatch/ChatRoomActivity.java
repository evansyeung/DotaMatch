package com.dotamatch.designproject.dotamatch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ChatRoomActivity extends AppCompatActivity {

    private Button buttonAddRoom;

    private EditText editTextRoomName;

    private ListView listViewRoomList;

    private ArrayAdapter<String> arrayAdapter;

    private ArrayList<String> listOfRooms = new ArrayList<>();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chat Rooms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        buttonAddRoom = (Button) findViewById(R.id.buttonAddRoom);
        editTextRoomName = (EditText) findViewById(R.id.editTextRoomName);
        listViewRoomList = (ListView) findViewById(R.id.listViewRoomList);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfRooms);

        listViewRoomList.setAdapter(arrayAdapter);

        //Disable automatic popup keyboard on activity start
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View view = this.getCurrentFocus();

        buttonAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String, Object>();
                map.put(editTextRoomName.getText().toString(),"");
                databaseReference.updateChildren(map);

                //Hides keyboard after clicking ADD ROOM button
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while(i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                listOfRooms.clear();
                listOfRooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listViewRoomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),MessageActivity.class);
                intent.putExtra("room_name",((TextView)view).getText().toString() );
                //intent.putExtra("user_name",name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ProfileActivity.class));
    }
}
