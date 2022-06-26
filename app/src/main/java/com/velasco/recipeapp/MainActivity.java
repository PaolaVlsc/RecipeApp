package com.velasco.recipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // STEP 6: Sundesh me xml
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if the user is not logged in
        //starting the login activity
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // STEP 9: Να ξεκινάει από εδώ
        replaceFragment(new CategoriesFragment()); // at the start of the app

        // STEP 7: sundesh
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        // STEP 8: on click navigate
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new CategoriesFragment());
                    break;
                case R.id.add:
                    replaceFragment(new AddFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }

    // STEP 10: Βοηθητική συνάρτηση
    // the main activity is repsonsible for the switches on fragments
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager(); // diaxeirisths
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction(); // sunallagh
        fragmentTransaction.replace(R.id.frameLayout, fragment); // pou tha ginei h sunallagh
        fragmentTransaction.commit(); // commit
    }
}