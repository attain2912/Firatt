package com.firatt.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firatt.model.Chat;
import com.firatt.ui.ChatActivity;
import com.firatt.R;
import com.firatt.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<Users> usersList;
    String userlogin;
    String fcmlogin;
    DatabaseReference reference;

    public UserAdapter(Context context, List<Users> usersList, String userlogin, String fcmlogin) {
        this.context = context;
        this.fcmlogin = fcmlogin;
        this.usersList = usersList;
        this.userlogin = userlogin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Users users = usersList.get(position);
        holder.tvemail.setText("Email: " + users.email);
        holder.tvusername.setText(users.username);
        holder.tvphone.setText("Phone: " + users.phone);
        if (usersList.get(position).username.equals(userlogin)) {
            // holder.itemView.setEnabled(false);
            holder.itemView.setVisibility(View.GONE);
            Toast.makeText(context, "You can't message for yourself", Toast.LENGTH_SHORT).show();
        }


        holder.ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                delete(users.username,position);
            }
        });
//        holder.ivupdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            Intent i= new Intent(context, UpdateActivity.class);
//            i.putExtra("username",users.username);
//            i.putExtra("phone",users.phone);
//            i.putExtra("email",users.email);
//            i.putExtra("password",users.password);
//            context.startActivity(i);
//
//            }
//        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("username", users.username);
                intent.putExtra("fcmId", users.fcmId);
                intent.putExtra("sender", userlogin);
                intent.putExtra("senderfcm", fcmlogin);
                //reference=FirebaseDatabase.getInstance().getReference("Chat").child("Room").child(users.username);
                reference = FirebaseDatabase.getInstance().getReference("Chat").child(userlogin).child(users.username);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            Chat chat = new Chat();
                            chat.rec = users.username;
                            chat.send = userlogin;

                            reference.setValue(chat, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    Toast.makeText(context, "Created Room ", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(context, "Room Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvemail;
        TextView tvusername;
        TextView tvphone;
        ImageView ivdelete;

        //        ImageView ivupdate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvemail = itemView.findViewById(R.id.tvemail);
            tvusername = itemView.findViewById(R.id.tvusername);
            tvphone = itemView.findViewById(R.id.tvphone);
            ivdelete = itemView.findViewById(R.id.ivdelete);
//            ivupdate=itemView.findViewById(R.id.ivupdate);
        }
    }

    private void delete(String adId, final int position) {
        FirebaseDatabase.getInstance().getReference("Users").child(usersList.get(position).username).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            usersList.remove(position);
                            notifyItemChanged(position);
                            notifyItemRemoved(position);
                            Log.e("Delete", "DELETED");
                            Toast.makeText(context, "DELETED", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("Delete", "not DELETED");
                            Toast.makeText(context, "not DELETED", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
