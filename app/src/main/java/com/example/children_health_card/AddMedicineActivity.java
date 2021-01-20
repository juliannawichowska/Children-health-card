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

public class AddMedicineActivity extends AppCompatActivity {

    Button sendMedicineInfo;
    EditText medicineName, reason, dosage, note;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        sendMedicineInfo = findViewById(R.id.sendMedicineInfoBtn);
        medicineName = findViewById(R.id.addMedicineName);
        note = findViewById(R.id.addMedicineNote);
        reason = findViewById(R.id.addReason);
        dosage = findViewById(R.id.addDosage);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("Dodawanie leku");

        Intent intent = getIntent();
        final String childName = intent.getStringExtra("namechild");

        sendMedicineInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mMedicineName = medicineName.getText().toString();
                String mReason = reason.getText().toString();
                String mDosage = dosage.getText().toString();
                String mNote = note.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (mMedicineName.isEmpty()) {
                    medicineName.setError("Proszę wprowadź nazwę leku..");
                    medicineName.requestFocus();
                } else {
                    //pobranie aktualnie zalogowanego użytkownika
                    FirebaseUser user = mAuth.getCurrentUser();

                    //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("medicineName", mMedicineName);
                    hashMap.put("reason", mReason);
                    hashMap.put("dosage", mDosage);
                    hashMap.put("note", mNote);
                    hashMap.put("nameChild", childName);

                    //referencja do ścieżki do tabeli 'Users'
                    DatabaseReference reference1 = firebaseDatabase.getReference("Children/"+ uid +"/"+childName+"/Medicines");
                    //przesłanie hashMap z danymi użytkownika do bazy
                    reference1.child(mMedicineName).setValue(hashMap);


                    Toast.makeText(AddMedicineActivity.this, "Dodano lek dla dziecka o imieniu : " + childName, Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });

    }
}