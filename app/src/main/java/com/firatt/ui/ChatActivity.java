package com.firatt.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firatt.R;
import com.firatt.adapter.ChatAdapter;
import com.firatt.model.Chat;
import com.firatt.retrofit.Retrofit;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {
    ChatAdapter adapter;
    EditText edtmess;
    TextView tvgroup;
    RecyclerView rv;
    DatabaseReference dbRef,changechat,refchat,recchat;
    String groupname,senderfcm;
    int type;
    List<Chat> messList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        edtmess=findViewById(R.id.edtmess);
        tvgroup=findViewById(R.id.tvgroupchat);
        rv=findViewById(R.id.rv);
        groupname=this.getIntent().getStringExtra("username");
        senderfcm=this.getIntent().getStringExtra("senderfcm");
        Log.e("Sender", senderfcm);
        Log.e("Receiver",groupname);
        tvgroup.setText(groupname);

        dbRef=FirebaseDatabase.getInstance().getReference("Chat");
        refchat=dbRef.child(getIntent().getStringExtra("sender")).child(groupname).child("ListMess");
        recchat=dbRef.child(groupname).child(getIntent().getStringExtra("sender")).child("ListMess");
        changechat=dbRef.child(getIntent().getStringExtra("sender")).child(groupname);
        refchat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat comment = dataSnapshot.getValue(Chat.class);
                messList.add(comment);
            for (int i=0;i<messList.size();i++){
                Query query=refchat.child("Room").child("ListMess").orderByChild(messList.get(i).messageText);
                query.limitToFirst(5);
            }
                adapter = new ChatAdapter(ChatActivity.this, messList,senderfcm);
                rv.setAdapter(adapter);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot){}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s){}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError){}
        });

//        RecyclerView.LayoutManager manager= new LinearLayoutManager(this);
//        rv.setLayoutManager(manager);
        LinearLayoutManager manager1=new LinearLayoutManager(this);
        manager1.setStackFromEnd(true);
        manager1.setOrientation(RecyclerView.VERTICAL);
        rv.scrollToPosition(messList.size()-1);
        rv.setLayoutManager(manager1);
    }


    public void addmess(View view) {
        if(tvgroup.equals("")){
            Toast.makeText(this, "Login before reply", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
        }else if(edtmess.getText().toString().trim().equals("")){
            Toast.makeText(this, "Not typing signal", Toast.LENGTH_SHORT).show();
        }else {
            HashMap map = new HashMap();
            HashMap map2 = new HashMap();
            map2.put("title",getIntent().getStringExtra("sender"));
            map2.put("body",edtmess.getText().toString());
            map.put("notification",map2);
            map.put("to",getIntent().getStringExtra("fcmId"));
            HashMap id=new HashMap<>();
            id.put("send",getIntent().getStringExtra("sender"));
            id.put("rec",groupname);
            changechat.updateChildren(id);
            recchat.push().setValue(new Chat(edtmess.getText().toString().trim(),getIntent().getStringExtra("senderfcm"),type));
            refchat.push().setValue(new Chat(edtmess.getText().toString().trim(),getIntent().getStringExtra("senderfcm"),type));
            Retrofit.getInstance().addnotifi(map).enqueue(new Callback<Map>() {
                @Override
                public void onResponse(Call<Map> call, Response<Map> response) {
                    response.body();
                    Log.e("NOTIFY", response.body().toString() );
                    Toast.makeText(ChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                    edtmess.setText("");
                }

                @Override
                public void onFailure(Call<Map> call, Throwable t) {

                }
            });
        }



    }

    public void back(View view) {
        finish();
    }
}
