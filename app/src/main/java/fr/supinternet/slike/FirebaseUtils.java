package fr.supinternet.slike;

import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by trump on 10/10/2017.
 */

public class FirebaseUtils {

    public  static  String getUid(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return currentUser == null ? null : currentUser.getUid();
    }

    private static DatabaseReference getRef(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        return database.getReference();
    }

    public static void sendMessage(final String message){

        String uid = getUid();
        getRef().child("users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    String username = dataSnapshot.getValue().toString();

                    Feed messageToSend = new Feed();

                    messageToSend.setMessage(message);
                    messageToSend.setUser(username);

                    getRef().child("messages").push().setValue(messageToSend);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public static void retrieveUsername(ValueEventListener listener){
        String Uid = getUid();
        getRef().child("users").child(Uid).child("name").addListenerForSingleValueEvent(listener);
    }

    public static void listenToMessages(ChildEventListener listener){
        getRef().child("messages").addChildEventListener(listener);
    }

    public static void listenToTypingPeople(ChildEventListener listener){
        getRef().child("typing").addChildEventListener(listener);
    }

    public  static void setTyping(final boolean isTyping){
        retrieveUsername(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    String username = dataSnapshot.getValue().toString();

                    if(isTyping){
                        getRef().child("typing").child(username).setValue(true);
                        getRef().child("typing").child(username).onDisconnect().removeValue();
                    }else {
                        getRef().child("typing").child(username).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
