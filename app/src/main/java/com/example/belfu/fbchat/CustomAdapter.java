package com.example.belfu.fbchat;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by belfu on 8.02.2018.
 */

public class CustomAdapter extends BaseAdapter {
    LayoutInflater layoutInflater;
    ArrayList<Mesaj> mesajList;
    FirebaseUser fUser;
    public CustomAdapter(Activity activity,ArrayList<Mesaj>mesajList,FirebaseUser fUser){
        this.mesajList=mesajList;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fUser=fUser;
    }
    @Override
    public int getCount(){return mesajList.size(); }

    @Override
    public Object getItem(int position){return mesajList.get(position);}

    @Override
    public long getItemId (int position) {return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View satir = null;
        Mesaj mesaj = mesajList.get(position);

        if(mesaj.getGonderen().equals(fUser.getEmail())) {
            satir = layoutInflater.inflate(R.layout.custom_sag,null);
            TextView mail = (TextView) satir.findViewById(R.id.textViewBen);
            mail.setText(mesaj.getGonderen());
            TextView mesajj = (TextView) satir.findViewById(R.id.textViewMesajim);
            mesajj.setText(mesaj.getMesaj());
            TextView zaman = (TextView) satir.findViewById(R.id.textViewZamanim);
            zaman.setText(mesaj.getZaman());

        }
        else {
            satir = layoutInflater.inflate(R.layout.custom_sol,null);
            TextView maili = (TextView) satir.findViewById(R.id.textViewGonderenKisi);
            maili.setText(mesaj.getGonderen());
            TextView mesaji = (TextView) satir.findViewById(R.id.textViewMesaji);
            mesaji.setText(mesaj.getMesaj());
            TextView zamani = (TextView) satir.findViewById(R.id.textViewZamani);
            zamani.setText(mesaj.getZaman());
        }
        return satir;
    }
}
