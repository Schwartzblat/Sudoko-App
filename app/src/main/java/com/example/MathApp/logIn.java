package com.example.MathApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class logIn extends AppCompatActivity {

    String username ="", password="";
    int status = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }


    public void log(View v){
        //get values
        username = ((TextView)findViewById(R.id.username)).getText().toString();
        password = ((TextView)findViewById(R.id.password)).getText().toString();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("user");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User xuser;
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    xuser = user.getValue(User.class);
                    if (xuser != null) {
                        if (username.equals(xuser.username)&&password.equals(xuser.password)) {
                            StatusIsOk();
                            status = 1;
                        }


                    } else {
                    }
                }
                Toast.makeText(getApplicationContext(), "invalid name or password", Toast.LENGTH_LONG).show();
                ((TextView) findViewById(R.id.password)).setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }});
    }




    public void StatusIsOk(){
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);
        finish();

    }

    public void launchSignUp(View v){
        Intent i = new Intent(this, signUp.class);
        startActivity(i);
        finish();
    }
}