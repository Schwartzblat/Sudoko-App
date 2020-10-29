package com.example.MathApp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class sudokuRoom extends AppCompatActivity{
    String username, status = "no",code;
    Context context;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_room);
        Intent i = getIntent();
        username = i.getStringExtra("username");
        context = getApplicationContext();
    }

    public void createRoom(View v){
        int code = random(0, 1000000000);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Rooms");
        ref.child(String.valueOf(code)).child("users").child("user1").child("username").setValue(username);
        Intent i = new Intent(this, waitingRoom.class);
        String [] arr = {username, String.valueOf(code)};
        i.putExtra("nameAndCode", arr);
        startActivity(i);
    }

    public void joinRoom(View v){
        code = ((TextView)findViewById(R.id.code)).getText().toString();
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(status.equals("no")) {
                    showData(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    int random(int min, int max){
        Random rand = new Random();
        int num = rand.nextInt();
        if(num<0){
            num = -num;
        }
        return num%(max-min)+min;
    }

    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot id : dataSnapshot.child("Rooms").getChildren()) {
            if(code!=null){
                if(Objects.equals(id.getKey(), code)){
                    correct();
                }
            }
        }
    }

    public void correct(){
        status = "yes";
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("users").child("user2").child("username").setValue(username);
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("users").child("user2").child("lives").setValue("3");

        Intent i = new Intent(this, SudokuOn.class);
        String [] arr = {username,code};
        i.putExtra("nameAndCode", arr);
        i.putExtra("first", "user2");
        startActivity(i);
        finish();
    }
}