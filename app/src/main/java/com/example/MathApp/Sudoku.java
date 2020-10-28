package com.example.MathApp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;


public class Sudoku {
    private int [][] board = new int[10][9];
    public int [][] answer = new int [10][9];
    public String boardNums="";
   public Sudoku(String diff, Context context){
       InputStream file = null;

       try {
           file = context.getAssets().open(diff);
       } catch (IOException e) {
           e.printStackTrace();
       }

       assert file != null;
       BufferedReader reader = new BufferedReader(new InputStreamReader(file));
       StringBuilder sb = new StringBuilder();
       try {
           String mLine = reader.readLine();

           while (mLine != null) {
               sb.append(mLine); // process line
               mLine = reader.readLine();

           }
            reader.close();
       }
       catch (Exception e){
           System.out.println("ERROR");
       }
        int num = random(0,sb.length()/81);
        boardNums = sb.substring(81*num,81*num+81);
        this.answer = solve(boardNumsToBoard(boardNums));
        this.board =boardNumsToBoard(boardNums);
   }

   public Sudoku(String boardNums){
       this.boardNums = boardNums;
       this.answer = solve(boardNumsToBoard(boardNums));
       this.board =boardNumsToBoard(boardNums);
   }
    public int random(int min, int max){
        Random rand = new Random();
        int num = rand.nextInt();
        if(num<0){
            num = -num;
        }
        return num%(max-min)+min;
    }

    public String getBoardNums() {
        return boardNums;
    }

    public void setBoardNums(String boardNums) {
        this.boardNums = boardNums;
    }

    public static int [][] boardNumsToBoard(String boardNums){
       int [][] board = new int[10][9];
        char [] arr = boardNums.toCharArray();
        for(int i =0;i<9;i++) {
            for(int j=0;j<9;j++){
                board[i][j] = Character.getNumericValue(arr[i * 9 + j]);
            }
        }
        board[9][0] = 0;
        board[9][1] = 0;
        return board;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int [][] getAnswer(){
       return this.answer;
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

    public String toString(){
       StringBuilder str= new StringBuilder();
        for(int i=0;i<9;i++) {
            if(i % 3 == 0 && i !=0) {
                str.append("- - - - - - - - - - - - - "+"\n");
            }
            for(int j = 0;j<9;j++) {
                if(j % 3 == 0 && j !=0){
                    str.append(" | ");
                }

                if(j == 8) {
                    str.append(this.board[i][j]).append("\n");
                }
                else{
                    str.append(this.board[i][j]).append(" ");
                }
            }
        }
        return str.toString();
    }

}
