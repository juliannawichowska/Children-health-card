package com.example.children_health_card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class MedicinesListFragment extends Fragment {

    TextView MedicineName, Dosage, Reason;
    RecyclerView recyclerView;
    MedicinesListAdapter medicinesListAdapter;
    List<MedicinesListModel> medicinesList;
    Context context;
    Button AddMedicineBtn;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medicines_list, container, false);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Children");

        MedicineName = v.findViewById(R.id.medicineNameTv);
        Dosage = v.findViewById(R.id.dosageTv);
        Reason = v.findViewById(R.id.reasonTv);
        AddMedicineBtn = v.findViewById(R.id.addMedicineBtn);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //zainicjowanie Recycler View
        recyclerView = v.findViewById(R.id.medicinesList_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //zainicjowanie listy użytkowników
        medicinesList = new ArrayList<>();

        final String namechild = getActivity().getIntent().getStringExtra("childName");

        //pobranie aktualnie zalogowanego użytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
        assert user != null;
        String email = user.getEmail();
        String uid = user.getUid();

        final String childName = getActivity().getIntent().getStringExtra("childName");

        //pobierz wszystkich użytkowników
        getAllMedicines(uid,  childName);

        AddMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addMedicine = new Intent(getContext(), AddMedicineActivity.class);
                addMedicine.putExtra("namechild", childName);
                startActivity(addMedicine);

            }
        });

        return v;
    }

    private void getAllMedicines(String uid, String childName) {
        //pobierz wszystkie daty
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Children/"+ uid +"/"+childName+"/Medicines");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicinesList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    MedicinesListModel medicinesListModel = ds.getValue(MedicinesListModel.class);
                    medicinesList.add(medicinesListModel);
                    medicinesListAdapter = new MedicinesListAdapter(context,medicinesList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(medicinesListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
