package com.example.MathApp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.Objects;

public class waitingRoom extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    Context context;

    String code, username,status = "no", diff = "easy";
    int close = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                String [] difficultyArr = {"easy", "normal", "medium", "hard"};
                diff = difficultyArr[pos];
                if(diff.equals("easy") || diff.equals("normal") || diff.equals("medium") || diff.equals("hard")) {
                    Toast.makeText(getApplicationContext(), "Difficulty changed to "+diff, Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "please choose between easy, normal, medium or hard", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }});

            update();
        Intent i = getIntent();
        String [] arr = i.getStringArrayExtra("nameAndCode");
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
        username = data.getString("username", null);
        code = i.getStringExtra("code");
        ((TextView)findViewById(R.id.code)).setText(("Room Code: "+code));
        context = getApplicationContext();
    }



    public void update(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Rooms");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(status.equals("no")) {
                    showData(snapshot);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showData(DataSnapshot dataSnapshot) {
        for (DataSnapshot id : dataSnapshot.child(code).child("users").getChildren()) {
            if (Objects.requireNonNull(id.getKey()).contains("user2")) {
                status = "ok";
                Intent i = new Intent(this, SudokuOn.class);
                Sudoku sudoku = new Sudoku(diff, context);
                String [] arr = {username,code};
                i.putExtra("nameAndCode", arr);
                i.putExtra("first", "user1");
                FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("board").setValue(sudoku.boardNums);
                FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("users").child("user1").child("lives").setValue("3");
                close = 1;
                startActivity(i);
                finish();
            }
        }
    }

    public void onBackPressed() {
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).removeValue();
    }

    public void onStop() {
        super.onStop();
        if(close == 0) {
            FirebaseDatabase.getInstance().getReference("Rooms").child(code).removeValue();
        }
    }



}
