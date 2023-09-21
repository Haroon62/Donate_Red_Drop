package com.example.donatereddropp.ActivityClasses;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.donatereddropp.Models.ChatModel;
import com.example.donatereddropp.Models.SignupModel;
import com.example.donatereddropp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Image_Activity extends AppCompatActivity {
    ImageView imageView,crop;

    CropImageView cropImageView;
    ImageButton button;
    SignupModel model;

    DatabaseReference databaseReference;
    FirebaseDatabase database;
    ChatModel roomModel;
    Uri originalImageUri; // Store the original image URI
    Uri croppedImageUri;
    Uri resultUri ;// Store the URI of the cropped image
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageset);
        cropImageView = findViewById(R.id.cropImageView); // Update this to the ID of your CropImageView
        button = findViewById(R.id.sendimagebtn);
        model = new SignupModel();
        roomModel = new ChatModel();
        crop=findViewById(R.id.crop);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Chats");

        if (getIntent().getSerializableExtra("image") != null) {
            String imageUriString = getIntent().getStringExtra("image");
            originalImageUri = Uri.parse(imageUriString);
            Glide.with(this).load(originalImageUri).into(imageView);
        }
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity(originalImageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(Image_Activity.this);
            }
        });

        // Initialize the CropImageView with the original image
        cropImageView.setImageUriAsync(originalImageUri);

        if (getIntent().getSerializableExtra("image2") != null) {
            model = (SignupModel) getIntent().getSerializableExtra("image2");
            Log.d("jasjjs", model.toString());
        }


        databaseReference = databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(model.getId());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hsahnjshd", croppedImageUri.toString());
                Log.d("hsahnjshd", originalImageUri.toString());

                if (croppedImageUri != null || originalImageUri != null) {
                    String purlofa = model.getPurl();
                    String messageID = databaseReference.push().getKey();
                    String cruntuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String receivrrID = model.getId();


                   if (croppedImageUri!=null){

                       storageReference = FirebaseStorage.getInstance().getReference("SentItemsSaved");
                       storageReference.child(croppedImageUri.getLastPathSegment());
                       storageReference.putFile(croppedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       Log.d("jhsuhsjsj", uri.toString());
                                       ChatModel roomModel = new ChatModel("", cruntuser, System.currentTimeMillis(), receivrrID, messageID, purlofa, uri.toString());
                                       databaseReference.child(messageID).setValue(roomModel);
                                       DatabaseReference otheruser = database.getReference("Chats")
                                               .child(receivrrID).child(cruntuser);
                                       Log.d("jhsuhs", roomModel.toString());
                                       otheruser.child(messageID).setValue(roomModel);

                                       finish();
                                   }
                               });
                           }
                       });

                   }

                    else if(originalImageUri!=null){
                       storageReference = FirebaseStorage.getInstance().getReference("SentItemsSaved");
                       storageReference.child(originalImageUri.getLastPathSegment());
                       storageReference.putFile(originalImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                           @Override
                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                   @Override
                                   public void onSuccess(Uri uri) {
                                       Log.d("jhsuhsjsj", uri.toString());
                                       ChatModel roomModel = new ChatModel("", cruntuser, System.currentTimeMillis(), receivrrID, messageID, purlofa, uri.toString());
                                       databaseReference.child(messageID).setValue(roomModel);
                                       DatabaseReference otheruser = database.getReference("Chats")
                                               .child(receivrrID).child(cruntuser);
                                       Log.d("jhsuhs", roomModel.toString());
                                       otheruser.child(messageID).setValue(roomModel);

                                       finish();
                                   }
                               });
                           }
                       });
                   }

                } else {
                    // Handle the case where the image was not cropped or cropping failed
                    Log.e("uahsiuhis", "Cropping failed or no image cropped.");
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                // Get the cropped image Uri
                croppedImageUri = result.getUri();
                Log.d("jjcnihks", croppedImageUri.toString());
                Glide.with(this).load(croppedImageUri).into(imageView);
                //cropImageView.setImageUriAsync(resultUri);




            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // Handle crop error
                Exception error = result.getError();
                Log.e("CropImageError", "Crop error: " + error.getMessage());
            }
        }
    }
}
