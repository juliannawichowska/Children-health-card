package com.example.children_health_card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllergyListFragment extends Fragment {

    TextView Allergens, Symptoms;
    RecyclerView recyclerView;
    AllergyListAdapter allergyListAdapter;
    List<AllergyListModel> allergyList;
    Context context;
    Button AddAllergyBtn;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_allergy_list, container, false);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Children");

        Allergens = v.findViewById(R.id.allergensTv);
        Symptoms = v.findViewById(R.id.symptomsTv);
        AddAllergyBtn = v.findViewById(R.id.addAllergyBtn);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //zainicjowanie Recycler View
        recyclerView = v.findViewById(R.id.allergyList_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //zainicjowanie listy użytkowników
        allergyList = new ArrayList<>();

        final String namechild = getActivity().getIntent().getStringExtra("childName");

        //pobranie aktualnie zalogowanego użytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
        assert user != null;
        String email = user.getEmail();
        String uid = user.getUid();

        final String childName = getActivity().getIntent().getStringExtra("childName");

        //pobierz wszystkich użytkowników
        getAllAllergies(uid,  childName);

        AddAllergyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addAllergy = new Intent(getContext(), AddAllergyActivity.class);
                addAllergy.putExtra("namechild", childName);
                startActivity(addAllergy);

            }
        });

        return v;
    }

    public void onResume(){
        super.onResume();
        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle("Lista alergii");

    }


    private void getAllAllergies(String uid, String childName) {
        //pobierz wszystkie alergie
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Children/"+ uid +"/"+childName+"/Allergies");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allergyList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    AllergyListModel allergyListModel = ds.getValue(AllergyListModel.class);
                    allergyList.add(allergyListModel);
                    allergyListAdapter = new AllergyListAdapter(context,allergyList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(allergyListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
