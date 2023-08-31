package com.example.donatereddrop.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.donatereddrop.ActivityClasses.Chat;
import com.example.donatereddrop.Models.ChatModel;
import com.example.donatereddrop.Models.SignupModel;
import com.example.donatereddrop.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

public class Image_Activity extends AppCompatActivity {
    ImageView imageView,crop;
    ImageButton button;
    SignupModel model;
    CropImageView cropImageView;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    ChatModel roomModel;
    Uri imageUri;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_image);
        cropImageView=findViewById(R.id.cropImageView);
        crop=findViewById(R.id.crop);
        imageView=findViewById(R.id.imageset);
        button=findViewById(R.id.sendimagebtn);
        model = new SignupModel();
        roomModel=new ChatModel();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Chats");

        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UCrop.of(imageUri, imageUri)
                        .withAspectRatio(16, 9)
                        .withMaxResultSize(300, 300)
                        .start(Image_Activity.this);
            }
        });



        if (getIntent().getSerializableExtra("image") != null) {
            String imageUriString = getIntent().getStringExtra("image");
            imageUri = Uri.parse(imageUriString);
            Glide.with(this).load(imageUri).into(imageView);

        }
        if (getIntent().getSerializableExtra("imagecemra") != null) {
            String imageUriString = getIntent().getStringExtra("imagecemra");
            imageUri = Uri.parse(imageUriString);
            Glide.with(this).load(imageUri).into(imageView);
        }
        if (getIntent().getSerializableExtra("image2") != null)
        {
            model = (SignupModel) getIntent().getSerializableExtra("image2");
            Log.d("jasjjs",model.toString());
        }

        databaseReference = databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(model.getId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String purlofa=model.getPurl();
                    String messageID = databaseReference.push().getKey();
                    String cruntuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String receivrrID=model.getId();


                    Log.d("jsjdsja",receivrrID);


                    storageReference = FirebaseStorage.getInstance().getReference("SentItemsSaved");
                    storageReference.child(imageUri.getLastPathSegment());
                            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("jhsuhsjsj",uri.toString());
                                            ChatModel roomModel = new ChatModel("", cruntuser, System.currentTimeMillis(), receivrrID,messageID,purlofa,uri.toString());
                                            databaseReference.child(messageID).setValue(roomModel);
                                            DatabaseReference otheruser =database.getReference("Chats")
                                                    .child(receivrrID).child(cruntuser);
                                            Log.d("jhsuhs", roomModel.toString());
                                            otheruser.child(messageID).setValue(roomModel);

                                            finish();
                                        }
                                    });
                                }
                            });


            }
        });

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            Glide.with(this).load(resultUri).into(imageView);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }




        }