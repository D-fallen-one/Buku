package com.example.shivam97.buku;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Buku extends Application {

    public static DatabaseReference mDatabase;
    public static FirebaseAuth mAuth;
    @Override
    public void onCreate() {
        super.onCreate();
        mDatabase= FirebaseDatabase.getInstance().getReference();
        mAuth= FirebaseAuth.getInstance();
    }
}