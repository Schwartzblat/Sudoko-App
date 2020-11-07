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

public class logIn extends AppCompatActivity {
    DataSnapshot data = null;
    String username ="", password="", email, phone;
    int status = 0;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("user");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = dataSnapshot;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});
    }


    public void log(View v){
        //get values
        username = ((TextView)findViewById(R.id.username)).getText().toString();
        password = ((TextView)findViewById(R.id.password)).getText().toString();
        if(data!=null) {
            User xuser;
            for (DataSnapshot user : data.getChildren()) {
                xuser = user.getValue(User.class);
                if (xuser != null) {
                    if (username.equals(xuser.username) && password.equals(xuser.password)) {
                        phone = xuser.phone;
                        email = xuser.email;
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            StatusIsOk();
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            // ...
                                        }

                                        // ...
                                    }
                                });

                        StatusIsOk();
                        status = 1;
                    }
                }
            }
            if (status == 0) {
                Toast.makeText(getApplicationContext(), "invalid name or password", Toast.LENGTH_LONG).show();
                ((TextView) findViewById(R.id.password)).setText("");
            }
        }
    }




    public void StatusIsOk(){
        Intent i = new Intent(this, MainActivity.class);
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE ).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.apply();
        startActivity(i);
        finish();

    }

    public void launchSignUp(View v){
        Intent i = new Intent(this, signUp.class);
        startActivity(i);
        finish();
    }
}