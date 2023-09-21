package com.example.donatereddropp.ActivityClasses;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.donatereddropp.Adapters.ChatAdapter;
import com.example.donatereddropp.Models.ChatModel;
import com.example.donatereddropp.Models.SignupModel;
import com.example.donatereddropp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {


    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int STORAGE_PERMISSION_REQUEST = 200;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    SignupModel model;

    ArrayList<ChatModel> chatlist;
    DatabaseReference databaseReference;
    FirebaseDatabase database;
    ChatAdapter chatAdapter;
    ImageView insertimage;
    ImageButton back, sendbtn;
    CircleImageView profileimage;
    TextView username;
    Uri filepath;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<Intent> cameraLauncher;
    TextInputLayout message;
    TextInputEditText editText;
    RecyclerView chatresycle;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        back = findViewById(R.id.back);
        sendbtn = findViewById(R.id.sendbtn);
        editText = findViewById(R.id.editext);
        chatresycle = findViewById(R.id.chatresycler);
        profileimage = findViewById(R.id.chat_profile_pic);
        username = findViewById(R.id.chatusername);
        message = findViewById(R.id.message);
        insertimage=findViewById(R.id.insertimage);
        chatlist = new ArrayList<>();



        // Get the current user's FCM token and save it to your database.




        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        chatresycle.setLayoutManager(new LinearLayoutManager(this));


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Chats");
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            filepath = data.getData();

                            Intent intent = new Intent(Chat.this, Image_Activity.class);
                            intent.putExtra("image", filepath.toString());
                            intent.putExtra("image2", model);
                            startActivity(intent);
                        }

                        }

                });
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {

                            Uri capturedImageUri = result.getData().getData();
                            Intent intent = new Intent(Chat.this, Image_Activity.class);
                            intent.putExtra("imagecemra", capturedImageUri.toString());
                            intent.putExtra("image2", model);
                            startActivity(intent);

                        }
                    }
                });

        insertimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePicDialog();

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (getIntent().getSerializableExtra("name") != null) {
            model = (SignupModel) getIntent().getSerializableExtra("name");
            username.setText(model.getName());
            Log.d("dhjjsa", model.toString());
            Glide.with(Chat.this).load(model.getPurl()).into(profileimage);
        }



        databaseReference = databaseReference
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(model.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlist.clear();
                chatlist=new ArrayList<>();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    ChatModel model1=snapshot1.getValue(ChatModel.class);
                    chatlist.add(model1);
                }
                chatAdapter=new ChatAdapter(Chat.this,chatlist);
                chatresycle.setAdapter(chatAdapter);
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msgtext = message.getEditText().getText().toString().trim();
                String purlofa=model.getPurl();

                if (!msgtext.isEmpty()) {

                    String messageID = databaseReference.push().getKey();
                    String receivrrID=model.getId();

                    DatabaseReference receiverUserReference = FirebaseDatabase.getInstance().getReference("Users")
                            ;

                    receiverUserReference.child(model.getFcmtoken()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String receiverFCMToken = dataSnapshot.getValue(String.class);

                                // Now, you have the receiver's FCM token.
                                // You can use it to send an FCM notification to the receiver.

                                sendFCMNotification(receiverFCMToken, "New Message", "You have received a new message!");

                                editText.setText("");
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle any errors here.
                        }
                    });


                    String cruntuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    ChatModel roomModel = new ChatModel(msgtext, cruntuser, System.currentTimeMillis(), receivrrID,messageID,purlofa,"");
                    databaseReference.child(messageID).setValue(roomModel);
                    DatabaseReference otheruser =database.getReference("Chats")
                            .child(model.getId()).child(FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid());
                    otheruser.child(messageID).setValue(roomModel);
                    editText.setText("");

                }
            }
        });


    }
    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {  // Camera option
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        openCamera();
                    }
                } else if (which == 1) {
//                    if (!checkStoragePermission()){
//                        requestStoragePermission();
//                    }else {
                        pickFromGallery();
                   // }


                }


            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activityResultLauncher.launch(intent);

    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, IMAGE_PICKCAMERA_REQUEST);
        }else {
            cameraLauncher.launch(cameraIntent);
        }
    }
    private boolean checkCameraPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST
        );
    }
//    private boolean checkStoragePermission() {
//        int storagePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        return storagePermission == PackageManager.PERMISSION_GRANTED;
//    }
//
//
//    private void requestStoragePermission() {
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                STORAGE_PERMISSION_REQUEST
//        );
//    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with opening the camera
                openCamera();
            } else {
                // Permission denied, handle this situation (e.g., show a message)
            }

        }
//        else if (requestCode == STORAGE_PERMISSION_REQUEST) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Storage permission granted, you can proceed with image operations
//                pickFromGallery(); // Or any other relevant action
//            } else {
//                // Storage permission denied, handle this situation (e.g., show a message)
//            }
//        }
    }
    private void sendFCMNotification(String receiverFCMToken, String title, String body) {
        // Create a message payload.
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);

        // Send the FCM message.
        FirebaseMessaging.getInstance().send(new RemoteMessage.Builder(receiverFCMToken)
                .setData(data)
                .build());
    }


}


