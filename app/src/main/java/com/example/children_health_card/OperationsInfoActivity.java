package com.example.children_health_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OperationsInfoActivity extends AppCompatActivity {

    TextView OperationsDate, OperationsName, OperationsNote;
    String childName, allerg;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations_info);

        OperationsDate = findViewById(R.id.showOperationsDate);
        OperationsName = findViewById(R.id.showOperationsName);
        OperationsNote = findViewById(R.id.showOperationsNote);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

        getSupportActionBar().setTitle("Operacje");

        Intent intent = getIntent();
        final String noteee = intent.getStringExtra("note");

        final String childName = intent.getStringExtra("nameChild");

        Log.v("tal", noteee);


        //odwołanie się referencją do zmiennej type w bazie
        DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = refe.child("Children").child(user.getUid()).child(childName).child("Operations").child(noteee).child("note");

        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                OperationsNote.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = refe1.child("Children").child(user.getUid()).child(childName).child("Operations").child(noteee).child("doctor");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                OperationsName.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe5 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref6 = refe5.child("Children").child(user.getUid()).child(childName).child("Operations").child(noteee).child("date");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                OperationsDate.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }



}