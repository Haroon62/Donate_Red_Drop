package com.example.donatereddropp.ActivityClasses;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
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
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 100;
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

   public static String msgtext;
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
       message  = findViewById(R.id.message);
        insertimage=findViewById(R.id.insertimage);
        chatlist = new ArrayList<>();

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
                msgtext = message.getEditText().getText().toString().trim();
                String purlofa=model.getPurl();

                if (!msgtext.isEmpty()) {

                    Intent serviceIntent = new Intent(Chat.this, ServiceClass.class);
                    serviceIntent.putExtra("message", msgtext);
                    startService(serviceIntent);

                    String messageID = databaseReference.push().getKey();
                    String receivrrID=model.getId();

                    DatabaseReference receiverUserReference = FirebaseDatabase.getInstance().getReference();
                    receiverUserReference.child("User").child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Log.d("hssdhsijs", dataSnapshot.toString());
                            if (dataSnapshot.exists()) {
                                DataSnapshot fcmTokenSnapshot = dataSnapshot.child("fcmtoken"); // Replace "id" with the actual key where the FCM token is stored

                                if (fcmTokenSnapshot.exists()) {
                                    String receiverFCMToken = fcmTokenSnapshot.getValue(String.class);

                                    if (receiverFCMToken != null) {
                                        Log.d("hssdhsijs", receiverFCMToken);
                                        FcmNotificationSender notificationSender = new FcmNotificationSender(receiverFCMToken, "Red Drop", "you have received a message", Chat.this);
                                        notificationSender.SendNotifications(model.getId());
                                    } else {
                                        Log.d("hssdhsijs", "FCM token not found in user data");
                                    }
                                } else {
                                    Log.d("hssdhsijs", "FCM token key not found in user f data");
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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

                }else {
                    Toast.makeText(Chat.this, "Can't Send empty message", Toast.LENGTH_SHORT).show();
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
//    private void  sendFCMNotification(String receiverFCMToken, String title, String body) {
//
//         RequestQueue requestQueue = Volley.newRequestQueue(this);
//        JSONObject mainObj = new JSONObject();
//        try {
//            mainObj.put("to", receiverFCMToken);
//            JSONObject notiObj = new JSONObject();
//            notiObj.put("title", title);
//            notiObj.put("body", body);
//            notiObj.put("type", 0);
//
//            mainObj.put("notification", notiObj);
//
//
//            Log.d("hjsj", mainObj.toString());
//
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, mainObj, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("notirespo", response.toString());
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("notirespo", error.getMessage()+" ");
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> header = new HashMap<>();
//                header.put("Content-Type", "application/json");
//                header.put("Authorization", Key);
//                return header;
//            }
//        };
//
//        requestQueue.add(request);
//
//
//    }


}


