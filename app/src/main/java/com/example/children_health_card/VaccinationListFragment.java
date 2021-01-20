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

public class VaccinationListFragment extends Fragment {

    TextView VaccinationType;
    RecyclerView recyclerView;
    VaccinationListAdapter vaccinationListAdapter;
    List<VaccinationListModel> vaccinationList;
    Context context;
    Button AddVaccinationBtn;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_vaccination_list, container, false);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Children");

        VaccinationType = v.findViewById(R.id.vaccinationTypeTv);
        AddVaccinationBtn = v.findViewById(R.id.addVaccinationBtn);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //zainicjowanie Recycler View
        recyclerView = v.findViewById(R.id.vaccinationList_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //zainicjowanie listy użytkowników
        vaccinationList = new ArrayList<>();

        final String namechild = getActivity().getIntent().getStringExtra("childName");

        //pobranie aktualnie zalogowanego użytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
        assert user != null;
        String email = user.getEmail();
        String uid = user.getUid();

        final String childName = getActivity().getIntent().getStringExtra("childName");

        //pobierz wszystkich użytkowników
        getAllVaccinations(uid,  childName);

        AddVaccinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addVaccination = new Intent(getContext(), AddVaccinationActivity.class);
                addVaccination.putExtra("namechild", childName);
                startActivity(addVaccination);

            }
        });

        return v;
    }

    public void onResume(){
        super.onResume();
        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle("Lista szczepień");

    }

    private void getAllVaccinations(String uid, String childName) {
        //pobierz wszystkie szczepienia
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Children/"+ uid +"/"+childName+"/Vaccinations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vaccinationList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    VaccinationListModel vaccinationListModel = ds.getValue(VaccinationListModel.class);
                    vaccinationList.add(vaccinationListModel);
                    vaccinationListAdapter = new VaccinationListAdapter(context,vaccinationList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(vaccinationListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}