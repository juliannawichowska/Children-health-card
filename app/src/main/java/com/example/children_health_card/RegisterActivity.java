package com.example.children_health_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText emailTxt, passwordTxt;
    Button registerBtnn;

    //deklaracja instancji FirebaseAuth i FirebaseDatabase
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailTxt = findViewById(R.id.email);
        passwordTxt = findViewById(R.id.password);
        registerBtnn = findViewById(R.id.registerBtnn);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Rejestracja");

        //reakcja na kliknięciu przycisku "Zarejestruj się"
        registerBtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* zapisanie do zmiennych wartości z pól tekstowych
                    - adres e-mail
                    - hasło
                    - imię
                    - nazwisko
                    - numer telefonu
                 */
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if(email.isEmpty()){
                    emailTxt.setError("Proszę wprowadź email..");
                    emailTxt.requestFocus();
                }
                else if(password.isEmpty()){
                    passwordTxt.setError("Proszę wprowadź hasło..");
                    passwordTxt.requestFocus();
                }else {
                    //odwołanie się do instancji FirebaseAuth i utworzenie nowego użytkownika
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                //rejestracja nie powiodła się
                                Toast.makeText(RegisterActivity.this, "Rejestracja nie powiodła się, proszę spróbuj ponownie", Toast.LENGTH_SHORT);
                            } else {
                                //rejestracja powiodła się, stworzony został nowy użytkownika w sekcji 'Authentication', należy jeszcze dodać użytkownika do tabeli w bazie danych

                                //pobranie aktualnie zalogowanego użytkownika
                                FirebaseUser user = mAuth.getCurrentUser();

                                //pobranie adresu e-mail i identyfikatora uid aktualnie zalogowanego użytkownika
                                assert user != null;
                                String email = user.getEmail();
                                String uid = user.getUid();

                                //utworzenie HashMap i uzupełnienie jej danymi nowego użytkownika
                                HashMap<Object, String> hashMap = new HashMap<>();
                                hashMap.put("child_name", "");

                                //inicjacja instancji FirebaseDatabase
                                firebaseDatabase = FirebaseDatabase.getInstance();

                                //referencja do ścieżki do tabeli 'Users'
                                DatabaseReference reference1 = firebaseDatabase.getReference("Children");
                                //przesłanie hashMap z danymi użytkownika do bazy
                                reference1.child(uid).setValue(hashMap);


                                 Toast.makeText(RegisterActivity.this, "Zalogowałeś się do aplikacji mailem : " + email, Toast.LENGTH_LONG).show();
                                 startActivity(new Intent(RegisterActivity.this, ChildListActivity.class));
                                 finish();
                                }

                        }
                    });
                }
            }
        });
    }


}
