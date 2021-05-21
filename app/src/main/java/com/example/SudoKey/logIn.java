package com.example.SudoKey;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Base64;

public class logIn extends AppCompatActivity {
    DataSnapshot data = null;
    String username ="", password="", email, phone;
    int status = 0, onlineWins;
    FirebaseAuth mAuth;
    byte[] image;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        //int [][]x = {{5},{2,2,3,2},{4,1,2,2},{2,1,2,1,2},{4,1,2,2,3},{1,2,1,3},{2,3,1,4},{5,2,4},{5,1,3},{1,4,2,3},{2,4,1,4},{2,6,5},{3,11},{4,3,7},{2,2,2,3},{1,1,2,2},{5,3,1},{2,2,2,1},{5,3,2},{3,1}};
        //int [][]y = {{2,4},{3,2,3},{1,1,3,1,1},{2,1,2,4,4},{3,8,3},{1,1,2,2},{2,4,1},{1,9,2,1,1},{2,3,7,1,2},{5,4},{4,3,2},{3,3,3},{2,10},{1,4,2},{2,2,3},{1,5,3},{3,4,4},{2,9},{1,7},{5}};
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("user");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                data = dataSnapshot;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }});

        ((EditText)findViewById(R.id.username)).setOnTouchListener(new OnTouchListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.password).setBackground(getDrawable(R.drawable.border));
                findViewById(R.id.username).setBackground(getDrawable(R.drawable.border_touch));
                return false;
            }
        });
        ((EditText)findViewById(R.id.password)).setOnTouchListener(new OnTouchListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                findViewById(R.id.username).setBackground(getDrawable(R.drawable.border));
                findViewById(R.id.password).setBackground(getDrawable(R.drawable.border_touch));
                return false;
            }
        });
    }

    public void log(View v){
        findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        //get values
        username = ((TextView)findViewById(R.id.username)).getText().toString();
        password = ((TextView)findViewById(R.id.password)).getText().toString();
        if(data!=null) {
            User xuser;
            for (DataSnapshot user : data.getChildren()) {
                xuser = user.getValue(User.class);
                if (xuser != null) {
                    if (username.equals(xuser.username) && password.equals(xuser.password)) {
                        status=1;
                        phone = xuser.phone;
                        email = xuser.email;
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            try{
                                                StorageReference islandRef = FirebaseStorage.getInstance().getReference(username);
                                                final long ONE_MEGABYTE = 1024 * 1024;

                                                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {
                                                        image = bytes.clone();
                                                        StatusIsOk();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {
                                                        StatusIsOk();
                                                    }
                                                });
                                            }
                                            catch (Exception ignored){
                                                StatusIsOk();
                                            }
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            findViewById(R.id.progressBar).setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                                            // ...
                                        }

                                        // ...
                                    }
                                });
                    }
                }
            }
            if (status == 0) {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "invalid name or password", Toast.LENGTH_LONG).show();
                ((TextView) findViewById(R.id.password)).setText("");
            }
        }
    }
    int highscore;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void StatusIsOk(){
        FirebaseDatabase.getInstance().getReference("Users").child("user").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    highscore = Integer.parseInt(String.valueOf(snapshot.child("highscore").getValue()));
                }
                catch(Exception e){
                    e.printStackTrace();
                    highscore=0;
                }
                try{
                    onlineWins= Integer.parseInt(String.valueOf(snapshot.child("onlineWins").getValue()));
                }
                catch(Exception e){
                    onlineWins=0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String encodedImage;
        try{
            encodedImage = Base64.getEncoder().encodeToString(image);
        }
        catch (Exception ignored){
            encodedImage = null;
        }
        Intent i = new Intent(this, sudokuRoom.class);
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE ).edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putInt("highscore", highscore);
        editor.putInt("onlineWins", onlineWins);
        editor.putString("image_data",encodedImage);
        editor.apply();
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        startActivity(i);
        finish();

    }

    public void launchSignUp(View v){
        Intent i = new Intent(this, signUp.class);
        startActivity(i);
        finish();
    }

    public void onBackPressed(){
        moveTaskToBack(true);
    }
}