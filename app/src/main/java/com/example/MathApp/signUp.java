package com.example.MathApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    String status;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private String [] users = new String[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        updateUsers();
    }

    public void alert(String alert) {
        Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
    }

    public int getUsers(){
        int counter =0;
        for (String user : users) {
            if (user != null) {
                counter++;
            }
        }
        return counter;
    }

    public void check(View v) {
        String status = "ok";
        String username = (String) ((TextView) findViewById(R.id.username)).getText().toString();
        String email = (String) ((TextView) findViewById(R.id.email)).getText().toString();
        String phone = (String) ((TextView) findViewById(R.id.phone)).getText().toString();
        String password = (String) ((TextView) findViewById(R.id.password)).getText().toString();
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
        System.out.println(getUsers());
       for(int i=0;i<getUsers();i++){
           if(username.equals(users[i])){
               status = "";
               alert("this username is already taken");
           }
       }
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


        if (status.equals("ok")) {
            User user = new User(username, email, phone, password);
            newUser(user);
        }
    }

    public void newUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child("user");
        myRef.child(user.username).setValue(user);
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", user.username);
        startActivity(i);
        finish();
    }

    public void updateUsers(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

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
        int counter = 0;
        for (DataSnapshot user : dataSnapshot.child("user").getChildren()) {
            User xuser = user.getValue(User.class);
            if(xuser != null) {
                users[counter] = xuser.getUsername();
            }
            else{
                System.out.println("error");
            }
            counter++;
        }
    }

    public void launchLogIn(View V){
        Intent i = new Intent(this, logIn.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}