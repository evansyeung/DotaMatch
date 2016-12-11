package com.dotamatch.designproject.dotamatch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {

    private EditText editTextMessageInput;

    private Button buttonSend;

    private TextView textViewConversation;

    private String room_name;
    //private String user_name;
    private String temp_key;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        editTextMessageInput = (EditText) findViewById(R.id.editTextMessageInput);

        buttonSend = (Button) findViewById(R.id.buttonSend);

        textViewConversation = (TextView) findViewById(R.id.textViewConversation);

        room_name = getIntent().getExtras().get("room_name").toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child(room_name);

        setTitle(room_name);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String,Object> map = new HashMap<String,Object>();
                temp_key = databaseReference.push().getKey();
                databaseReference.updateChildren(map);

                DatabaseReference message_root = databaseReference.child(temp_key);

                Map<String,Object> map2 = new HashMap<String,Object>();
                map2.put("msg",editTextMessageInput.getText().toString());

                message_root.updateChildren(map2);
            }
        });

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String chat_msg;
    //private String chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapShot) {

        Iterator i = dataSnapShot.getChildren().iterator();

        while(i.hasNext()) {

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();

            textViewConversation.append(chat_msg+" \n");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ChatRoomActivity.class));
    }
}
