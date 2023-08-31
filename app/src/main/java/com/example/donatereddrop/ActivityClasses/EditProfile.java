package com.example.donatereddrop.ActivityClasses;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.donatereddrop.Models.SignupModel;
import com.example.donatereddrop.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {
    TextInputEditText Nameedit, bloodgroupedit, phoneedit, emailedit,streetedit,adressedit;
    CircleImageView circleImageView;
    ImageView backedit,editit;
    Uri filepath;
    SignupModel signupModel;
    DatabaseReference databaseReference;
    AutoCompleteTextView autoCompleteTextView;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Nameedit = findViewById(R.id.nameedit);
        bloodgroupedit = findViewById(R.id.bloodgrouedit);
        emailedit = findViewById(R.id.emailedit);
        circleImageView = findViewById(R.id.profilepicedit);
        adressedit = findViewById(R.id.adressedit);
        phoneedit = findViewById(R.id.phonenumberedit);
        streetedit = findViewById(R.id.streetedit);
        backedit=findViewById(R.id.backedit);
        update=findViewById(R.id.update);
        editit=findViewById(R.id.editimage);

        databaseReference= FirebaseDatabase.getInstance().getReference();
        signupModel=new SignupModel();
        autoCompleteTextView=findViewById(R.id.AutoCompleteTextviewedit);

        backedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        if (getIntent().getSerializableExtra("name1") != null) {
            signupModel = (SignupModel) getIntent().getSerializableExtra("name1");
            Log.d("dhjjsassf", signupModel.toString());
            Nameedit.setText(signupModel.getName());
            bloodgroupedit.setText(signupModel.getBgroup());
            emailedit.setText(signupModel.getEmaiil());
            adressedit.setText(signupModel.getAddresmodel());
            phoneedit.setText(signupModel.getPhone());
            streetedit.setText(signupModel.getStreet());
            autoCompleteTextView.setText(signupModel.getGender());

            if (signupModel.getPurl() != null) {
                Glide.with(this).load(signupModel.getPurl()).into(circleImageView);
            }
        }




        String[] Subjects = new String[]{"Male","Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.genderselect, Subjects);
        autoCompleteTextView.setAdapter(adapter);



        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            filepath = data.getData();
                            circleImageView.setImageURI(filepath);

                            //Log.d("imageee",filepath.getPath());


                        } else {
                            Toast.makeText(EditProfile.this, "Image not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        editit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("name",Nameedit.getText().toString());
                hashMap.put("bgroup",bloodgroupedit.getText().toString());
                hashMap.put("emaiil",emailedit.getText().toString());
                hashMap.put("phone",phoneedit.getText().toString());
                hashMap.put("addresmodel",adressedit.getText().toString());
                hashMap.put("street",streetedit.getText().toString());
                hashMap.put("gender",autoCompleteTextView.getText().toString());
//                hashMap.put("purl",filepath);

                databaseReference.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap);
                Toast.makeText(EditProfile.this, "Update successfully", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(EditProfile.this, Home.class);
                startActivity(intent);
                finish();

            }
        });
    }
}