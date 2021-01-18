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

public class AddOperationsActivity extends AppCompatActivity {
    Button sendOperationsInfo;
    EditText operationsName, operationsDate, operationsNote;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operations);

        sendOperationsInfo = findViewById(R.id.sendOperationsInfoBtn);
        operationsDate = findViewById(R.id.addOperationsDate);
        operationsName = findViewById(R.id.addOperationsName);
        operationsNote = findViewById(R.id.addOperationsNote);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        final String childName = intent.getStringExtra("namechild");

        sendOperationsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mOperationsDate = operationsDate.getText().toString();
                String mOperationsName = operationsName.getText().toString();
                String mOperationsNote = operationsNote.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (mOperationsNote.isEmpty()) {
                    operationsDate.setError("Proszę wprowadź typ operacji..");
                    operationsDate.requestFocus();
                } else {
                    //pobranie aktualnie zalogowanego użytkownika
                    FirebaseUser user = mAuth.getCurrentUser();

                    //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("doctor", mOperationsName);
                    hashMap.put("date", mOperationsDate);
                    hashMap.put("note", mOperationsNote);
                    hashMap.put("nameChild", childName);

                    //referencja do ścieżki do tabeli 'Users'
                    DatabaseReference reference1 = firebaseDatabase.getReference("Children/"+ uid +"/"+childName+"/Operations");
                    //przesłanie hashMap z danymi użytkownika do bazy
                    reference1.child(mOperationsNote).setValue(hashMap);


                    Toast.makeText(AddOperationsActivity.this, "Dodano operację dla dziecka o imieniu : " + childName, Toast.LENGTH_LONG).show();

                    finish();
                }
            }
        });

    }
}