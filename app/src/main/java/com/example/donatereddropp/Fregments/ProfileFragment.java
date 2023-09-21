package com.example.donatereddropp.Fregments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.donatereddropp.ActivityClasses.EditProfile;
import com.example.donatereddropp.Models.SignupModel;
import com.example.donatereddropp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView person;
    DatabaseReference databaseReference;

    SignupModel signupModel;
    int index;
    TextView blod, name1, ads, street, phonenm, email, genderofuser;
    LinearLayout edit;
    FirebaseAuth auth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        person = view.findViewById(R.id.profilepic);
        blod = view.findViewById(R.id.bloodgorupofuser);
        name1 = view.findViewById(R.id.nameofuser);
        street = view.findViewById(R.id.streetofuser);
        ads = view.findViewById(R.id.addressofuser);
        phonenm = view.findViewById(R.id.phoneofuser);
        email = view.findViewById(R.id.emailofuser);
        genderofuser=view.findViewById(R.id.genderofuser);
        edit=view.findViewById(R.id.edit);
        auth = FirebaseAuth.getInstance();
        signupModel = new SignupModel();
        readData();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), EditProfile.class);
                intent.putExtra("name1",signupModel);
                startActivity(intent);
            }
        });
        return view;
    }


    private void readData() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").child(auth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               
                signupModel = snapshot.getValue(SignupModel.class);

                Log.d("debuhhj",snapshot.toString());
                Log.d("debuhhj",snapshot.toString());


                if (signupModel != null) {
//                     Set the data to respective TextViews
                    blod.setText(signupModel.getBgroup());
                    name1.setText(signupModel.getName());
                    ads.setText(signupModel.getAddresmodel());
                    phonenm.setText(signupModel.getPhone());
                    email.setText(signupModel.getEmaiil());
                    street.setText(signupModel.getStreet());
                    genderofuser.setText(signupModel.getGender());


                   //  Load the profile picture using Glide
                    Glide.with(requireContext()).load(signupModel.getPurl()).into(person);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
