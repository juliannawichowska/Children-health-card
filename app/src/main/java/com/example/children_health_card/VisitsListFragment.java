package com.example.children_health_card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class VisitsListFragment extends Fragment {

    TextView VisitName, VisitDate;
    RecyclerView recyclerView;
    VisitsListAdapter visitsListAdapter;
    List<VisitsListModel> visitsList;
    Context context;
    Button AddVisitBtn;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_visits_list, container, false);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Children");

        VisitName = v.findViewById(R.id.visitNameTv);
        VisitDate = v.findViewById(R.id.visitDateTv);
        AddVisitBtn = v.findViewById(R.id.addVisitBtn);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //zainicjowanie Recycler View
        recyclerView = v.findViewById(R.id.visitsList_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //zainicjowanie listy użytkowników
        visitsList = new ArrayList<>();

        //pobranie aktualnie zalogowanego użytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
        assert user != null;
        String email = user.getEmail();
        String uid = user.getUid();

        final String childName = getActivity().getIntent().getStringExtra("childName");

        //pobierz wszystkich użytkowników
        getAllVisits(uid,  childName);

        AddVisitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addVisit = new Intent(getContext(), AddVisitActivity.class);
                addVisit.putExtra("namechild", childName);
                startActivity(addVisit);

            }
        });

        return v;
    }

    public void onResume(){
        super.onResume();
        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle("Lista wizyt");

    }

    private void getAllVisits(String uid, String childName) {
        //pobierz wszystkie alergie
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Children/"+ uid +"/"+childName+"/Visits");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                visitsList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    VisitsListModel visitsListModel = ds.getValue(VisitsListModel.class);
                    visitsList.add(visitsListModel);
                    visitsListAdapter = new VisitsListAdapter(context,visitsList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(visitsListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



