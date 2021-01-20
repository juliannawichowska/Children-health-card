package com.example.children_health_card;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ChartsFragment extends Fragment  {
    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    LineChart  lineChart;
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData lineData;
    String mName;
    Button btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View v = inflater.inflate(R.layout.fragment_chart, container, false);
        lineChart = v.findViewById(R.id.linechart);
        mName = getActivity().getIntent().getStringExtra("childName");
        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        user = firebaseAuth.getCurrentUser();
        //referencja do ścieżki do tabeli 'Users'
        DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
        databaseReference = refe.child("Children").child(user.getUid()).child(mName).child("Measurements");

        retrieveData();
        return v;
    }
    /*private void insertData(){
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                retrieveData();
            }
        });

    }*/

    public void onResume(){
        super.onResume();
        // Set title bar
        ((HomeActivity) getActivity())
                .setActionBarTitle("Wykresy");

    }

    private void retrieveData(){
        databaseReference.addValueEventListener(new ValueEventListener()  {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Entry> dataVals = new ArrayList<Entry>();
                if(snapshot.hasChildren()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                      DataPoint dataPoint = ds.getValue(DataPoint.class);
                      dataVals.add(new Entry(dataPoint.getxValue(), dataPoint.getyValue()));
                        Log.v(TAG, "i am here  " + dataVals );
                    }

                    showChart(dataVals);

                }else{

                    lineChart.clear();
                    lineChart.invalidate();
                }
         }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
        private void showChart(ArrayList<Entry> dataVals){
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("DataSet 1");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();
        }

}
