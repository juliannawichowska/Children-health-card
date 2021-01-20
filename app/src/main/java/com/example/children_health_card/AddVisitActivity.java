package com.example.children_health_card;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddVisitActivity extends AppCompatActivity {
    Button sendVisitsInfo;
    EditText visitName, visitDate, visitType, visitNote;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit);

        sendVisitsInfo = findViewById(R.id.sendVisitsInfoBtn);
        visitName = findViewById(R.id.addVisitsName);
        visitDate = findViewById(R.id.addVisitsDate);
        visitType = findViewById(R.id.addVisitsType);
        visitNote = findViewById(R.id.addVisitsNote);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("Dodawanie wizyty");

        Intent intent = getIntent();
        final String childName = intent.getStringExtra("namechild");

        sendVisitsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mVisitType = visitType.getText().toString();
                String mVisitName = visitName.getText().toString();
                String mVisitDate = visitDate.getText().toString();
                String mNote = visitNote.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (mVisitDate.isEmpty()) {
                    visitDate.setError("Proszę wprowadź datę wizyty..");
                    visitDate.requestFocus();
                } else if (mVisitName.isEmpty()) {
                    visitName.setError("Proszę wprowadź nazwisko lekarza..");
                    visitName.requestFocus();
                } else {
                    //pobranie aktualnie zalogowanego użytkownika
                    FirebaseUser user = mAuth.getCurrentUser();

                    //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("name", mVisitName);
                    hashMap.put("date", mVisitDate);
                    hashMap.put("note", mNote);
                    hashMap.put("type", mVisitType);
                    hashMap.put("nameChild", childName);

                    //referencja do ścieżki do tabeli 'Users'
                    DatabaseReference reference1 = firebaseDatabase.getReference("Children/"+ uid +"/"+childName+"/Visits");
                    //przesłanie hashMap z danymi użytkownika do bazy
                    reference1.child(mVisitType).setValue(hashMap);


                    Toast.makeText(AddVisitActivity.this, "Dodano wizytę dla dziecka o imieniu : " + childName, Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });

    }
}