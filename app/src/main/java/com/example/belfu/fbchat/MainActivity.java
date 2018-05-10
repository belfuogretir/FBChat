package com.example.belfu.fbchat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EditText et_mail, et_pass;
    Button btn_giris;
    TextView tv;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> cuserlist = null;
        String asd;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cuserlist = bundle.getStringArrayList("userKey");
            asd = bundle.getString("userKey");
        }

        Log.wtf("sdgg", cuserlist + "");


        et_mail = (EditText) findViewById(R.id.editText);
        et_pass = (EditText) findViewById(R.id.editText2);
        btn_giris = (Button) findViewById(R.id.button2);
        tv = (TextView) findViewById(R.id.textView);
        mAuth = FirebaseAuth.getInstance();
        final ArrayList<String> finalCuserlist = cuserlist;
        btn_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = et_mail.getText().toString();
                String pass = et_pass.getText().toString();
                mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), ChatOdalariActivity.class);
                            intent.putExtra("userKey2", finalCuserlist);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Giriş Yapılamadı", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iintent = new Intent(getApplicationContext(), KayitOlActivity.class);
                startActivity(iintent);
            }
        });

    }
}
