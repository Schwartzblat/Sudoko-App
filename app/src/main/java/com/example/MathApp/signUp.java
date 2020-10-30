package com.example.MathApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    String status, username, check="", password;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    ValueEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    }

    public void alert(String alert) {
        Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
    }



    public void check(View v) {
        check = "";
        status = "ok";
        username = (String) ((TextView) findViewById(R.id.username)).getText().toString();
        String email = (String) ((TextView) findViewById(R.id.email)).getText().toString();
        String phone = (String) ((TextView) findViewById(R.id.phone)).getText().toString();
        password = (String) ((TextView) findViewById(R.id.password)).getText().toString();
        String passwordc = (String) ((TextView) findViewById(R.id.passwordConfirm)).getText().toString();
        int counter = 0;
        //valid username
        if (username.length() < 4) {
            status = "";
            alert("please enter valid username");
        }
        char [] charArray = ".[]#$ ".toCharArray();
        for(int i =0;i<username.length();i++){
            for (char c : charArray) {
                if (username.toCharArray()[i] == c) {
                    status = "";
                    alert("please enter valid username");
                }
            }
        }
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User xuser;
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    xuser = user.getValue(User.class);
                    if (xuser != null) {
                        if (username.equals(xuser.username)) {
                            status = "";

                            alert("this username is already taken");
                        }


                    } else {
                    }
                }
                check = "ok";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});

            //valid password
        if (!password.equals(passwordc)) {
            status = "";
            alert("please enter the same password");
        }
        if (password.length() < 8) {
            status = "";
            alert("please enter a valid password");
        }

        // valid phone
        if (phone.length() != 10) {
            status = "";
            alert("please enter valid phone number");
        }


        // valid email:
        for (int i = 0; i < email.length(); i++) {
            if (email.split("")[i].equals("@")) {
                counter++;
            }
        }
        if (counter == 0) {
            alert("please enter a valid email");
            status = "";
        }


        if (status.equals("ok")&&check.equals("ok")) {
            User user = new User(username, email, phone, password);
            newUser(user);
        }
    }

    public void newUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child("user");
        myRef.child(user.username).setValue(user);
        Intent i = new Intent(this, MainActivity.class);
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
        username = data.getString("username", null);
        password = data.getString("password", null);
        startActivity(i);
        finish();
    }


    public void launchLogIn(View V){
        Intent i = new Intent(this, logIn.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}