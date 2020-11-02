package com.example.MathApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class Update extends AppCompatActivity {
    ImageView imageView;
    String oldUsername, oldPassword, inUsername, oldPhone, oldEmail;
    int status=1;
    User oldUser;
    byte [] image = null;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        SharedPreferences data = getSharedPreferences("data",MODE_PRIVATE );
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
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
        imageView.setVisibility(View.VISIBLE);
        try{
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            FirebaseStorage.getInstance().getReference(oldUsername).putBytes(byteArray);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void update(View v){
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
            // phone check:
        if(!inPhone.equals("")){
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


        if(status==1) {
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


}