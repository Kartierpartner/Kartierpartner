package com.entropia.kartierpartner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;

public class ChangeProjectFragment extends Fragment {
    public DBHelper mydb;


    public static ChangeProjectFragment newInstance() {
        return new ChangeProjectFragment();
    }

    public ChangeProjectFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_change_project, container, false);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        ListView obj;
        mydb = new DBHelper(getActivity());
        final ArrayList array_list = mydb.getAllFormations();
        final ArrayList array_list2 = mydb.getAllFormations2();
        final ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1, array_list);

        obj = (ListView)getActivity().findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {

                int id2pass = (Integer)array_list2.get(position);
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id2pass);
                FormationDetailFragment f = new FormationDetailFragment();
                f.setArguments(dataBundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, f)
                        .commit();


            }
        });

        Button button = (Button) view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, AddFormationFragment.newInstance())
                        .commit();
            }
        });
    }
}


