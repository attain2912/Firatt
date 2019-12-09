package com.firatt.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firatt.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UpdateActivity extends AppCompatActivity {
    EditText edtemail,edtphone,edtpassword;
    String username,phone,password,email;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        edtemail=findViewById(R.id.edtemail);
        edtphone=findViewById(R.id.edtphone);
        edtpassword=findViewById(R.id.edtpassword);



        username = getIntent().getStringExtra("username");
        phone = getIntent().getStringExtra("phone");
        edtphone.setText(phone);
        password = getIntent().getStringExtra("password");
        edtpassword.setText(password);
        email = getIntent().getStringExtra("email");
        edtemail.setText(email);
    }

    public void update(View view) {
        dbRef= FirebaseDatabase.getInstance().getReference("Users").child(username);
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("email", edtemail.getText().toString());
        childUpdates.put("phone", edtphone.getText().toString());
        childUpdates.put("password", edtpassword.getText().toString());

        dbRef.updateChildren(childUpdates);
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }


    public void back(View view) {
        String updatephone=edtphone.getText().toString();
        String updateemail=edtemail.getText().toString();
        String updatepass=edtpassword.getText().toString();
        Intent i= new Intent(this, ListActivity.class);
        i.putExtra("phone",updatephone);
        i.putExtra("email",updateemail);
        i.putExtra("password",updatepass);
        startActivity(i);
    }
}
