package com.example.children_health_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class ContactsInfoActivity extends AppCompatActivity {

    TextView ContactsType, ContactsName, ContactsNumber, ContactsAddress;
    String childName, allerg;
    Button callBtn;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_info);

        ContactsType = findViewById(R.id.showContactsType);
        ContactsName = findViewById(R.id.showContactsName);
        ContactsNumber = findViewById(R.id.showContactsNumber);
        ContactsAddress = findViewById(R.id.showContactsAddress);
        callBtn = findViewById(R.id.CallBtn);


        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("Kontakty");

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        final String doctorName = intent.getStringExtra("doctorName");

        final String childName = intent.getStringExtra("nameChild");

        Log.v("tal", doctorName);


        //odwołanie się referencją do zmiennej type w bazie
        DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = refe.child("Children").child(user.getUid()).child("Contacts").child(doctorName).child("doctorName");

        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                ContactsName.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = refe1.child("Children").child(user.getUid()).child("Contacts").child(doctorName).child("doctorType");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
               ContactsType.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe5 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref6 = refe5.child("Children").child(user.getUid()).child("Contacts").child(doctorName).child("doctorNumber");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref6.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                ContactsNumber.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        //odwołanie się referencją do zmiennej symptoms w bazie
        DatabaseReference refe7 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref8 = refe7.child("Children").child(user.getUid()).child("Contacts").child(doctorName).child("doctorAddress");


        //wyczytanie za pomocą referencji wartości zmiennej w bazie
        ref8.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String allll = dataSnapshot.getValue(String.class);
                ContactsAddress.setText(allll);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //odwołanie się referencją do zmiennej symptoms w bazie
                DatabaseReference refes = FirebaseDatabase.getInstance().getReference();
                DatabaseReference refss = refes.child("Children").child(user.getUid()).child("Contacts").child(doctorName).child("doctorNumber");


                //wyczytanie za pomocą referencji wartości zmiennej w bazie
                refss.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String phone = dataSnapshot.getValue(String.class);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        System.out.println("The read failed: " + databaseError.getCode());
                    }
                });
            }
        });


    }



}