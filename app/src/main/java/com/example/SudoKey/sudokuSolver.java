package com.example.SudoKey;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;


public class sudokuSolver extends AppCompatActivity {
    int [] placeTable = {R.id.place1, R.id.place2, R.id.place3, R.id.place4, R.id.place5, R.id.place6, R.id.place7, R.id.place8, R.id.place9, R.id.place10, R.id.place11, R.id.place12, R.id.place13, R.id.place14, R.id.place15, R.id.place16, R.id.place17, R.id.place18, R.id.place19, R.id.place20, R.id.place21, R.id.place22, R.id.place23, R.id.place24, R.id.place25, R.id.place26, R.id.place27, R.id.place28, R.id.place29, R.id.place30, R.id.place31, R.id.place32, R.id.place33, R.id.place34, R.id.place35, R.id.place36, R.id.place37, R.id.place38, R.id.place39, R.id.place40, R.id.place41, R.id.place42, R.id.place43, R.id.place44, R.id.place45, R.id.place46, R.id.place47, R.id.place48, R.id.place49, R.id.place50, R.id.place51, R.id.place52, R.id.place53, R.id.place54, R.id.place55, R.id.place56, R.id.place57, R.id.place58, R.id.place59, R.id.place60, R.id.place61, R.id.place62, R.id.place63, R.id.place64, R.id.place65, R.id.place66, R.id.place67, R.id.place68, R.id.place69, R.id.place70, R.id.place71, R.id.place72, R.id.place73, R.id.place74, R.id.place75, R.id.place76, R.id.place77, R.id.place78, R.id.place79, R.id.place80, R.id.place81};
    int [][]board = new int[9][9];

    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_solver);
        for(int i=0;i<81;i++){
            ((EditText)findViewById(placeTable[i])).setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    ((EditText)view).setText("");
                    return false;
                }
            });
        }
    }



    public void solveSudoku(View v){
        String temp;
        for(int x = 0;x<9;x++){
            for(int y =0;y<9;y++){
                temp = ((EditText)findViewById(placeTable[x*9+y])).getText().toString();
                if(temp.equals("")){
                    board[x][y] = 0;
                }
                else {
                    board[x][y] =Integer.parseInt(temp);
                }
            }
        }
        if(!validBoard(board)){
            Toast.makeText(this, "This sudoku has no solution", Toast.LENGTH_LONG).show();
        }
        else {
            if (solve()) {
                setBoard(board);
            } else {
                Toast.makeText(this, "This sudoku has no solution", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void setBoard(int [][] board){
        EditText block;
        for(int i=0;i<9;i++){
            for(int j = 0;j<9;j++) {
                block = ((EditText)findViewById(placeTable[9*i+j]));
                block.setText(String.valueOf(board[i][j]));
            }
        }
        Button solverButton = ((Button)findViewById(R.id.solve));
        if(!solverButton.isEnabled()) {
            solverButton.setEnabled(true);
        }
    }

    public boolean solve() {
        int[] find = find_empty(board);
        if (find == null) {
            return true;
        } else {
            int row = find[0];
            int col = find[1];
            int[] arr = new int[2];
            for (int i = 1; i < 10; i++) {
                arr[0] = row;
                arr[1] = col;
                if (valid(board, i, arr)) {
                    board[row][col] = i;
                    if (solve()) {
                        return true;
                    }
                    board[row][col] = 0;
                }
            }
            return false;
        }
    }

    public static boolean valid(int [][] board, int num, int [] pos){
        for(int i = 0; i<9;i++) {
            if(board[pos[0]][i] == num && pos[1] !=i){
                return false;
            }
        }

        for(int i = 0;i<9;i++) {
            if (board[i][pos[1]] == num && pos[0] != i) {
                return false;
            }
        }
        double number = (double) pos[1]/3;
        int box_x = (int) Math.floor(number);
        double number1 = (double) pos[0]/3;
        int box_y = (int) Math.floor(number1);
        for(int i = box_y*3;i<box_y*3+3;i++) {
            for(int j = box_x*3;j<box_x*3+3;j++) {
                if(board[i][j] == num && !Arrays.equals(new int[]{i, j}, pos)){
                    return false;
                }
            }
        }
        return true;





    }

    public static int [] find_empty(int [][] board) {
        int [] returnArray = new int [2];
        for(int i=0;i<9;i++) {
            for(int j = 0;j<9;j++) {
                if(board[i][j] == 0){
                    returnArray[0] = i;
                    returnArray[1] = j;
                    return returnArray;
                }
            }
        }
        return null;
    }

    public void clearBoard(View v){
        for(int i=0;i<81;i++){
            ((EditText)findViewById(placeTable[i])).setText("");
        }
    }

    public boolean validBoard(int [][] board){
        for(int i=0;i<9;i++){
            for(int j=0;j<9;j++){
                if(board[i][j]!=0){
                    if(!valid(board, board[i][j], new int[]{i, j})){
                        return false;
                    }
                }
            }
        }
        return true;
    }












}