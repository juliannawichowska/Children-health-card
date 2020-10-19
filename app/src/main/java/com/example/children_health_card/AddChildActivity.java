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

public class AddChildActivity extends AppCompatActivity {

    Button sendInfo;
    EditText name, surname, weight, height, pesel, date;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        sendInfo = findViewById(R.id.sendChildInfoBtn);
        name = findViewById(R.id.child_name);
        surname = findViewById(R.id.child_surname);
        weight = findViewById(R.id.child_weight);
        height = findViewById(R.id.child_height);
        date = findViewById(R.id.child_date);
        pesel = findViewById(R.id.child_pesel);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        //inicjacja instancji FirebaseDatabase
        firebaseDatabase = FirebaseDatabase.getInstance();

        sendInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mName = name.getText().toString();
                String mSurname = surname.getText().toString();
                String mWeight = weight.getText().toString();
                String mHeight = height.getText().toString();
                String mPesel = pesel.getText().toString();
                String mDate = date.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (mName.isEmpty()) {
                    name.setError("Proszę wprowadź imię dziecka..");
                    name.requestFocus();
                } else if (mSurname.isEmpty()) {
                    surname.setError("Proszę wprowadź nazwisko dziecka..");
                    surname.requestFocus();
                } else if (mPesel.isEmpty()) {
                    pesel.setError("Proszę wprowadź pesel dziecka..");
                    pesel.requestFocus();
                } else {
                    //pobranie aktualnie zalogowanego użytkownika
                    FirebaseUser user = mAuth.getCurrentUser();

                    //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                    assert user != null;
                    String email = user.getEmail();
                    String uid = user.getUid();

                    //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("name", mName);
                    hashMap.put("surname", mSurname);
                    hashMap.put("weight", mWeight);
                    hashMap.put("height", mHeight);
                    hashMap.put("date", mDate);
                    hashMap.put("pesel", mPesel);

                    //referencja do ścieżki do tabeli 'Users'
                    DatabaseReference reference1 = firebaseDatabase.getReference("Children/" + uid);
                    //przesłanie hashMap z danymi użytkownika do bazy
                    reference1.child(mName).setValue(hashMap);


                    Toast.makeText(AddChildActivity.this, "Dodano dziecko o imieniu : " + mName, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddChildActivity.this, ChildListActivity.class));
                    finish();
                }
            }
        });

    }
}