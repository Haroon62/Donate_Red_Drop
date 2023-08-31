    package com.example.donatereddrop.Adapters;

    import android.content.Context;
    import android.content.Intent;
    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.LinearLayout;
    import android.widget.TextView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.example.donatereddrop.ActivityClasses.Chat;
    import com.example.donatereddrop.Models.ChatModel;
    import com.example.donatereddrop.Models.SignupModel;
    import com.example.donatereddrop.R;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Date;
    import java.util.Locale;

    import de.hdodenhof.circleimageview.CircleImageView;

    public class AdapterForRecentChats extends   RecyclerView.Adapter<AdapterForRecentChats.viewholder>  {

        Context context;
        ArrayList<SignupModel> arrayList;
        ArrayList<ChatModel> arrayListchat;
        DatabaseReference databaseReference;
        public AdapterForRecentChats(Context context , ArrayList<SignupModel> arrayList, ArrayList<ChatModel> arrayListchat) {
            this.context=context;
            this.arrayList=arrayList;
            this.arrayListchat=arrayListchat;
            databaseReference= FirebaseDatabase.getInstance().getReference();
        }



        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View  view= LayoutInflater.from(context).inflate(R.layout.chats_single_layout, parent, false);
            return  new viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
            SignupModel model=arrayList.get(position);
            holder.textView.setText(model.getName());
            Glide.with(holder.imageView.getContext()).load(model.getPurl()).into(holder.imageView);

            String userId = model.getId();
            Log.d("hasgdia",userId);
            ChatModel lastChatModel =arrayListchat.get(position);
          Log.d("jhgsuaksa",lastChatModel.toString());
            if (lastChatModel != null) {

                holder.msg.setText(lastChatModel.getMessagecontent());
                holder.time.setText(getFormattedTime(lastChatModel.getMessagetime()));
            }

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Chat.class);
                    intent.putExtra("name",model);
                    context.startActivity(intent);
                }
            });

        }



        public class viewholder extends RecyclerView.ViewHolder {
            LinearLayout linearLayout;
            TextView textView,time,msg;
            CircleImageView imageView;

            public viewholder(@NonNull View itemView) {
                super(itemView);

                imageView=itemView.findViewById(R.id.userimagechat);
                textView=itemView.findViewById(R.id.nameuserchat);
                time=itemView.findViewById(R.id.time);
                linearLayout=itemView.findViewById(R.id.recntCard);
                msg=itemView.findViewById(R.id.msg);
            }
        }

        private String getFormattedTime(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }
