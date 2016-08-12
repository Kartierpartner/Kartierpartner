package com.entropia.kartierpartner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

public class AddFormationFragment extends Fragment {
    public DBHelper mydb;
    Button mButton;
    EditText mEdit;


    public static AddFormationFragment newInstance() {
        return new AddFormationFragment();
    }

    public AddFormationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_formation, container, false);
    }

    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        mydb = new DBHelper(getActivity());

        Button button = (Button) view.findViewById(R.id.btnadd);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try  {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                mEdit = (EditText) getActivity().findViewById(R.id.formation_name);
                String toDB = mEdit.getText().toString();
                mydb.insertFormation(toDB);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, ChangeProjectFragment.newInstance())
                        .commit();
            }
        });


    }
}
