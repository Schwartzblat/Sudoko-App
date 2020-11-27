package com.example.SudoKey;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class TicTacToePC extends AppCompatActivity{
    private int num_of_turns = 0;
    private int [] turns = {1,0,0,0,0};
    private String status = "";
    int navi =0;
    int [] place = {0, R.id.pos1, R.id.pos2, R.id.pos3, R.id.pos4, R.id.pos5, R.id.pos6, R.id.pos7, R.id.pos8, R.id.pos9};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_pc);
        display(R.id.pos1);
        setupNavi();
    }

    private int getLast(){
        int counter = 0;
        for (int turn : turns) {
            if (turn != 0) {
                counter++;
            }
        }
        return counter;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void display(int id){
        ImageView place = (ImageView) findViewById(id);
        place.setImageDrawable(getDrawable(R.drawable.x));
        if(place.getVisibility() == View.INVISIBLE) {
            place.setVisibility(View.VISIBLE);
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void displayO(int id){
        ImageView place = (ImageView) findViewById(id);
        place.setImageDrawable(getDrawable(R.drawable.o));
        if(place.getVisibility() == View.INVISIBLE) {
            place.setVisibility(View.VISIBLE);
        }
    }

    public void gameOver(){
        disableAll();
        TextView STATUS = findViewById(R.id.status);
        TextView gameover = findViewById(R.id.gameOver);
        if(status.equals("1")){
            STATUS.setText((CharSequence)"PC Wins");
        }
        else{
            STATUS.setText((CharSequence)"It's a tie!");
        }
        gameover.setVisibility(View.VISIBLE);
        STATUS.setVisibility(View.VISIBLE);
    }

    public void click(View v){
        v.setEnabled(false);
        num_of_turns++;
        int last = Integer.parseInt((String) v.getContentDescription());
        turns[getLast()] = last;
        displayO(place[last]);
        doMove();
        if(!status.equals("")){
            gameOver();
        }
    }

    public void disableAll(){
        int [] buttons = {R.id.place2, R.id.place3, R.id.place4, R.id.place5, R.id.place6, R.id.place7, R.id.place8, R.id.place9};
        for (int value : buttons) {
            Button button = findViewById(value);
            button.setEnabled(false);
        }
    }

    public void clearAll(View v){
        int [] buttons = {R.id.place2, R.id.place3, R.id.place4, R.id.place5, R.id.place6, R.id.place7, R.id.place8, R.id.place9};
        for (int value : buttons) {
            Button button = findViewById(value);
            button.setEnabled(true);
        }
        int [] placeX = {0, R.id.pos2, R.id.pos3, R.id.pos4, R.id.pos5, R.id.pos6, R.id.pos7, R.id.pos8, R.id.pos9};
        for(int i=1;i<placeX.length;i++){
            ImageView place = (ImageView) findViewById(placeX[i]);
            place.setVisibility(View.INVISIBLE);
        }
        status = "";
        num_of_turns = 0;
        for(int x =1;x<turns.length;x++){
            turns[x] =0;
        }
        turns[0]=1;
        TextView STATUS = findViewById(R.id.status);
        TextView gameover = findViewById(R.id.gameOver);
        STATUS.setVisibility(View.INVISIBLE);
        gameover.setVisibility(View.INVISIBLE);
    }
    
    public void disable(int num){
        int [] buttons = {R.id.place2, R.id.place3, R.id.place4, R.id.place5, R.id.place6, R.id.place7, R.id.place8, R.id.place9};
        for (int value : buttons) {
            Button button = findViewById(value);
            if (Integer.parseInt((String) button.getContentDescription()) == num) {
                button.setEnabled(false);
            }
        }
    }

    public void doMove() {
        if (num_of_turns > 0) {
            //2, 4, 6, 8:
            if (turns[1] % 2 == 0) {
                display(place[5]);
                disable(5);
                if (num_of_turns > 1) {
                    if (turns[2] != 9) {
                        display(place[9]);
                        disable(9);
                        status = "1";
                    } else if (turns[1] == 6 || turns[1] == 4) {
                        display(place[3]);
                        disable(3);
                        if (num_of_turns > 2) {
                            if (turns[3] != 2) {
                                display(place[2]);
                                disable(2);
                            } else {
                                display(place[7]);
                                disable(7);
                            }
                            status = "1";
                        }
                    } else if (turns[1] == 2 || turns[1] == 8) {
                        display(place[7]);
                        disable(7);
                        if (num_of_turns > 2) {
                            if (turns[3] != 3) {
                                display(place[3]);
                                disable(3);
                                status = "1";
                            } else {
                                display(place[4]);
                                disable(4);
                                status = "1";
                            }
                        }
                    }
                }
            }
            //5, 7, 3:
            else if (turns[1] == 3 || turns[1] == 5 || turns[1] == 7) {
                display(place[9]);
                disable(9);
                if (num_of_turns > 1) {
                    if (turns[2] != 5 && turns[1] != 5) {
                        display(place[5]);
                        disable(5);
                        status = "1";
                    } else if (turns[2] == 3) {
                        display(place[7]);
                        disable(7);
                        if (num_of_turns > 2) {
                            if (turns[3] != 4) {
                                display(place[4]);
                                disable(4);
                                status = "1";
                            } else {
                                display(place[8]);
                                disable(8);
                                status = "1";
                            }
                        }
                    } else if (turns[2] == 7) {
                        display(place[3]);
                        disable(3);
                        if (num_of_turns > 2) {
                            if (turns[3] != 2) {
                                display(place[2]);
                                disable(2);
                                status = "1";
                            } else {
                                display(place[6]);
                                disable(4);
                                status = "1";
                            }
                        }
                    } else if (turns[2] == 8) {
                        display(place[2]);
                        disable(2);
                        if (num_of_turns > 2) {
                            if (turns[3] != 3) {
                                display(place[3]);
                                disable(3);
                                status = "1";
                            } else {
                                display(place[7]);
                                disable(7);
                                if (num_of_turns > 3) {
                                    if (turns[4] != 4) {
                                        display(place[4]);
                                        disable(4);
                                        status = "1";
                                    } else {
                                        display(place[6]);
                                        disable(4);
                                        status = "0";
                                    }
                                }
                            }
                        }
                    } else if (turns[2] == 4) {
                        display(place[6]);
                        disable(4);
                        if (num_of_turns > 2) {
                            if (turns[3] != 3) {
                                display(place[3]);
                                disable(3);
                            } else {
                                display(place[7]);
                                disable(7);
                                if (num_of_turns > 3) {
                                    if (turns[4] != 8) {
                                        display(place[8]);
                                        disable(8);
                                        status = "1";
                                    } else {
                                        display(place[2]);
                                        disable(2);
                                        status = "0";
                                    }
                                }
                            }
                        }
                    } else if (turns[2] == 6) {
                        display(place[4]);
                        disable(4);
                        if (num_of_turns > 2) {
                            if (turns[3] != 7) {
                                display(place[7]);
                                disable(7);
                                status = "1";
                            } else {
                                display(place[3]);
                                disable(3);
                                if (num_of_turns > 3) {
                                    if (turns[4] != 2) {
                                        display(place[2]);
                                        disable(2);
                                        status = "1";
                                    } else {
                                        display(place[8]);
                                        disable(8);
                                        status = "0";
                                    }
                                }
                            }
                        }
                    } else if (turns[2] == 5 && turns[1] != 3) {
                        display(place[3]);
                        disable(3);
                        if (num_of_turns > 2) {
                            if (turns[3] != 2) {
                                display(place[2]);
                                disable(2);
                                status = "1";
                            } else {
                                display(place[6]);
                                disable(6);
                                status = "1";
                            }
                        }
                    } else if (turns[2] == 5 && turns[1] != 7) {
                        display(place[7]);
                        disable(7);
                        if (num_of_turns > 2) {
                            if (turns[3] != 4) {
                                display(place[4]);
                                disable(4);
                                status = "1";
                            } else {
                                display(place[8]);
                                disable(8);
                                status = "1";
                            }
                        }
                    } else if (turns[2] == 2) {
                        display(place[8]);
                        disable(8);
                        if (num_of_turns > 2) {
                            if (turns[3] != 7) {
                                display(place[7]);
                                disable(7);
                                status = "1";
                            } else {
                                display(place[3]);
                                disable(3);
                                if (num_of_turns > 3) {
                                    if (turns[4] != 6) {
                                        display(place[6]);
                                        disable(4);
                                        status = "1";
                                    } else {
                                        display(place[4]);
                                        disable(4);
                                        status = "0";
                                    }
                                }
                            }
                        }
                    }
                }
            }

            else {  //9
                display(place[3]);
                disable(3);
                if (num_of_turns > 1) {
                    if (turns[2] != 2) {
                        display(place[2]);
                        disable(2);
                        status = "1";
                    } else {
                        display(place[7]);
                        disable(7);
                        if (num_of_turns > 2) {
                            if (turns[3] != 4) {
                                display(place[4]);
                                disable(4);
                                status = "1";
                            } else {
                                display(place[5]);
                                disable(5);
                                status = "1";
                            }
                        }
                    }
                }
            }
        }
    }

    public void launchSudokuSolver(){
        Intent i = new Intent(this, sudokuSolver.class);
        startActivity(i);
    }

    public void launchTriangle(View v){
        Intent i = new Intent(this, Triangle.class);
        startActivity(i);
    }

    public void launchSudoku(){
        Intent i = new Intent(this, sudokuOff.class);
        startActivity(i);
    }

    public void launchRoom(){
        Intent i = new Intent(this, sudokuRoom.class);
        startActivity(i);
    }

    public void launchUpdate(){
        Intent i = new Intent(this, Update.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        if(((DrawerLayout)findViewById(R.id.frame)).isDrawerOpen(GravityCompat.START)){
            ((DrawerLayout)findViewById(R.id.frame)).closeDrawer(GravityCompat.START);
        }
        else{
            finish();
        }
    }


    public void openNav(View v){
        ((DrawerLayout)findViewById(R.id.frame)).openDrawer(GravityCompat.START);
    }

    public void setupNavi(){
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
                findViewById(R.id.frame).setTranslationZ(-10);
                navi = 1;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(navi==0) {
                    findViewById(R.id.frame).setTranslationZ(10);
                }
                navi=0;
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

                    case "Sudoku Online":
                        launchRoom();
                        break;

                    case "Update Info":
                        launchUpdate();
                        break;

                    case "Log Out":
                        logOut();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });


    }

    public void logOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("Are you sure you want to log out?").
                setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        startActivity(new Intent(getApplicationContext(), logIn.class));
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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
