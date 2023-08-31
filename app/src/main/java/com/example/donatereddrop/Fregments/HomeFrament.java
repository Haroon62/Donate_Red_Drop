package com.example.donatereddrop.Fregments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.donatereddrop.Models.SignupModel;
import com.example.donatereddrop.R;
import com.example.donatereddrop.Adapters.AdapterForUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFrament extends Fragment {


    DatabaseReference databaseReference;
    SearchView searchView;
    Button Apostive, Bpostive, Opostive, Onegitve, Anegitive,Bnegitive,ABpostive,ABnegitive;
    RecyclerView recyclerView;
    ArrayList<SignupModel> Modellist;
    ArrayList<SignupModel> filteredArrayList;
    AdapterForUser adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_frament, container, false);
        searchView = view.findViewById(R.id.search);
        Apostive = view.findViewById(R.id.ap);
        Anegitive = view.findViewById(R.id.an);
        Bpostive = view.findViewById(R.id.bp);
        Opostive = view.findViewById(R.id.op);
        Onegitve = view.findViewById(R.id.on);
        Bnegitive = view.findViewById(R.id.bn);
        ABpostive = view.findViewById(R.id.abp);
        ABnegitive = view.findViewById(R.id.abn);

        adapter=new AdapterForUser(getContext(),Modellist);
        recyclerView = view.findViewById(R.id.resycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Modellist = new ArrayList<>();

        readData();

        Anegitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Anegitive.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                 filteredArrayList.clear();
        for (SignupModel person : Modellist) {
            if (person.getBgroup().equalsIgnoreCase("A-")) {
                filteredArrayList.add(person);
            }
        }
        adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Apostive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Apostive.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                filteredArrayList.clear();
                for (SignupModel person : Modellist) {
                    if (person.getBgroup().equalsIgnoreCase("A+")) {
                        filteredArrayList.add(person);
                    }
                }
                adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Bpostive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Bpostive.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                filteredArrayList.clear();
                for (SignupModel person : Modellist) {
                    if (person.getBgroup().equalsIgnoreCase("B+")) {
                        filteredArrayList.add(person);
                    }
                }
                adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Bnegitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                filteredArrayList.clear();
                for (SignupModel person : Modellist) {
                    if (person.getBgroup().equalsIgnoreCase("B-")) {
                        filteredArrayList.add(person);
                    }
                }
                adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Opostive.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Opostive.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                filteredArrayList.clear();
                for (SignupModel person : Modellist) {
                    if (person.getBgroup().equalsIgnoreCase("O+")) {
                        filteredArrayList.add(person);
                    }
                }
                adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Onegitve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Onegitve.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                filteredArrayList.clear();
                for (SignupModel person : Modellist) {
                    if (person.getBgroup().equalsIgnoreCase("O-")) {
                        filteredArrayList.add(person);
                    }
                }
                adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ABpostive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ABpostive.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                filteredArrayList.clear();
                for (SignupModel person : Modellist) {
                    if (person.getBgroup().equalsIgnoreCase("AB+")) {
                        filteredArrayList.add(person);
                    }
                }
                adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ABnegitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.button_design_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));


                filteredArrayList=new ArrayList<>();
                filteredArrayList.clear();
                for (SignupModel person : Modellist) {
                    if (person.getBgroup().equalsIgnoreCase("AB-")) {
                        filteredArrayList.add(person);
                    }
                }
                adapter.filterByBloodGroup(filteredArrayList);
                if (filteredArrayList.isEmpty()){
                    Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ABnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Apostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Bnegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Opostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Onegitve.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                ABpostive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                Anegitive.setBackground(getResources().getDrawable(R.drawable.non_selected_outer));
                onProceesing(newText);
                return true;
            }
        });

        return view;
    }

    private void readData() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Modellist.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    SignupModel model1=dataSnapshot.getValue(SignupModel.class);
                    Modellist.add(model1);
                }
                adapter=new AdapterForUser(getContext(),Modellist);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public  void onProceesing(String query){

        filteredArrayList= new ArrayList<>();
            filteredArrayList.clear();
            String lowerQuery = query.toLowerCase();
            for (SignupModel model : Modellist) {
                if (model.getBgroup().toLowerCase().contains(lowerQuery)) {
                    filteredArrayList.add(model);
                }

        }
        adapter.filterByBloodGroup(filteredArrayList);

        if (filteredArrayList.isEmpty()){
            //Toast.makeText(getContext(), "No data found", Toast.LENGTH_SHORT).show();
        }

    }



    }

