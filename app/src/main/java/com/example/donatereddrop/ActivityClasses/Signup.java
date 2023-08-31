package com.example.donatereddrop.ActivityClasses;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.donatereddrop.Models.SignupModel;
import com.example.donatereddrop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {

    TextInputLayout Name, bloodgroup, phone, email, pass, adrs;
    CircleImageView circleImageView;
    StorageReference storageReference;
    Uri filepath;
    AutoCompleteTextView autoCompleteTextView;
   RadioGroup radioGroup;
    TextView textView;
    ProgressBar progressBar;
    String selectedRadioButtonText;
    Button button;
 FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Name = findViewById(R.id.name);
        bloodgroup = findViewById(R.id.bloodgroup);
        email = findViewById(R.id.email);
        circleImageView = findViewById(R.id.imageurlget);
        adrs = findViewById(R.id.addressss);
        phone = findViewById(R.id.phonenumer);
        textView = findViewById(R.id.clickforlogin);
        pass = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);
        button = findViewById(R.id.signup);
        radioGroup=findViewById(R.id.radiogroup);

        auth=FirebaseAuth.getInstance();
        autoCompleteTextView = findViewById(R.id.AutoCompleteTextview);
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
                            Toast.makeText(Signup.this, "Image not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });



//for drop down list



        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = findViewById(checkedId);
                if (selectedRadioButton != null) {
                    selectedRadioButtonText = selectedRadioButton.getText().toString();

                }
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              //  String id = auth.getUid();
                String email1 = email.getEditText().getText().toString();
                String password = pass.getEditText().getText().toString();
                String phonenumer = phone.getEditText().getText().toString();
                String addreses = adrs.getEditText().getText().toString();
                String name = Name.getEditText().getText().toString();
                String blod = autoCompleteTextView.getText().toString();


                if (TextUtils.isEmpty(email1)) {
                    Toast.makeText(Signup.this, "Enter Email", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(addreses)) {
                    Toast.makeText(Signup.this, "Enter Eddress", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Signup.this, "Enter Password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(phonenumer)) {
                    Toast.makeText(Signup.this, "Enter phone number ", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Signup.this, "Enter Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(blod)) {
                    Toast.makeText(Signup.this, "Please Enter blood group", Toast.LENGTH_SHORT).show();
                } else if (selectedRadioButtonText.isEmpty()) {
                    Toast.makeText(Signup.this, "Please select are you donner OR looking for a donner ", Toast.LENGTH_SHORT).show();

                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email1, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        String id = FirebaseAuth.getInstance().getUid();
                                        storageReference = FirebaseStorage.getInstance().getReference(id + ".png");
                                        storageReference.child(filepath.getLastPathSegment());
                                        storageReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {


                                                        Log.d("debugddd", uri.toString());

                                                        SignupModel signupModel = new SignupModel(id,uri.toString(), name, addreses, blod,selectedRadioButtonText, phonenumer, email1, password,"","");

                                                        FirebaseDatabase.getInstance().getReference("User")
                                                                .child(id)
                                                                .setValue(signupModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        Toast.makeText(Signup.this, "Registered Successfully.",
                                                                                Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.d("hgausg",e.toString());
                                                                        Toast.makeText(Signup.this, e.getMessage(),
                                                                                Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });


                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(Signup.this, "Registered failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }

        });
    }
}
