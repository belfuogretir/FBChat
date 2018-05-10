package com.example.belfu.fbchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import java.util.ArrayList;

public class KayitOlActivity extends AppCompatActivity {

    EditText et_mail, et_pass;
    Button btn_kayıt;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    ArrayList <String> userList = new ArrayList<>();
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        et_mail = (EditText) findViewById(R.id.editText5);
        et_pass = (EditText) findViewById(R.id.editText6);
        btn_kayıt = (Button) findViewById(R.id.button5);
        database = FirebaseDatabase.getInstance();

        mAuth=FirebaseAuth.getInstance();
        final DatabaseReference dbRef1 = database.getReference("users");
        btn_kayıt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = et_mail.getText().toString();
                String pass = et_pass.getText().toString();
                mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String mail = et_mail.getText().toString();
                            String id =OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId();
                            dbRef1.getRef().push().setValue(new User(mail,id));
                           // dbRef.setValue(user);


                            dbRef1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    userList.clear();
                                    for(DataSnapshot ds:dataSnapshot.getChildren()){
                                        User user = ds.getValue(User.class);
                                        if(user.getEmail().equals(firebaseUser.getEmail())){
                                            continue;
                                        }
                                        else {
                                            String[] asd = user.getEmail().split("\\.");
                                            userList.add(asd[0]);
                                        }
                                    }
                                    Log.wtf("asd",userList+"");
                                    Toast.makeText(getApplicationContext(),"Kayıt başarılı",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    intent.putExtra("userKey",userList);
                                    startActivity(intent);
                                    finish();
                                   // listview.setAdapter(adapter);
                                   // adapter.notifyDataSetChanged();
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }


                            });

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"kayıt başarısız",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}
