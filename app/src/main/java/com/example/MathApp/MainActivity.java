package com.example.MathApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        username = i.getStringExtra("username");
        password = i.getStringExtra("password");
    }

    public void launchSudokuSolver(View view){
        Intent i = new Intent(this, sudokuSolver.class);
        startActivity(i);
    }
    public void launchTriangle(View view){
        Intent i = new Intent(this, Triangle.class);
        startActivity(i);
    }

    public void launchTicTacToePC(View v){
        Intent i = new Intent(this, TicTacToePC.class);
        startActivity(i);
    }

    public void launchSudoku(View view){
        Intent i = new Intent(this, sudokuOff.class);
        startActivity(i);
    }

    public void launchRoom(View view){
        Intent i = new Intent(this, sudokuRoom.class);
        i.putExtra("username", username);
        startActivity(i);
    }

    public void launchUpdate(View view){
        Intent i = new Intent(this, Update.class);
        i.putExtra("username", username);
        i.putExtra("password", password);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}