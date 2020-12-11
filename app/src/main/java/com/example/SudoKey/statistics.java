package com.example.SudoKey;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class statistics extends AppCompatActivity {
    String username;
    int highscore, onlineWins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
        username = data.getString("username", null);
        highscore = data.getInt("highscore", 0);
        onlineWins = data.getInt("onlineWins",0);
        ((TextView)findViewById(R.id.highscore)).setText((String)(highscore+" Seconds"));
        ((TextView)findViewById(R.id.wins)).setText((String)(onlineWins+" Wins"));

    }
}