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

public class VisitsInfoActivity extends AppCompatActivity {

    TextView VisitsDate, VisitsName, VisitsType, VisitsNote, VisitsNoteDialog;
    String childName, allerg;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_info);

        VisitsDate = findViewById(R.id.showVisitDate);
        VisitsName = findViewById(R.id.showVisitName);
        VisitsType = findViewById(R.id.showVisitType);
        VisitsNote = findViewById(R.id.dodnot);
        VisitsNoteDialog = findViewById(R.id.not);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        final String typeee = intent.getStringExtra("type");

        final String childName = intent.getStringExtra("nameChild");

        Log.v("tal", typeee);

        getSupportActionBar().setTitle("Wizyty");


        //odwołanie się referencją do zmiennej type w bazie
        DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = refe.child("Children").child(user.getUid()).child(childName).child("Visits").child(typeee).child("date");

        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                VisitsDate.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = refe1.child("Children").child(user.getUid()).child(childName).child("Visits").child(typeee).child("name");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                VisitsName.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej note w bazie
        DatabaseReference refe3 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref4 = refe3.child("Children").child(user.getUid()).child(childName).child("Visits").child(typeee).child("note");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref4.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                VisitsNoteDialog.setText(allll);
                if(allll.isEmpty()){
                    VisitsNote.setText("");
                    VisitsNoteDialog.setText("");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe5 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref6 = refe5.child("Children").child(user.getUid()).child(childName).child("Visits").child(typeee).child("type");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                VisitsType.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }



}