package com.example.donatereddropp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.donatereddropp.ActivityClasses.Chat;
import com.example.donatereddropp.R;
import com.example.donatereddropp.Models.SignupModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterForUser extends RecyclerView.Adapter<AdapterForUser.viewholder> {

    Context context;
    ArrayList<SignupModel> arrayList;
    DatabaseReference databaseReference;
    public AdapterForUser(Context context , ArrayList<SignupModel> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public AdapterForUser.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.single_layout_recycler_view, parent,false);
        viewholder viewHolder1=new viewholder(view);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterForUser.viewholder holder, int position) {
        SignupModel model=arrayList.get(position);

        if (model.getDonatee().equals("No")) {

        }
            holder.namesingle.setText(model.getName());
            holder.addressingle.setText(model.getAddresmodel());
            holder.phonenumbersingle.setText(model.getPhone());
            holder.bloodgorupsingle.setText(model.getBgroup());
            Glide.with(holder.imageViewsingle.getContext()).load(model.getPurl()).into(holder.imageViewsingle);
            holder.chatsingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Chat.class);
                    intent.putExtra("name", model);
                    context.startActivity(intent);
                }
            });




    }


    @Override
    public int getItemCount() {
        int count = 0;
        for (SignupModel model : arrayList) {
            if (!model.getDonatee().equals("No")) {
                count++;
            }
        }
        return count;
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView imageViewsingle;
        TextView namesingle,addressingle,phonenumbersingle,bloodgorupsingle;
        ImageButton chatsingle;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            imageViewsingle=itemView.findViewById(R.id.userimage);
            namesingle=itemView.findViewById(R.id.nameuser);
            addressingle=itemView.findViewById(R.id.address);
            phonenumbersingle=itemView.findViewById(R.id.phone);
            bloodgorupsingle=itemView.findViewById(R.id.bloood);
            chatsingle=itemView.findViewById(R.id.chatbutton);

        }
    }

    public void filterByBloodGroup(ArrayList<SignupModel> query) {
            arrayList=query;
            notifyDataSetChanged();
        }



}
