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

public class AddAllergyActivity extends AppCompatActivity {
    Button sendAllergyInfo;
    EditText allergens, symptoms, note;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergy);

        sendAllergyInfo = findViewById(R.id.sendAllergyInfoBtn);
        symptoms = findViewById(R.id.addSymptoms);
        note = findViewById(R.id.addNote);
        allergens = findViewById(R.id.addAllergens);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        final String childName = intent.getStringExtra("namechild");

        sendAllergyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mAllergens = allergens.getText().toString();
                String mSymptoms = symptoms.getText().toString();
                String mNote = note.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (mAllergens.isEmpty()) {
                    allergens.setError("Proszę wprowadź imię dziecka..");
                    allergens.requestFocus();
                } else if (mSymptoms.isEmpty()) {
                    symptoms.setError("Proszę wprowadź nazwisko dziecka..");
                    symptoms.requestFocus();
                } else {
                    //pobranie aktualnie zalogowanego użytkownika
                    FirebaseUser user = mAuth.getCurrentUser();

                    //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("allergens", mAllergens);
                    hashMap.put("symptoms", mSymptoms);
                    hashMap.put("note", mNote);
                    hashMap.put("nameChild", childName);

                    //referencja do ścieżki do tabeli 'Users'
                    DatabaseReference reference1 = firebaseDatabase.getReference("Children/"+ uid +"/"+childName+"/Allergies");
                    //przesłanie hashMap z danymi użytkownika do bazy
                    reference1.child(mAllergens).setValue(hashMap);


                    Toast.makeText(AddAllergyActivity.this, "Dodano uczulenie dla dziecka o imieniu : " + childName, Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });

    }
}