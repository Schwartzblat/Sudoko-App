package com.example.MathApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.EventListener;
import java.util.Objects;

public class SudokuOn extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    String winner;
    Sudoku sudoku;
    int [][] solved, board;
    int remain =-10, lives = 3;
    String username,code, otherUser, pos,remainOther;
    int status =0;
    int [] tableArray = {R.id.id0, R.id.id1, R.id.id2, R.id.id3, R.id.id4, R.id.id5, R.id.id6, R.id.id7, R.id.id8, R.id.id9, R.id.id10, R.id.id11, R.id.id12, R.id.id13, R.id.id14, R.id.id15, R.id.id16, R.id.id17, R.id.id18, R.id.id19, R.id.id20, R.id.id21, R.id.id22, R.id.id23, R.id.id24, R.id.id25, R.id.id26, R.id.id27, R.id.id28, R.id.id29, R.id.id30, R.id.id31, R.id.id32, R.id.id33, R.id.id34, R.id.id35, R.id.id36, R.id.id37, R.id.id38, R.id.id39, R.id.id41, R.id.id42, R.id.id43, R.id.id44, R.id.id45, R.id.id46, R.id.id47, R.id.id48, R.id.id49, R.id.id50, R.id.id51, R.id.id52, R.id.id53, R.id.id54, R.id.id55, R.id.id56, R.id.id57, R.id.id58, R.id.id59, R.id.id60, R.id.id61, R.id.id62, R.id.id63, R.id.id64, R.id.id65, R.id.id66, R.id.id67, R.id.id68, R.id.id69, R.id.id70, R.id.id71, R.id.id72, R.id.id73, R.id.id74, R.id.id75, R.id.id76, R.id.id77, R.id.id78, R.id.id79, R.id.id80, R.id.id81};
    int [] textTable = {R.id.place1, R.id.place2, R.id.place3, R.id.place4, R.id.place5, R.id.place6, R.id.place7, R.id.place8, R.id.place9, R.id.place10, R.id.place11, R.id.place12, R.id.place13, R.id.place14, R.id.place15, R.id.place16, R.id.place17, R.id.place18, R.id.place19, R.id.place20, R.id.place21, R.id.place22, R.id.place23, R.id.place24, R.id.place25, R.id.place26, R.id.place27, R.id.place28, R.id.place29, R.id.place30, R.id.place31, R.id.place32, R.id.place33, R.id.place34, R.id.place35, R.id.place36, R.id.place37, R.id.place38, R.id.place39, R.id.place40, R.id.place41, R.id.place42, R.id.place43, R.id.place44, R.id.place45, R.id.place46, R.id.place47, R.id.place48, R.id.place49, R.id.place50, R.id.place51, R.id.place52, R.id.place53, R.id.place54, R.id.place55, R.id.place56, R.id.place57, R.id.place58, R.id.place59, R.id.place60, R.id.place61, R.id.place62, R.id.place63, R.id.place64, R.id.place65, R.id.place66, R.id.place67, R.id.place68, R.id.place69, R.id.place70, R.id.place71, R.id.place72, R.id.place73, R.id.place74, R.id.place75, R.id.place76, R.id.place77, R.id.place78, R.id.place79, R.id.place80, R.id.place81};
    int [] livesID = {R.id.live3,R.id.live2,R.id.live1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_on);
        Intent i = getIntent();
        pos = i.getStringExtra("first");
        String [] arr = i.getStringArrayExtra("nameAndCode");
        username = arr[0];
        code = arr[1];
        firebase();
        //check
    }

    public void firebase(){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        myRef.child("Rooms").child(code).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (status < 2) {
                    if (status < 1) {
                        try {
                            sudoku = new Sudoku(dataSnapshot.child("board").getValue().toString());
                            if (pos.equals("user1")) {
                                otherUser = dataSnapshot.child("users").child("user2").child("username").getValue().toString();
                            } else if (pos.equals("user2")) {
                                otherUser = dataSnapshot.child("users").child("user1").child("username").getValue().toString();
                            }
                            board = sudoku.getBoard();
                            solved = Sudoku.solve(Sudoku.boardNumsToBoard(sudoku.boardNums));
                            setBoard();
                            status++;
                            update();
                        } catch (Exception ignored) {
                        }
                    }
                    try {
                        if (pos.equals("user1")) {
                            remainOther = dataSnapshot.child("users").child("user2").child("remain").getValue().toString();
                        } else {
                            remainOther = dataSnapshot.child("users").child("user1").child("remain").getValue().toString();

                        }
                    } catch (Exception ignored) {
                    }
                    update();
                    try {
                        winner = Objects.requireNonNull(dataSnapshot.child("winner").getValue()).toString();
                        if (winner.equals(otherUser)) {
                            gameOver(false);
                        } else if (winner.equals(username)) {
                            gameOver(true);
                        }
                    } catch (Exception ignored){}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setBoard(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j] == 0){
                    findViewById(tableArray[i*9+j]).setVisibility(View.INVISIBLE);
                    findViewById(textTable[i*9+j]).setVisibility(View.VISIBLE);
                    remain++;
                }
                else{
                    ((TextView)findViewById(tableArray[i*9+j])).setText(String.valueOf(board[i][j]));
                    findViewById(textTable[i*9+j]).setVisibility(View.INVISIBLE);
                    findViewById(tableArray[i*9+j]).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void check(View v){
        //getting input
        String input = "";
        for(int i=0;i<textTable.length;i++){
            input = ((TextView)findViewById(textTable[i])).getText().toString();
            if(!(input.equals(""))){
                if(!(input.equals(String.valueOf(solved[i/9][i%9])))){
                    lives--;
                    findViewById(livesID[lives]).setVisibility(View.INVISIBLE);
                    ((TextView)findViewById(textTable[i])).setText("");
                }
                else {
                    ((TextView) findViewById(tableArray[i])).setText(input);
                    findViewById(tableArray[i]).setVisibility(View.VISIBLE);
                    findViewById(textTable[i]).setVisibility(View.INVISIBLE);
                    ((TextView)findViewById(textTable[i])).setText("");
                    remain--;
                }
            }
        }
        update();
    }

    public void update() {
        try {
            if (remain != 0 && remainOther != null) {
                ((TextView) findViewById(R.id.other)).setText(remainOther);
                ((TextView) findViewById(R.id.you)).setText(String.valueOf(remain));
            }
        } catch (Exception ignored) {
        }
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("users").child(pos).child("remain").setValue(remain);
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("users").child(pos).child("lives").setValue(lives);
        if (remain != -10 && remainOther != null) {
            if (remain == 0) {
                FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("winner").setValue(username);
                gameOver(true);
            } else if (lives <= 0) {
                FirebaseDatabase.getInstance().getReference("Rooms").child(code).child("winner").setValue(otherUser);
                gameOver(false);
            }
        }

    }

    public void gameOver(boolean bool){
        status = 10;
        //delete room:
        FirebaseDatabase.getInstance().getReference("Rooms").child(code).removeValue();
        //close screen
        Intent i;
        if(bool){
            Toast.makeText(this,"You are the winner! good job!", Toast.LENGTH_LONG).show();
            i = new Intent(this, MainActivity.class);
        }
        else{
            Toast.makeText(this,"you lost, maybe next time...", Toast.LENGTH_LONG).show();
            i = new Intent(this, MainActivity.class);
        }
        i.putExtra("username", username);
        startActivity(i);
        finish();
    }





}