package com.example.SudoKey;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class sudokuSolver extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku_solver);
        Button solverButton = ((Button)findViewById(R.id.solve));
        solverButton.setEnabled(false);
        EditText sudokuNums = (EditText)(findViewById(R.id.sudokuNums));
    }

    public void upload(View v){
        TextView t = findViewById(R.id.sudokuNums);
        String boardNums = t.getText().toString();
        if(!(boardNums.length()==81)){
            Toast.makeText(this, "Please enter a valid sudoku", Toast.LENGTH_LONG).show();
        }
        else{
            int [][] board =new int [10][9];
            char [] arr = boardNums.toCharArray();
            for(int i =0;i<9;i++) {
                for(int j=0;j<9;j++){
                    board[i][j] = Character.getNumericValue(arr[i * 9 + j]);
                }
            }
            board[9][0] = 0;
            board[9][1] = 0;
            setBoard(board);
        }
    }

    public void solveSudoku(View v){
        TextView block;
        StringBuilder str = new StringBuilder();
        int [] array = {R.id.id0, R.id.id1, R.id.id2, R.id.id3, R.id.id4, R.id.id5, R.id.id6, R.id.id7, R.id.id8, R.id.id9, R.id.id10, R.id.id11, R.id.id12, R.id.id13, R.id.id14, R.id.id15, R.id.id16, R.id.id17, R.id.id18, R.id.id19, R.id.id20, R.id.id21, R.id.id22, R.id.id23, R.id.id24, R.id.id25, R.id.id26, R.id.id27, R.id.id28, R.id.id29, R.id.id30, R.id.id31, R.id.id32, R.id.id33, R.id.id34, R.id.id35, R.id.id36, R.id.id37, R.id.id38, R.id.id39, R.id.id41, R.id.id42, R.id.id43, R.id.id44, R.id.id45, R.id.id46, R.id.id47, R.id.id48, R.id.id49, R.id.id50, R.id.id51, R.id.id52, R.id.id53, R.id.id54, R.id.id55, R.id.id56, R.id.id57, R.id.id58, R.id.id59, R.id.id60, R.id.id61, R.id.id62, R.id.id63, R.id.id64, R.id.id65, R.id.id66, R.id.id67, R.id.id68, R.id.id69, R.id.id70, R.id.id71, R.id.id72, R.id.id73, R.id.id74, R.id.id75, R.id.id76, R.id.id77, R.id.id78, R.id.id79, R.id.id80, R.id.id81};
        for(int i=0;i<9;i++){
            for(int j = 0;j<9;j++) {
                block = ((TextView) findViewById(array[9*i+j]));
                str.append(block.getText());
            }
        }
        String sudokuNums = str.toString();
        int [][] board =new int [10][9];
        char [] arr = sudokuNums.toCharArray();
        for(int i =0;i<9;i++) {
            for(int j=0;j<9;j++){
                board[i][j] = Character.getNumericValue(arr[i * 9 + j]);
            }
        }
        board[9][0] = 0;
        int [][] board1 = solve(board);
        if(lastCheck(board1)){
            setBoard(board1);
        }
        else{
            Toast.makeText(this, "This sudoku has no solution", Toast.LENGTH_LONG).show();
        }

    }

    public void setBoard(int [][] board){
        TextView block;

        int [] array = {R.id.id0, R.id.id1, R.id.id2, R.id.id3, R.id.id4, R.id.id5, R.id.id6, R.id.id7, R.id.id8, R.id.id9, R.id.id10, R.id.id11, R.id.id12, R.id.id13, R.id.id14, R.id.id15, R.id.id16, R.id.id17, R.id.id18, R.id.id19, R.id.id20, R.id.id21, R.id.id22, R.id.id23, R.id.id24, R.id.id25, R.id.id26, R.id.id27, R.id.id28, R.id.id29, R.id.id30, R.id.id31, R.id.id32, R.id.id33, R.id.id34, R.id.id35, R.id.id36, R.id.id37, R.id.id38, R.id.id39, R.id.id41, R.id.id42, R.id.id43, R.id.id44, R.id.id45, R.id.id46, R.id.id47, R.id.id48, R.id.id49, R.id.id50, R.id.id51, R.id.id52, R.id.id53, R.id.id54, R.id.id55, R.id.id56, R.id.id57, R.id.id58, R.id.id59, R.id.id60, R.id.id61, R.id.id62, R.id.id63, R.id.id64, R.id.id65, R.id.id66, R.id.id67, R.id.id68, R.id.id69, R.id.id70, R.id.id71, R.id.id72, R.id.id73, R.id.id74, R.id.id75, R.id.id76, R.id.id77, R.id.id78, R.id.id79, R.id.id80, R.id.id81};
        for(int i=0;i<9;i++){
            for(int j = 0;j<9;j++) {
                block = ((TextView) findViewById(array[9*i+j]));
                block.setText(String.valueOf(board[i][j]));
            }
        }
        Button solverButton = ((Button)findViewById(R.id.solve));
        if(!solverButton.isEnabled()) {
            solverButton.setEnabled(true);
        }
    }

    public static int [][] solve(int [][] bo) {
        int[] find = find_empty(bo);
        if (find == null) {
            bo[9][0] = 1;
            return bo;
        } else {
            int row = find[0];
            int col = find[1];
            int[] arr = new int[2];
            for (int i = 1; i < 10; i++) {
                arr[0] = row;
                arr[1] = col;
                if (valid(bo, i, arr)) {
                    bo[row][col] = i;
                    if (solve(bo)[9][0] == 1) {
                        return bo;
                    }
                    bo[row][col] = 0;
                }
            }
            bo[9][0] = 0;
            return bo;
        }
    }

    public static boolean valid(int [][] board, int num, int [] pos){
        for(int i = 0; i<9;i++) {
            if(board[pos[0]][i] == num && pos[ 1] !=i) {
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
        int [] arr = new int[2];

        for(int i = box_y*3;i<box_y*3+3;i++) {
            for(int j = box_x*3;j<box_x*3+3;j++) {
                arr[0] = i;
                arr[1]=j;
                if(board[i][j] == num && arr != pos){
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

    public static boolean lastCheck(int [][] board){
        for(int i = 0; i<9;i++){
            for(int j = 0; j<9;j++){
                if(board[i][j] == 0){
                    return false;
                }
            }
        }
        return true;
    }















}