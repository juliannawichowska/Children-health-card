package com.example.children_health_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicinesInfoActivity extends AppCompatActivity {

    TextView medicineName, dosage, reason, name, note, extraInfo;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);

        medicineName = findViewById(R.id.showMedicineName);
        dosage = findViewById(R.id.showDosage);
        reason = findViewById(R.id.showReason);
        note = findViewById(R.id.showNote);
        extraInfo = findViewById(R.id.textNote);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        final String medname = intent.getStringExtra("medicinename");
        final String childName = intent.getStringExtra("nameChild");

        Log.v("jj", user.getUid());
        Log.v("jj", medname);
        Log.v("jj", childName);


        //odwołanie się referencją do zmiennej allergens w bazie
        DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = refe.child("Children").child(user.getUid()).child(childName).child("Medicines").child(medname).child("medicineName");

        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                medicineName.setText(data);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = refe1.child("Children").child(user.getUid()).child(childName).child("Medicines").child(medname).child("dosage");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                dosage.setText(data);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej note w bazie
        DatabaseReference refe3 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref4 = refe3.child("Children").child(user.getUid()).child(childName).child("Medicines").child(medname).child("reason");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);
                reason.setText(data);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej note w bazie
        DatabaseReference refe5 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref6 = refe5.child("Children").child(user.getUid()).child(childName).child("Medicines").child(medname).child("note");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = dataSnapshot.getValue(String.class);

                Log.v("jj", user.getUid());
                Log.v("jj", medname);
                Log.v("jj", childName);
                Log.v("jj", data);

                note.setText(data);
                if(data.isEmpty()){
                    note.setText("");
                    extraInfo.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

}