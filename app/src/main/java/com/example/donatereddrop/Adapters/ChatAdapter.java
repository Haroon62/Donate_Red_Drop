package com.example.donatereddrop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.donatereddrop.Models.ChatModel;
import com.example.donatereddrop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.viewholder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    Context context;
    ArrayList<ChatModel> arrayList;


    DatabaseReference databaseReference;
    public ChatAdapter(Context context , ArrayList<ChatModel> arrayList) {
        this.context=context;
        this.arrayList=arrayList;
        databaseReference= FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public ChatAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==VIEW_TYPE_MESSAGE_SENT) {
           view= LayoutInflater.from(context).inflate(R.layout.chat_send, parent, false);
        }else {
           view= LayoutInflater.from(context).inflate(R.layout.chat_receive, parent, false);
        }

        return  new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.viewholder holder, int position) {
        ChatModel chatModel =arrayList.get(position);
        Log.d("jashis",chatModel.toString());


        if (!chatModel.getImage().equals("")) {

            holder.linearLayout2.setVisibility(View.VISIBLE);
            holder.linearLayout1.setVisibility(View.GONE);
            Glide.with(holder.imagesend.getContext()).load(chatModel.getImage()).into(holder.imagesend);
            Glide.with(holder.imageView.getContext()).load(chatModel.getPurlofprofilepic()).into(holder.imageView);

            long timestamp1 = chatModel.getMessagetime();
            String formattedTime1 = getFormattedTime(timestamp1);
            holder.timeview1.setText(formattedTime1);
        } else {

            holder.linearLayout2.setVisibility(View.GONE);
            holder.linearLayout1.setVisibility(View.VISIBLE);

            holder.textView.setText(chatModel.getMessagecontent());
            Glide.with(holder.imageView.getContext()).load(chatModel.getPurlofprofilepic()).into(holder.imageView);

            long timestamp = chatModel.getMessagetime();
            String formattedTime = getFormattedTime(timestamp);
            holder.timeview.setText(formattedTime);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    @Override
    public int getItemViewType(int position) {
        ChatModel message = arrayList.get(position);
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("hjssh",currentUserId);

        if (message.getSenderids() ==currentUserId) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView textView,timeview,timeview1;
       LinearLayout linearLayout1,linearLayout2;
        ImageView imageView;
        ImageView imageView1,imagesend;


        public viewholder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.chatpic);
            textView=itemView.findViewById(R.id.text);
            timeview=itemView.findViewById(R.id.timestamp);
            imageView1=itemView.findViewById(R.id.chatpic1);
            imagesend=itemView.findViewById(R.id.imagesend);
            timeview1=itemView.findViewById(R.id.timestamp1);
            linearLayout1=itemView.findViewById(R.id.lineerlayout);
            linearLayout2=itemView.findViewById(R.id.layoutlineer);
        }
    }
    private String getFormattedTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
