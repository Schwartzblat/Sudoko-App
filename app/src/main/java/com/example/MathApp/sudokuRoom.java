package com.example.MathApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.Random;

public class sudokuRoom extends AppCompatActivity{
    String username,code;
    Context context;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_room);
        Intent i = getIntent();
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
        username = data.getString("username", null);
        context = getApplicationContext();
    }

    public void createRoom(View v){
        code = String.valueOf(random(0, 1000000000));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Rooms");
        ref.child(code).child("users").child("user1").child("username").setValue(username);
        Intent i = new Intent(this, waitingRoom.class);
        i.putExtra("code", code);
        startActivity(i);
    }

    public void joinRoom(View v){
        code = ((TextView)findViewById(R.id.code)).getText().toString();
        myRef = FirebaseDatabase.getInstance().getReference("Rooms");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot);

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
        for (DataSnapshot id : dataSnapshot.getChildren()) {
            if(code!=null){
                if(Objects.equals(id.getKey(), code)){
                    correct();
                }
            }
        }
    }

    public void correct(){
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("users").child("user2").child("username").setValue(username);
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("users").child("user2").child("lives").setValue("3");
        Intent i = new Intent(this, SudokuOn.class);
        i.putExtra("code", String.valueOf(code));
        i.putExtra("first", "user2");
        startActivity(i);
        finish();
    }
}