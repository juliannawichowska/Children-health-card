package com.example.children_health_card;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
     ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Button btn;
    String mName, mSurname;
    public String pickedChildName, pickedChildSurname, pickedChildPesel;

    TextView textView;
    ImageView avatar;

    //deklaracja instancji FirebaseAuth, FirebaseUser, FirebaseDatabase i FirebaseReference
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //inicjacja instancji FirebaseAuth i FirebaseDatabase
        firebaseAuth =  FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //pobranie aktualnie zalogowanego użytkownika
        user = firebaseAuth.getCurrentUser();

       Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawer_layout);
        btn = findViewById(R.id.button);
        navigationView=findViewById(R.id.nav_view);
        textView = (TextView)navigationView.getHeaderView(0).findViewById(R.id.navTxt);
        avatar = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.navImg);
        navigationView.setNavigationItemSelectedListener(this);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mName = getIntent().getStringExtra("childName");

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new CalendarFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_calendar);
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pickedChildName= null;
            } else {
                pickedChildName= extras.getString("childName");
            }
        } else {
            pickedChildName= (String) savedInstanceState.getSerializable("childName");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pickedChildSurname= null;
            } else {
                pickedChildSurname= extras.getString("childSurname");
            }
        } else {
            pickedChildSurname= (String) savedInstanceState.getSerializable("childSurname");
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                pickedChildPesel= null;
            } else {
                pickedChildPesel= extras.getString("childPesel");
            }
        } else {
            pickedChildPesel= (String) savedInstanceState.getSerializable("childPesel");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent childlist = new Intent(HomeActivity.this, ChildListActivity.class);
                startActivity(childlist);
            }
        });


        //odwołanie się referencją do zmiennej userType w bazie
        DatabaseReference refe = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = refe.child("Children").child(user.getUid()).child(mName);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                textView.setText(dataSnapshot.child("name").getValue().toString());

                //załadowanie zdjęcia profilowego z bazy danych
                try {
                    String img = dataSnapshot.child("imageURL").getValue().toString();
                    Picasso.get().load(img).into(avatar);
                } catch (Exception e){
                    Picasso.get().load(R.mipmap.ic_launcher_round).into(avatar);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.nav_calendar:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CalendarFragment()).commit();
                    break;
                case R.id.nav_charts:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChartsFragment()).commit();
                    break;
                case R.id.nav_visits_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VisitsListFragment()).commit();
                    break;
                case R.id.nav_allergy_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AllergyListFragment()).commit();
                    break;
                case R.id.nav_medicines_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MedicinesListFragment()).commit();
                    break;
                case R.id.nav_vaccination_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VaccinationListFragment()).commit();
                    break;
                case R.id.nav_operations_list:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OperationsListFragment()).commit();
                    break;
                case R.id.nav_contact:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactFragment()).commit();
                    break;
                case R.id.nav_edit:
                    Log.v("hbbbbbb",pickedChildName);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new EditDataFragment()).commit();
                    break;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}