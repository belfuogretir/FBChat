package com.example.belfu.fbchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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

public class ChatOdalariActivity extends AppCompatActivity {

    ListView listView2;
    ArrayList<String> asd;
    ArrayList<String> chatOdalariList = new ArrayList<>();
    FirebaseDatabase database;
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_odalari);
        listView2 = (ListView) findViewById(R.id.ListView2);

        Bundle bundle = getIntent().getExtras();
        asd = bundle.getStringArrayList("userKey2");

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, chatOdalariList);
        database = FirebaseDatabase.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:dd");

        final DatabaseReference dbRef = database.getReference("chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] email = firebaseUser.getEmail().split("\\.");
                String sEmail = email[0];
                chatOdalariList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().contains(sEmail)) {
                        chatOdalariList.add(ds.getKey());
                    }
                    listView2.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String secilenOda = chatOdalariList.get(i);
                Log.wtf("secilen oda",chatOdalariList.get(i));
                Intent intent = new Intent(getApplicationContext(),ChatYapActivity.class);
                intent.putExtra("KisiKey",secilenOda);
                startActivity(intent);
            }
        });


        /*  //odayı burda oluşturuyoruz
        final DatabaseReference dbRef = database.getReference("chats");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oda = editText.getText().toString();
                dbRef.getRef().push().setValue(new OdaIsmi(oda));
                editText.setText("");
            }
        });*/
        /*dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatOdalariList.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    for(DataSnapshot ds2:ds.getChildren()){
                        if (ds2.getKey().equals("name"))
                        chatOdalariList.add(ds2.getValue()+"");
                    }
                    //chatOdalariList.add(ds.getKey());
                }
                listVieww.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

        //listViewda seçilen odanın ismini tutuyor
       /* listVieww.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String secilenOda = chatOdalariList.get(i);
                Intent odaIntent = new Intent(getApplicationContext(),ChatYapActivity.class);
                odaIntent.putExtra("odaKey",secilenOda);
                startActivity(odaIntent);
            }
        });*/
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(getApplicationContext(),UsersActivity.class);
            intent.putExtra("listKey",asd);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

