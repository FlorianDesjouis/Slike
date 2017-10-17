package fr.supinternet.slike;

import android.content.Intent;
import android.os.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private RecyclerView rvList;
    private Button btnSend, btnMedia;
    private EditText etMessage;
    private TextView tvTypingPeople;

    private FeedAdapter adapter;

    private ArrayList<String> typingPeople = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        setupViews();

        listenToMessages();

        listenToTypingPeople();
    }

    private void setupViews() {
        rvList = (RecyclerView) findViewById(R.id.messageRecyclerView);
        btnSend = (Button) findViewById(R.id.btnSend);
        etMessage = (EditText) findViewById(R.id.etMessage);
        tvTypingPeople = (TextView) findViewById(R.id.tvTypingPeople);
        btnMedia = (Button) findViewById(R.id.btnMedia);

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0 ){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = etMessage.getText().toString();
                if (!message.isEmpty()) {
                    FirebaseUtils.sendMessage(message);
                    etMessage.setText("");
                }
            }
        });

        btnMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Choose picture"), 1);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        rvList.setLayoutManager(manager);
        adapter = new FeedAdapter(this);
        rvList.setAdapter(adapter);
    }

    private void listenToMessages() {
        FirebaseUtils.listenToMessages(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.addMessage(dataSnapshot.getValue(Feed.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.removeMessage(dataSnapshot.getValue(Feed.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void listenToTypingPeople(){
        FirebaseUtils.listenToTypingPeople(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                typingPeople.add(dataSnapshot.getKey());
                updateTypingPeople();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                typingPeople.remove(dataSnapshot.getKey());
                updateTypingPeople();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private  void updateTypingPeople(){
        if (typingPeople.size() > 0){
            tvTypingPeople.setVisibility(View.VISIBLE);
            tvTypingPeople.setText(getTypingPeopleDisplayText());
        }else {
            tvTypingPeople.setVisibility(View.GONE);
        }
    }

    public  String getTypingPeopleDisplayText(){

        if (typingPeople.size() > 3){
            return "Several people are typing";
        }
        else if (typingPeople.size() > 1){
            StringBuffer buf = new StringBuffer();

            for (int i = 0; 1 < typingPeople.size() ; i++){
                buf.append(typingPeople.get(i));
                buf.append(i < typingPeople.size() - 1 ? "," : " ");
            }

            buf.append("are typing...");

            return buf.toString();
        }
        else{
            return typingPeople.get(0) + " is typing...";
        }
    }



}
