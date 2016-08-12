package com.entropia.kartierpartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class StrikeDipFragment extends Fragment {

    public DBHelper mydb;

    public static StrikeDipFragment newInstance() {
        return new StrikeDipFragment();
    }

    public StrikeDipFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_strike_dip, container, false);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final Intent intent = new Intent(getActivity(), StrikeDip.class);
        ListView obj;
        mydb = new DBHelper(getActivity());

        final ArrayList array_list = mydb.getAllFormations();
        final ArrayList array_list2 = mydb.getAllFormations2();
        ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, array_list);

        obj = (ListView)getActivity().findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int position, long id) {
                int id2pass = (Integer)array_list2.get(position);
                intent.putExtra("idToSearch", id2pass);
                startActivity(intent);

            }
        });
    }
}