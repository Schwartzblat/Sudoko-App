package com.example.SudoKey;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class Update extends AppCompatActivity {
    ImageView imageView;
    String oldUsername, oldPassword, inUsername, oldPhone, oldEmail;
    int status=1 , check=0;
    int navi=0;
    User oldUser;
    byte [] image = null;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
        setupNavi();
        oldUsername = data.getString("username", null);
        oldPassword = data.getString("password", null);
        oldPhone = data.getString("phone", null);
        oldEmail = data.getString("email", null);
        oldUser = new User(oldUsername, oldEmail, oldPhone, oldPassword);
        imageView = findViewById(R.id.image);
        try{
            StorageReference islandRef = FirebaseStorage.getInstance().getReference(oldUsername);
            final long ONE_MEGABYTE = 1024 * 1024;

            islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    image = bytes.clone();
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes , 0, bytes.length));
                    imageView.setVisibility(View.VISIBLE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }
        catch (Exception ignored){
        }

    }

    public void picture(View v){
        Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cam,0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                FirebaseStorage.getInstance().getReference(oldUsername).putBytes(byteArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void update(View v){
        status =1;
        check=0;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser authUser= mAuth.getCurrentUser();
        // get input:
        inUsername = ((TextView)findViewById(R.id.username)).getText().toString();
        String inEmail = ((TextView)findViewById(R.id.email)).getText().toString();
        String inPhone = ((TextView)findViewById(R.id.phone)).getText().toString();
        String inPass = ((TextView)findViewById(R.id.password)).getText().toString();
        String inPassc = ((TextView)findViewById(R.id.passwordc)).getText().toString();
        String oldPass = ((TextView)findViewById(R.id.oldPass)).getText().toString();
        //check:

        //check username
        if(!inUsername.equals("")) {
            check=1;
            if (inUsername.length() < 4) {
                status = 0;
                alert("please enter valid username");
            }
            char [] charArray = ".[]#$ ".toCharArray();
            for(int i =0;i<oldUsername.length();i++){
                for (char c : charArray) {
                    if (oldUsername.toCharArray()[i] == c) {
                        status = 0;
                        alert("please enter valid username");
                        break;
                    }
                }
            }
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Users").child("user");
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User xuser;
                    for (DataSnapshot user : dataSnapshot.getChildren()) {
                        xuser = user.getValue(User.class);
                        if (xuser != null) {
                            if (inUsername.equals(xuser.username)) {
                                status = 0;
                                alert("this username is already taken");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            if(status==1){
                oldUser.setUsername(inUsername);
            }
        }


        //check passwords:
        if(!inPass.equals("")){
            check =1;
            if(oldPass.equals(oldPassword)) {
                if (inPass.equals(inPassc) && inPass.length() > 7) {
                    if(authUser!=null) {
                        authUser.updatePassword(inPass);
                    }
                    else{
                        System.out.println("noder");
                    }
                    oldUser.setPassword(inPass);
                } else {
                    alert("please enter valid password");
                    status = 0;
                }
            }
            else{
                alert("incorrect password please try again");
                status=0;
            }
        }

        if(oldPass.equals("")){
            alert("please enter your old password");
            status=0;
        }
            // phone check:
        if(!inPhone.equals("")){
            check =1;
            if(inPhone.length()==10){
                oldUser.setPhone(inPhone);
            }
            else{
                alert("please enter valid phone number");
                status=0;
            }
        }
        // email check:
        if(!inEmail.equals("")){
            check =1;
            if(inEmail.contains("@")){
                if(authUser!=null) {
                    authUser.updateEmail(inEmail);
                }
                oldUser.setEmail(inEmail);
            }
            else{
                alert("please enter valid email");
                status=0;
            }
        }


        if(status==1 && check==1) {
            updateInfo(oldUser);
        }





    }


    public void alert(String alert) {
        Toast.makeText(this, alert, Toast.LENGTH_LONG).show();
    }

    public void updateInfo(User user){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child("user");
        //delete the old one:
        FirebaseDatabase.getInstance().getReference("Users").child("user").child(oldUsername).removeValue();
        Objects.requireNonNull(mAuth.getCurrentUser()).updatePassword(user.password);
        Objects.requireNonNull(mAuth.getCurrentUser()).updateEmail(user.email);
        myRef.child(user.getUsername()).setValue(user);

        //changing the name of the image:
        if(image!=null) {
            FirebaseStorage.getInstance().getReference(oldUsername).delete();
            FirebaseStorage.getInstance().getReference(inUsername).putBytes(image);
        }
        oldUsername = user.username;
        oldPhone = user.phone;
        oldEmail = user.email;
        oldPassword = user.password;
        //clear all:
        ((TextView)findViewById(R.id.username)).setText("");
        ((TextView)findViewById(R.id.password)).setText("");
        ((TextView)findViewById(R.id.passwordc)).setText("");
        ((TextView)findViewById(R.id.phone)).setText("");
        ((TextView)findViewById(R.id.email)).setText("");
        ((TextView)findViewById(R.id.oldPass)).setText("");

        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE ).edit();
        editor.putString("username", oldUsername);
        editor.putString("password", oldPassword);
        editor.putString("phone", oldPhone);
        editor.putString("email", oldEmail);
        editor.apply();
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

    public void launchSudoku(){
        Intent i = new Intent(this, sudokuOff.class);
        startActivity(i);
    }

    public void launchRoom(){
        Intent i = new Intent(this, sudokuRoom.class);
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
        //moveTaskToBack(true);
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
                status = 1;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if(status==0) {
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

                    case "Sudoku Offline":
                        launchSudoku();
                        break;

                    case "Sudoku Online":
                        launchRoom();
                        break;

                    case "Tic Tac Toe vs PC":
                        launchTicTacToePC();
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