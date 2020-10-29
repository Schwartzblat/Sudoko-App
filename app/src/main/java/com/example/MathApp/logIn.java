package com.example.MathApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class logIn extends AppCompatActivity {
    private String [] users = new String[100];
    private String [] passwords = new String[100];
    String username ="", password="", status="no";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }


    public void log(View v){
        //get values
        username = ((TextView)findViewById(R.id.username)).getText().toString();
        password = ((TextView)findViewById(R.id.password)).getText().toString();
        updateUsers();
    }

    public void updateUsers(){
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference();
        new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            }
        };
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println("calling show data");
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        User xuser;
        for (DataSnapshot user1 : dataSnapshot.child("user").getChildren()) {
            xuser = user1.getValue(User.class);
            if(xuser != null) {
                if(username.equals(xuser.username) && password.equals(xuser.password)) {
                    status = "ok";
                    StatusIsOk();
                }

            }
            else{
                System.out.println("error");
            }
        }
        if(!status.equals("ok")) {
            Toast.makeText(this, "invalid name or password", Toast.LENGTH_LONG).show();
            ((TextView) findViewById(R.id.password)).setText("");
        }
    }

    public void StatusIsOk(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);

    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void launchSignUp(View v){
        Intent i = new Intent(this, signUp.class);
        startActivity(i);
        finish();
    }
}