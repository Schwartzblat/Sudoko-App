package com.example.MathApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TicTacToePC extends AppCompatActivity{
    private int num_of_turns = 0;
    private int [] turns = {1,0,0,0,0};
    private String status = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_pc);
        display(R.id.place1X);
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

    public void display(int id){
        ImageView place = (ImageView) findViewById(id);
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
        int [] placeO = {0, R.id.place1O, R.id.place2O, R.id.place3O, R.id.place4O, R.id.place5O, R.id.place6O, R.id.place7O, R.id.place8O, R.id.place9O};
        v.setEnabled(false);
        num_of_turns++;
        int last = Integer.parseInt((String) v.getContentDescription());
        turns[getLast()] = last;
        display(placeO[last]);
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
        int [] placeO = {0, R.id.place1O, R.id.place2O, R.id.place3O, R.id.place4O, R.id.place5O, R.id.place6O, R.id.place7O, R.id.place8O, R.id.place9O};
        for(int i=1;i<placeO.length;i++){
            ImageView place = (ImageView) findViewById(placeO[i]);
            place.setVisibility(View.INVISIBLE);
        }
        int [] placeX = {0, R.id.place2X, R.id.place3X, R.id.place4X, R.id.place5X, R.id.place6X, R.id.place7X, R.id.place8X, R.id.place9X};
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
        display(R.id.place1X);
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
        int[] place = {0, R.id.place1X, R.id.place2X, R.id.place3X, R.id.place4X, R.id.place5X, R.id.place6X, R.id.place7X, R.id.place8X, R.id.place9X};
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

}
