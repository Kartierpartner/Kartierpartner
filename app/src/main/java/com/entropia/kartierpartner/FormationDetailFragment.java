package com.entropia.kartierpartner;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;

public class FormationDetailFragment extends Fragment {
    public DBHelper mydb;
    public int id;


    public static FormationDetailFragment newInstance() {
        return new FormationDetailFragment();
    }

    public FormationDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_formation_detail, container, false);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        final int id = getArguments().getInt("id");
        mydb = new DBHelper(getActivity());
        Cursor cursor1 = mydb.getData(id);
        cursor1.moveToFirst();
        String name1 = cursor1.getString(cursor1.getColumnIndex(DBHelper.KP_COLUMN_NAME));
        TextView textViewDetail = (TextView) getActivity().findViewById(R.id.textViewDetail);
        textViewDetail.setText(name1);

        Button button = (Button) view.findViewById(R.id.deleteButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });
    }
    private AlertDialog AskOption()
    {
        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.deleteFormation))
                .setMessage(getString(R.string.deleteFormationText))

                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        mydb = new DBHelper(getActivity());
                        final int idToDelete = getArguments().getInt("id");
                        mydb.deleteFormation(idToDelete);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.content_frame, ChangeProjectFragment.newInstance())
                                .commit();
                    }

                })



                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }
}
