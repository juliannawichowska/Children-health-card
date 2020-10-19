package com.example.children_health_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChildListActivity extends AppCompatActivity {

    ImageView avatar;
    TextView name, pesel;
    Button addChild;
    RecyclerView recyclerView;
    ChildListAdapter childListAdapter;
    List<ChildListModel> childList;
    Context context;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    //dres url zdjęcia profilowego
    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Children");

        avatar = findViewById(R.id.profilePicture);
        name = findViewById(R.id.nameTv);
        pesel = findViewById(R.id.peselTv);
        addChild = findViewById(R.id.addChildBtn);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //zainicjowanie Recycler View
        recyclerView = findViewById(R.id.childList_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //zainicjowanie listy użytkowników
        childList = new ArrayList<>();

        //pobranie aktualnie zalogowanego użytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
        assert user != null;
        String email = user.getEmail();
        String uid = user.getUid();
        
        //pobierz wszystkich użytkowników
        getAllChildren(uid);
    }

    private void getAllChildren(String uid) {
        //pobierz wszystkie daty
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Children/"+ uid);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                childList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    ChildListModel childListModel = ds.getValue(ChildListModel.class);
                    childList.add(childListModel);
                    childListAdapter = new ChildListAdapter(context,childList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(childListAdapter);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

