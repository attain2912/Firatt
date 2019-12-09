package com.firatt.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

import com.firatt.R;
import com.firatt.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText edtusername, edtpassword;
    Button btnsign;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnsign = findViewById(R.id.btnsign);
        edtusername = findViewById(R.id.edtusername);
        edtpassword = findViewById(R.id.edtpassword);

        Fabric.with(this, new Crashlytics());

    }

    public void in(View view) {
        final String username = edtusername.getText().toString();
        final String password = edtpassword.getText().toString();


        reference=FirebaseDatabase.getInstance().getReference("Users").child(username);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(username.equals("")||password.equals("")){
                    Toast.makeText(MainActivity.this, "Please Login in right way :v", Toast.LENGTH_SHORT).show();
                }
                else if (dataSnapshot.getValue() == null) {
                    Toast.makeText(MainActivity.this, "Username is not signed up",Toast.LENGTH_SHORT).show();
                } else {

                    Users user = dataSnapshot.getValue(Users.class);

                    if (user.password.equals(password)) {

                        Toast.makeText(MainActivity.this, "Login success. Wellcome to Firatt", Toast.LENGTH_SHORT).show();
                        FirebaseInstanceId.getInstance().getInstanceId()
                                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                        if (!task.isSuccessful()) {
                                            return;
                                        }

                                        Map<String,Object> id=new HashMap<>();
                                        id.put("fcmId",task.getResult().getToken());
                                        reference.updateChildren(id);
                                        Log.e("UPDATE","fcmid= "+task.getResult().getToken() );
                                        Intent intent=new Intent(getApplicationContext(), ListActivity.class);
                                        intent.putExtra("username",username);
                                        intent.putExtra("fcmId",task.getResult().getToken());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    } else {
                        Toast.makeText(MainActivity.this, "Wrong Password. Please Login in right way", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void logReportAndPrint() {
        // [START crash_log_report_and_print]
        Crashlytics.log(Log.DEBUG, "tag", "message");
        // [END crash_log_report_and_print]
    }

    public void logReportOnly() {
        // [START crash_log_report_only]
        Crashlytics.log("message");
        // [END crash_log_report_only]
    }

    public void methodThatThrows() throws Exception {
        throw new Exception();
    }

    public void logCaughtEx() {
        // [START crash_log_caught_ex]
        try {
            methodThatThrows();
        } catch (Exception e) {
            Crashlytics.logException(e);
            // handle your exception here
        }
        // [END crash_log_caught_ex]
    }

    public void up (View view){
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
