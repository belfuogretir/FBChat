package com.example.belfu.fbchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChatYapActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    ImageButton button;
    ListView listView;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_yap);

        textView = (TextView) findViewById(R.id.textView2);
        editText = (EditText) findViewById(R.id.editText4);
        button = (ImageButton) findViewById(R.id.imageButton);
        listView = (ListView) findViewById(R.id.listview);
        listView.setDivider(null);
        final String oda = getIntent().getStringExtra("KisiKey");
        textView.setText(oda);
        final ArrayList<Mesaj> mesajList = new ArrayList<>();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        final DatabaseReference dbRef=database.getReference("chats/"+oda);
        String[] not = oda.split("-");
        String aaaa= not[0];
        Log.wtf("aaa",aaaa);
        String[] email = firebaseUser.getEmail().split("\\.");
        String bbbb= email[0];
        Log.wtf("bbbb",bbbb);
        String giden = null;
        final String userId;
        if(not[0].equals(email[0])){
            giden = not[1];
        }
        else
        giden =not[0];
        Log.wtf("notification",giden);
        final String finalGiden = giden;
        Log.wtf("final giden",finalGiden);

        DatabaseReference dbRefUsers=database.getReference("users");
        final String[] gonderilecekOneSignalId = {null};

        dbRefUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    Log.wtf("usermail",user.getEmail());
                    if (user.getEmail().equals(finalGiden+".com")) {
                        Log.wtf("onesignalId", user.getOneSignalId());
                        gonderilecekOneSignalId[0] = user.getOneSignalId();
                        Log.wtf("gidenID", gonderilecekOneSignalId[0]);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
       // userId = "c6c6541f-caab-4236-9396-1fb278b72ebe";
        //odanın içine child ekliyoruz
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OneSignal.postNotification(new JSONObject("{'contents': {'en':"+editText.getText().toString()+"}, 'include_player_ids': ['" + gonderilecekOneSignalId[0] + "'],'data':{'id':"+oda+"}}"), null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String gonderen = firebaseUser.getEmail();
                String mesaj = editText.getText().toString();
                SimpleDateFormat sdf= new SimpleDateFormat("HH:mm:dd");
                String zaman = sdf.format(new Date());
                dbRef.getRef().push().setValue(new Mesaj(gonderen,mesaj,zaman));
                editText.setText("");
            }
        });
        final CustomAdapter adapter = new CustomAdapter(this,mesajList,firebaseUser);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mesajList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if (ds.getKey().equals("name"))
                        continue;
                    mesajList.add(ds.getValue(Mesaj.class));
                }
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
