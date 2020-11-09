package com.example.SudoKey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {
    String status, username, password, phone, email;
    private FirebaseAuth mAuth;
    DataSnapshot data;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = dataSnapshot;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}});
    }

    public void alert(String alert) {
        Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
    }



    public void check(View v) {
        status = "ok";
        username = ((TextView) findViewById(R.id.username)).getText().toString().trim();
        email = ((TextView) findViewById(R.id.email)).getText().toString().trim();
        phone = ((TextView) findViewById(R.id.phone)).getText().toString().trim();
        password = ((TextView) findViewById(R.id.password)).getText().toString().trim();
        String passwordc = ((TextView) findViewById(R.id.passwordConfirm)).getText().toString().trim();
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
        User xuser;
        for (DataSnapshot user : data.getChildren()) {
            xuser = user.getValue(User.class);
            if (xuser != null) {
                if (username.equals(xuser.username)) {
                    status = "";

                    alert("this username is already taken");
                }
            } else {
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

    public void newUser(final User user) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child("user");
                            myRef.child(user.username).setValue(user);
                            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.putString("phone", phone);
                            editor.putString("email", email);
                            editor.apply();
                            Intent i = new Intent(getApplicationContext(), sudokuRoom.class);
                            startActivity(i);
                        } else {
                            System.out.println(task.getException());
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Please enter real email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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