package com.gac2013.tdylf.pocketparamedic;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//import android.widget.Toast;

public class StartFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.start, container, false);

        ListView listView = (ListView) vg.findViewById(R.id.listView);

        final String[] VALUES = new String[]{
                "Allergies",
                "Asthma attack",
                "Bleeding",
                "Broke bone",
                "Burns",
                "Chocking",
                "Diabetic emergency",
                "Heart attack",
                "Head injury",
                "Seizure",
                "Strains and sprains"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, VALUES);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

              //  Toast.makeText(getActivity().getApplicationContext(), "onItemClick", Toast.LENGTH_LONG).show();

                ((MainActivity) getActivity()).setupInstructionScreen();

            }
        });

        return vg;
    }
}

