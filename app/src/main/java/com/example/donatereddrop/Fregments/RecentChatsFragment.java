package com.example.donatereddrop.Fregments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.donatereddrop.Adapters.AdapterForRecentChats;
import com.example.donatereddrop.Models.ChatModel;
import com.example.donatereddrop.Models.SignupModel;
import com.example.donatereddrop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecentChatsFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList<SignupModel> modelArrayList;
    ArrayList<ChatModel> modelArrayListchat;
    ChatModel chatModel;
    SignupModel model;
    AdapterForRecentChats adapter;
    DatabaseReference databaseReference;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_recent_chats, container, false);
        recyclerView=view.findViewById(R.id.resyclerchats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        modelArrayList=new ArrayList<>();
        modelArrayListchat=new ArrayList<>();
        chatModel =new ChatModel();
        model=new SignupModel();

        readData();

        return view;
    }
    private void readData() {

        DatabaseReference chatsReference = databaseReference.child("Chats").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String model1 = dataSnapshot.getKey();
                    Log.d("jshgdsu",model1);
                    if (model1 != null) {

                        DatabaseReference chatReference = databaseReference.child("Chats")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child(model1);

                        chatReference.limitToLast(1) // Limit to the last message
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot chatSnapshot) {

                                        Log.d("jahiujd",chatSnapshot.toString());
                                        for (DataSnapshot messageSnapshot : chatSnapshot.getChildren()) {
                                            ChatModel chat = messageSnapshot.getValue(ChatModel.class);
                                            if (chat != null) {
                                                modelArrayListchat.add(chat); // Add the chat model
                                                Log.d("hsaguyayshbj",modelArrayListchat.toString());
                                                adapter = new AdapterForRecentChats(getContext(), modelArrayList, modelArrayListchat);
                                                recyclerView.setAdapter(adapter);
                                                adapter.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                    if (model1!=null){
                        DatabaseReference userReference = databaseReference.child("User").child(model1);
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                SignupModel user = userSnapshot.getValue(SignupModel.class);
                                Log.d("hsaguyays",user.toString());

                                modelArrayList.add(user);
                                Log.d("hjsjs",modelArrayList.toString());
                                adapter=new AdapterForRecentChats(getContext(),modelArrayList,modelArrayListchat);
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}

