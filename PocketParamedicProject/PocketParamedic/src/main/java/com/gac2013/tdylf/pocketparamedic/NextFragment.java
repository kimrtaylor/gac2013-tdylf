package com.gac2013.tdylf.pocketparamedic;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.security.PermissionCollection;
import java.util.ArrayList;
import java.util.Collections;

public class NextFragment extends Fragment implements ContinuousSpeechRecognizer.RecognizedTextListener {

    private ContinuousSpeechRecognizer csr;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.instructions, container, false);
        return vg;
    }

    @Override
    public void onResume() {
        super.onResume();
        context = getActivity().getApplicationContext();
        csr = new ContinuousSpeechRecognizer(context);
        csr.setListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                csr.startRecognition();
            }
        }, 2000);

    }

    @Override
    public void onStop() {
        super.onStop();
        csr.stopRecognition();
    }

    @Override
    public void onResults(ArrayList<String> results) {
        if (results.contains("yes")) {
            Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
        } else if (results.contains("no")) {
            Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
        }
    }
}
