package com.example.belfu.fbchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UsersActivity extends AppCompatActivity {

    ListView listView;
    int odaStatu = 0;
    String[] kullanıcıAdi;
    String aa = "";
    ArrayList<String> list;
    ArrayList<String> userList = new ArrayList<>();
    FirebaseDatabase database;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        listView= (ListView) findViewById(R.id.listviewkisiler);

        Bundle bundle = getIntent().getExtras();
        list = bundle.getStringArrayList("kisiList");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, userList);
        database = FirebaseDatabase.getInstance();

        if (list == null) {
            final DatabaseReference dbRef2 = database.getReference("users");
            dbRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if(user.getEmail().equals(firebaseUser.getEmail())){
                            continue;
                        }
                        else {
                            String[] asd = user.getEmail().split("\\.");
                            userList.add(asd[0]);
                        }
                    }
                    Log.wtf("asd", userList + "");
                    listView.setAdapter(adapter1);
                    adapter1.notifyDataSetChanged();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        final DatabaseReference dbRef = database.getReference("chats");


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:dd");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (list == null) {
                            kullanıcıAdi = userList.get(i).split("\\.");
                        } else {
                            kullanıcıAdi = list.get(i).split("\\.");
                        }
                        String sKulAdi = kullanıcıAdi[0];
                        String[] email = firebaseUser.getEmail().split("\\.");
                        String sEmail = email[0];
                        int alph = 0;
                        alph = sKulAdi.compareTo(sEmail);
                        if (alph < 0) {
                            aa = kullanıcıAdi[0] + "-" + email[0];
                        } else {
                            aa = email[0] + "-" + kullanıcıAdi[0];
                        }
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            for (DataSnapshot ds2 : ds.getChildren()) {
                                if (ds2.getKey().equals(aa)) ;
                                odaStatu = 1;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                //konuşma varsa
                if (!aa.equals("")) {
                    if (odaStatu == 0) {
                        dbRef.child(aa).setValue("");
                    }
                    Intent odaIntentt = new Intent(getApplicationContext(), ChatYapActivity.class);
                    odaIntentt.putExtra("KisiKey", aa);
                    startActivity(odaIntentt);
                } else{
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:dd");
                    String zaman = sdf.format(new Date());
                    dbRef.child("updatedTime").setValue(zaman);
                }
            }
        });
    }
}
