package com.example.grantha;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragments.homeFragment;
import Fragments.notificationFragment;
import Fragments.profileFragment;
import Fragments.searchFragment;

public class home extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Fragment selectorFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        selectorFragment=new homeFragment();
                        break;
                    case R.id.nav_fav:
                        selectorFragment=new notificationFragment();
                        break;
                    case R.id.nav_add:
                        selectorFragment=null;
                        startActivity(new Intent(home.this,postActivity.class));
                        break;
                    case R.id.nav_search:
                        selectorFragment=new searchFragment();
                        break;
                    case R.id.nav_person:
                        selectorFragment=new profileFragment();
                        break;
                }
                if (selectorFragment!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectorFragment).commit();

                }
                return true;

            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new profileFragment()).commit();

    }
}