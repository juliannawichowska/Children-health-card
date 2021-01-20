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

public class OperationsListFragment extends Fragment {

    TextView OperationsDate, OperationsName, OperationsNote;
    RecyclerView recyclerView;
    OperationsListAdapter operationsListAdapter;
    List<OperationsListModel> operationsList;
    Context context;
    Button AddOperationsBtn;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_operations_list, container, false);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //referencja do ścieżki do tabeli 'Users'
        databaseReference = firebaseDatabase.getReference("Children");

        OperationsName = v.findViewById(R.id.operationNoteTv);
        OperationsDate = v.findViewById(R.id.operationDateTv);
        AddOperationsBtn = v.findViewById(R.id.addOperationsBtn);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        //zainicjowanie Recycler View
        recyclerView = v.findViewById(R.id.operationsList_RecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //zainicjowanie listy użytkowników
        operationsList = new ArrayList<>();

        //pobranie aktualnie zalogowanego użytkownika
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
        assert user != null;
        String email = user.getEmail();
        String uid = user.getUid();

        final String childName = getActivity().getIntent().getStringExtra("childName");

        //pobierz wszystkich użytkowników
        getAllOperations(uid,  childName);

        AddOperationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addOperation = new Intent(getContext(), AddOperationsActivity.class);
                addOperation.putExtra("namechild", childName);
                startActivity(addOperation);

            }
        });

        return v;
    }

    public void onResume(){
        super.onResume();
        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle("Lista operacji");

    }

    private void getAllOperations(String uid, String childName) {
        //pobierz wszystkie alergie
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Children/"+ uid +"/"+childName+"/Operations");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                operationsList.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()) {
                    OperationsListModel operationsListModel = ds.getValue(OperationsListModel.class);
                    operationsList.add(operationsListModel);
                    operationsListAdapter = new OperationsListAdapter(context,operationsList);
                    //set adapter to recycler view
                    recyclerView.setAdapter(operationsListAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}



