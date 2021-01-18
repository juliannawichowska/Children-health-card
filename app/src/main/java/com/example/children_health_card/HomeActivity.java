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

import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
     ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    Button btn;
    public String pickedChildName, pickedChildSurname, pickedChildPesel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawer_layout);
        btn = findViewById(R.id.button);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

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