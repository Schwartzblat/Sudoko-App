package com.example.MathApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    String username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
        username = data.getString("username", null);
        password = data.getString("password", null);
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
        startActivity(i);
    }

    public void launchUpdate(View view){
        Intent i = new Intent(this, Update.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        createDialog("Are you sure you want to exit?", "yes", "no");
        //moveTaskToBack(true);
    }

    public void createDialog(String message, String positiveString, String negativeString){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to exit?").
                setCancelable(true)
                .setPositiveButton(positiveString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton(negativeString, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);


    }
}