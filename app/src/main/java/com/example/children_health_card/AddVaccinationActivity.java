package com.example.children_health_card;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddVaccinationActivity extends AppCompatActivity {

    Button sendVaccationInfo;
    EditText addVaccinationType, addVaccinationDate, addVaccinationNote, addVaccinationName;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vaccination);

        sendVaccationInfo = findViewById(R.id.sendVaccinationInfoBtn);
        addVaccinationType = findViewById(R.id.addVaccinationType);
        addVaccinationName = findViewById(R.id.addVaccinationName);
        addVaccinationDate = findViewById(R.id.addVaccinationDate);
        addVaccinationNote = findViewById(R.id.addVaccinationNote);

        getSupportActionBar().setTitle("Dodawanie szczepienia");

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        final String childName = intent.getStringExtra("namechild");

        sendVaccationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mVaccinationType = addVaccinationType.getText().toString();
                String mVaccinationName = addVaccinationName.getText().toString();
                String mVaccinationDate= addVaccinationDate.getText().toString();
                String mVaccinationNote =  addVaccinationNote.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (mVaccinationType.isEmpty()) {
                    addVaccinationType.setError("Proszę wprowadź rodzaj szczepienia..");
                    addVaccinationType.requestFocus();
                } else {
                    //pobranie aktualnie zalogowanego użytkownika
                    FirebaseUser user = mAuth.getCurrentUser();

                    //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("vaccinationType", mVaccinationType);
                    hashMap.put("vaccinationName", mVaccinationName);
                    hashMap.put("vaccinationDate", mVaccinationDate);
                    hashMap.put("note", mVaccinationNote);
                    hashMap.put("nameChild", childName);

                    //referencja do ścieżki do tabeli 'Vaccations'
                    DatabaseReference reference1 = firebaseDatabase.getReference("Children/"+ uid +"/"+childName+"/Vaccinations");
                    //przesłanie hashMap z danymi użytkownika do bazy
                    reference1.child(mVaccinationType).setValue(hashMap);


                    Toast.makeText(AddVaccinationActivity.this, "Dodano szczepienie dla dziecka o imieniu : " + childName, Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });

    }
}