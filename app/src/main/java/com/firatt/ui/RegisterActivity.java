package com.firatt.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firatt.R;
import com.firatt.model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterActivity extends AppCompatActivity {
    EditText edtemail,edtphone,edtusername,edtpassword;
    DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edtemail=findViewById(R.id.edtemail);
        edtphone=findViewById(R.id.edtphone);
        edtusername=findViewById(R.id.edtusername);
        edtpassword=findViewById(R.id.edtpassword);




    }

    public void cancel(View view) {
        finish();
    }

    public void register(View view) {
        final String email = edtemail.getText().toString();
        final String phone = edtphone.getText().toString();
        final String username = edtusername.getText().toString();
        final String password = edtpassword.getText().toString();

        dbRef= FirebaseDatabase.getInstance().getReference("Users").child(username);

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() == null) {

                    Users user=new Users();
                    user.email=email;
                    user.phone=phone;
                    user.username=username;
                    user.password=password;

                    dbRef.setValue(user,new DatabaseReference.CompletionListener(){

                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                            Toast.makeText(RegisterActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                            edtemail.setText("");
                            edtusername.setText("");
                            edtphone.setText("");
                            edtpassword.setText("");
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
