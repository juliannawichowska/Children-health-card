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

public class AddContactsActivity extends AppCompatActivity {
    Button sendContactsInfo;
    EditText doctorType, doctorName, doctorNumber, doctorAddress;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        sendContactsInfo = findViewById(R.id.sendContactsInfoBtn);
        doctorType = findViewById(R.id.addContactsType);
        doctorName = findViewById(R.id.addContactsName);
        doctorNumber = findViewById(R.id.addContactsNumber);
        doctorAddress = findViewById(R.id.addContactsAddress);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        final String childName = intent.getStringExtra("namechild");

        sendContactsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mDoctorType = doctorType.getText().toString();
                String mDoctorName = doctorName.getText().toString();
                String mDoctorNumber = doctorNumber.getText().toString();
                String mDoctorAddress = doctorAddress.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (mDoctorNumber.isEmpty()) {
                    doctorNumber.setError("Proszę wprowadź numer dolekarza..");
                    doctorNumber.requestFocus();
                } else {
                    //pobranie aktualnie zalogowanego użytkownika
                    FirebaseUser user = mAuth.getCurrentUser();

                    //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("doctorType", mDoctorType);
                    hashMap.put("doctorName", mDoctorName);
                    hashMap.put("doctorNumber", mDoctorNumber);
                    hashMap.put("doctorAddress", mDoctorAddress);

                    //referencja do ścieżki do tabeli 'Users'
                    DatabaseReference reference1 = firebaseDatabase.getReference("Children/"+ uid +"/"+"Contacts");
                    //przesłanie hashMap z danymi użytkownika do bazy
                    reference1.child(mDoctorName).setValue(hashMap);


                    Toast.makeText(AddContactsActivity.this, "Dodano nowy kontakt do lekarza", Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });

    }
}