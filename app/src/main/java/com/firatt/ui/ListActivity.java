package com.firatt.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firatt.R;
import com.firatt.adapter.UserAdapter;
import com.firatt.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class ListActivity extends AppCompatActivity {
    RecyclerView rv;
    List<Users> users = new ArrayList<>();
    UserAdapter adapter;
    String userlogin,fcmlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        rv = findViewById(R.id.rv);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ListActivity.this);
        rv.setLayoutManager(manager);
        userlogin=this.getIntent().getStringExtra("username");
        fcmlogin=this.getIntent().getStringExtra("fcmId");
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference("Users");
        nm.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        Users l = npsnapshot.getValue(Users.class);
                        users.add(l);
                    }
                    adapter = new UserAdapter(ListActivity.this, users,userlogin,fcmlogin);
                    rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
        public void back (View view){
            finish();
        }
    }

