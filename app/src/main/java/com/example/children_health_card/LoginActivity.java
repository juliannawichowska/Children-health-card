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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailTxt, passwordTxt;
    Button loginBtn, registerBtn;
    Context context;

    //deklaracja instancji FirebaseAuth i FirebaseAuth.AuthStateListener
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailTxt = findViewById(R.id.email);
        passwordTxt = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        //inicjacja instancji FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Logowanie");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //pobranie aktualnie zalogowanego użytkownika
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                if (mFirebaseUser != null) {
                    //Użytkownik jest już zalogowany
                    Toast.makeText(LoginActivity.this, "Jesteś zalogowany", Toast.LENGTH_SHORT);

                    //otworzenie aplikacji po stronie pacjenta
                    Intent childList = new Intent(LoginActivity.this, ChildListActivity.class);
                    startActivity(childList);
                } else {
                    //Użytkownik nie jest zologowany, pozostaje na stronie w celu logowania
                    Toast.makeText(LoginActivity.this, "Zaloguj się", Toast.LENGTH_SHORT).show();
                }
            }
        };

        //kliknięcie tego przycisku spowoduje przekierowanie użytkownika do strony na której może się zarejestrować
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        //kliknięcie tego przycisku spowoduje rozpoczęcie procesu logowania
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* zapisanie do zmiennych wartości z pól tekstowych
                    - adres e-mail
                    - hasło
                 */
                String email = emailTxt.getText().toString();
                String password = passwordTxt.getText().toString();

                //sprawdzanie, czy pola są uzupełnione
                if (email.isEmpty()) {
                    emailTxt.setError("Proszę wprowadź email..");
                    emailTxt.requestFocus();
                } else if (password.isEmpty()) {
                    passwordTxt.setError("Proszę wprowadź hasło..");
                    passwordTxt.requestFocus();
                } else {
                    //odwołanie się do instancji FirebaseAuth i logowanie użytkownika
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @SuppressLint("ShowToast")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                //logowanie nie powiodło się
                                Toast.makeText(LoginActivity.this, "Logowanie nie powiodło się, proszę spróbuj ponownie", Toast.LENGTH_SHORT);
                            } else {
                                //logowanie powiodło się
                                Intent childList = new Intent(LoginActivity.this, ChildListActivity.class);
                                startActivity(childList);
                            }

                        }
                    });

                }
            }
        });
    }


}