package com.example.donatereddrop.ActivityClasses;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.donatereddrop.Models.SignupModel;
import com.example.donatereddrop.R;
import com.example.donatereddrop.databinding.ActivityHomeBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    DatabaseReference databaseReference;
    SignupModel signupModel1;
    FirebaseUser user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        signupModel1=new SignupModel();
        user = auth.getCurrentUser();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);



        if (user == null) {
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
        }

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        profileimage();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
               R.id.home_frament, R.id.profile_fragment, R.id.recentChats_fragment, R.id.logout_fragment)
                .setOpenableLayout(drawer)
                .build();

        binding.appBarHome.profileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the profile fragment
               navController.navigate(R.id.profile_fragment);
                binding.drawerLayout.closeDrawers();

            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void profileimage(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                signupModel1 = snapshot.getValue(SignupModel.class);

                Log.d("debuhhjss",snapshot.toString());



                if (signupModel1 != null) {
//                     Set the data to respective TextViews
                    //  Load the profile picture using Glide
                    Glide.with(Home.this).load(signupModel1.getPurl()).into(binding.appBarHome.profileimage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}