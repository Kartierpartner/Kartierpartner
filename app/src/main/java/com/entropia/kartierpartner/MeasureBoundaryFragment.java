package com.entropia.kartierpartner;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MeasureBoundaryFragment extends Fragment {

    public static MeasureBoundaryFragment newInstance() {
        return new MeasureBoundaryFragment();
    }

    public MeasureBoundaryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_measure_boundary, container, false);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final ListView obj;
        final DBHelper mydb;
        mydb = new DBHelper(getActivity());
        ArrayList array_list = mydb.getAllFormations();
        final ArrayAdapter arrayAdapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_multiple_choice, array_list);

        obj = (ListView)getActivity().findViewById(R.id.listView1);
        obj.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        obj.setAdapter(arrayAdapter);
        Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SparseBooleanArray checked = obj.getCheckedItemPositions();
                ArrayList<Integer> selectedItems = new ArrayList<Integer>();
                for (int i = 0; i < checked.size(); i++) {
                    int position = checked.keyAt(i);
                    if (checked.valueAt(i))
                        selectedItems.add(position);
                }
                if(2!=selectedItems.size()) {
                    Toast.makeText(getActivity(), R.string.boundaryOnlyTwo, Toast.LENGTH_SHORT).show();
                }
                else{
                    ArrayList array_list2 = mydb.getAllFormations2();
                    ArrayList<Integer> ids2pass = new ArrayList<Integer>();
                    ids2pass.add((Integer)array_list2.get((Integer)selectedItems.get(0)));
                    ids2pass.add((Integer)array_list2.get((Integer)selectedItems.get(1)));

                    Intent intent = new Intent(getActivity(), MeasureBoundary.class);
                    intent.putExtra("array_list", ids2pass);
                    startActivity(intent);
                }
            }
        });
    }
}