package fr.supinternet.slike;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private Button signUpBtn;
    private EditText nameText, emailText, passwordText, emailLog, passLog;
    private RelativeLayout loaderWrapper;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private Button signInBtn;
    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUpBtn = (Button)findViewById(R.id.signUpBtn);
        signInBtn = (Button)findViewById(R.id.signInBtn);
        nameText = (EditText)findViewById(R.id.name);
        emailText = (EditText)findViewById(R.id.email);
        passwordText = (EditText)findViewById(R.id.password);
        emailLog = (EditText)findViewById(R.id.mail);
        passLog = (EditText)findViewById(R.id.pass);
        loaderWrapper = (RelativeLayout)findViewById(R.id.loaderWrapper);
        auth = FirebaseAuth.getInstance();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameText.length() < 1){
                    Toast.makeText(MainActivity.this, "Enter a valid name", Toast.LENGTH_SHORT).show();
                }else if (emailText.length() < 6){
                    Toast.makeText(MainActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else if (passwordText.length() < 6){
                    Toast.makeText(MainActivity.this, "Enter a valid Password", Toast.LENGTH_SHORT).show();
                }else{
                    loaderWrapper.setVisibility(View.VISIBLE);
                    signUpBtn.setClickable(false);
                    nameText.setFocusable(false);
                    emailText.setFocusable(false);
                    passwordText.setFocusable(false);

                    auth.createUserWithEmailAndPassword(emailText.getText().toString(), passwordText.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = auth.getCurrentUser();
                                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        User newUser = new User(nameText.getText().toString(), emailText.getText().toString());
                                        mDatabase = FirebaseDatabase.getInstance().getReference();
                                        mDatabase.child("users").child(user.getUid()).setValue(newUser);
                                        Intent myIntent = new Intent(MainActivity.this, FeedActivity.class);
                                        startActivity(myIntent);
                                        loaderWrapper.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Authentification failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailLog.length() < 6){
                    Toast.makeText(MainActivity.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
                }
                else if (passLog.length() < 6){
                    Toast.makeText(MainActivity.this, "Enter a valid Password", Toast.LENGTH_SHORT).show();
                }else{
                    loaderWrapper.setVisibility(View.VISIBLE);
                    signUpBtn.setClickable(false);
                    nameText.setFocusable(false);
                    emailText.setFocusable(false);
                    emailLog.setFocusable(false);
                    passLog.setFocusable(false);
                    passwordText.setFocusable(false);

                    auth.signInWithEmailAndPassword(emailLog.getText().toString(), passLog.getText().toString())
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = auth.getCurrentUser();
                                        Intent myIntent = new Intent(MainActivity.this, FeedActivity.class);
                                        startActivity(myIntent);
                                        loaderWrapper.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(MainActivity.this, "Authentification failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }
            }
        });
    }
}
