package com.example.SudoKey;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileOutputStream;

public class sudokuOff extends AppCompatActivity{
    String diff = "easy";
    Context context;
    int reamin=0, status=0;
    int[][] board;
    int [][] solved;
    Sudoku sudoku;
    int [] tableArray = {R.id.id0, R.id.id1, R.id.id2, R.id.id3, R.id.id4, R.id.id5, R.id.id6, R.id.id7, R.id.id8, R.id.id9, R.id.id10, R.id.id11, R.id.id12, R.id.id13, R.id.id14, R.id.id15, R.id.id16, R.id.id17, R.id.id18, R.id.id19, R.id.id20, R.id.id21, R.id.id22, R.id.id23, R.id.id24, R.id.id25, R.id.id26, R.id.id27, R.id.id28, R.id.id29, R.id.id30, R.id.id31, R.id.id32, R.id.id33, R.id.id34, R.id.id35, R.id.id36, R.id.id37, R.id.id38, R.id.id39, R.id.id41, R.id.id42, R.id.id43, R.id.id44, R.id.id45, R.id.id46, R.id.id47, R.id.id48, R.id.id49, R.id.id50, R.id.id51, R.id.id52, R.id.id53, R.id.id54, R.id.id55, R.id.id56, R.id.id57, R.id.id58, R.id.id59, R.id.id60, R.id.id61, R.id.id62, R.id.id63, R.id.id64, R.id.id65, R.id.id66, R.id.id67, R.id.id68, R.id.id69, R.id.id70, R.id.id71, R.id.id72, R.id.id73, R.id.id74, R.id.id75, R.id.id76, R.id.id77, R.id.id78, R.id.id79, R.id.id80, R.id.id81};
    int [] textTable = {R.id.place1, R.id.place2, R.id.place3, R.id.place4, R.id.place5, R.id.place6, R.id.place7, R.id.place8, R.id.place9, R.id.place10, R.id.place11, R.id.place12, R.id.place13, R.id.place14, R.id.place15, R.id.place16, R.id.place17, R.id.place18, R.id.place19, R.id.place20, R.id.place21, R.id.place22, R.id.place23, R.id.place24, R.id.place25, R.id.place26, R.id.place27, R.id.place28, R.id.place29, R.id.place30, R.id.place31, R.id.place32, R.id.place33, R.id.place34, R.id.place35, R.id.place36, R.id.place37, R.id.place38, R.id.place39, R.id.place40, R.id.place41, R.id.place42, R.id.place43, R.id.place44, R.id.place45, R.id.place46, R.id.place47, R.id.place48, R.id.place49, R.id.place50, R.id.place51, R.id.place52, R.id.place53, R.id.place54, R.id.place55, R.id.place56, R.id.place57, R.id.place58, R.id.place59, R.id.place60, R.id.place61, R.id.place62, R.id.place63, R.id.place64, R.id.place65, R.id.place66, R.id.place67, R.id.place68, R.id.place69, R.id.place70, R.id.place71, R.id.place72, R.id.place73, R.id.place74, R.id.place75, R.id.place76, R.id.place77, R.id.place78, R.id.place79, R.id.place80, R.id.place81};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_off);
        context= getApplicationContext();
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
                Toast.makeText(getApplicationContext(), "Difficulty changed to "+diff, Toast.LENGTH_LONG).show();
                sudoku = new Sudoku(diff, context);
                board = sudoku.getBoard();
                solved = Sudoku.solve(Sudoku.boardNumsToBoard(sudoku.boardNums));
                setBoard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }});
        setupNavi();
        sudoku = new Sudoku(diff, context);
        board = sudoku.getBoard();
        solved = Sudoku.solve(Sudoku.boardNumsToBoard(sudoku.boardNums));
        setBoard();
    }

    public void setBoard(){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j] == 0){
                    reamin++;
                    findViewById(tableArray[i*9+j]).setVisibility(View.INVISIBLE);
                    findViewById(textTable[i*9+j]).setVisibility(View.VISIBLE);
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
                    alert("you did a mistake");
                    ((TextView)findViewById(textTable[i])).setText("");
                }
                else {
                    reamin--;
                    ((TextView) findViewById(tableArray[i])).setText(input);
                    findViewById(tableArray[i]).setVisibility(View.VISIBLE);
                    findViewById(textTable[i]).setVisibility(View.INVISIBLE);
                }
            }
        }
        if(reamin==0){
            gameOver();
        }
    }

    public void alert(String alert) {
        Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
    }

    public void gameOver(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage("well done you won!").
                setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(dialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        dialog.getButton(dialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    public void download(View v){
        View a = new MyCanvas(context, board);
        Bitmap bitmap = Bitmap.createBitmap(900, 900, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        a.draw(canvas);

        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(String.valueOf(context.getAssets())));
        }
        catch (Exception ignored){}
        MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title" , "des");
        alert("download to gallery");
    }

    public void onBackPressed(){
        if(((DrawerLayout)findViewById(R.id.frame)).isDrawerOpen(GravityCompat.START)){
            ((DrawerLayout)findViewById(R.id.frame)).closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
            builder.setMessage("Are you sure you want to exit?").
                    setCancelable(true)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
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

                    case "Sudoku Online":
                        launchRoom();
                        break;

                    case "Tic Tac Toe vs PC":
                        launchTicTacToePC();
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

    public void launchUpdate(){
        Intent i = new Intent(this, Update.class);
        startActivity(i);
    }

    public void launchRoom(){
        Intent i = new Intent(this, sudokuRoom.class);
        startActivity(i);
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