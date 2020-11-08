package com.example.SudoKey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
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
    int status = 0;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_room);
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
        username = data.getString("username", null);
        context = getApplicationContext();
        setupNavi();
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

    public void openNav(View v){
        ((DrawerLayout)findViewById(R.id.frame)).openDrawer(GravityCompat.START);
    }

    public void closeNav(View v){
        findViewById(R.id.frame).setTranslationZ(-10);
        ((DrawerLayout)findViewById(R.id.frame)).closeDrawer(GravityCompat.START);
    }

    public void setupNavi(){
        findViewById(R.id.closeNav).setVisibility(View.INVISIBLE);
        findViewById(R.id.frame).setTranslationZ(-10);
        ((DrawerLayout)findViewById(R.id.frame)).addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset){
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                findViewById(R.id.closeNav).setVisibility(View.INVISIBLE);
                findViewById(R.id.frame).setTranslationZ(-10);
                status = 1;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(status==0) {
                    findViewById(R.id.closeNav).setVisibility(View.VISIBLE);
                    findViewById(R.id.closeNav).setTranslationZ(10);
                    findViewById(R.id.frame).setTranslationZ(10);
                }
                status=0;
            }
        });

        ((NavigationView)findViewById(R.id.navi)).setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ((String)item.getTitle()){
                    case "Sudoku Solver":
                        launchSudokuSolver();
                        break;

                    case "Sudoku Offline":
                        launchSudoku();
                        break;

                    case "Tic Tac Toe vs PC":
                        launchTicTacToePC();
                        break;

                    case "Update Info":
                        launchUpdate();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });


    }

    public void launchSudokuSolver(){
        Intent i = new Intent(this, sudokuSolver.class);
        startActivity(i);
    }

    public void launchTriangle(View v){
        Intent i = new Intent(this, Triangle.class);
        startActivity(i);
    }

    public void launchTicTacToePC(){
        Intent i = new Intent(this, TicTacToePC.class);
        startActivity(i);
    }

    public void launchSudoku(){
        Intent i = new Intent(this, sudokuOff.class);
        startActivity(i);
    }
    
    public void launchUpdate(){
        Intent i = new Intent(this, Update.class);
        startActivity(i);
    }

}