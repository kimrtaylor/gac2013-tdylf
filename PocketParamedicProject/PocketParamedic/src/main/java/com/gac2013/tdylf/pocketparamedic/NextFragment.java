package com.gac2013.tdylf.pocketparamedic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class NextFragment extends Fragment
        implements ContinuousSpeechRecognizer.RecognizedTextListener, TextToSpeech.OnInitListener {

    private ContinuousSpeechRecognizer csr;
    private Context context;
    private TextToSpeech tts;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++)
            Log.e(getClass().getName(), i + " -> " + fm.getBackStackEntryAt(i).getName());

        State currentState = StateMachine.getCurrentState();
        ViewGroup vg = (ViewGroup)inflater.inflate(R.layout.instructions, container, false);
        ((TextView)vg.findViewById(R.id.tvInstr)).setText("" + currentState.getId());
        ((ImageView)vg.findViewById(R.id.ivInstr)).setImageResource(currentState.getImageResId());

        context = getActivity().getApplicationContext();

        tts = new TextToSpeech(context, this);

        return vg;
    }

    @Override
    public void onResume() {
        super.onResume();

        csr = new ContinuousSpeechRecognizer(context);
        csr.setListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                csr.startRecognition();
            }
        }, 3000);

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
            performYesTransition();

        } else if (results.contains("no")) {
            Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
            performNoTransition();
        } else if (results.contains("done")) {
            Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
            performDoneTransition();
        }
    }

    private void performYesTransition() {
        int state = StateMachine.getCurrentState().getYesAnswered();
        StateMachine.setCurrentState(state);
        ((MainActivity)getActivity()).setupInstructionFragment();
    }

    private void performNoTransition() {
        int state = StateMachine.getCurrentState().getNoAnswered();
        StateMachine.setCurrentState(state);
        ((MainActivity)getActivity()).setupInstructionFragment();
    }

    private void performDoneTransition() {
        int state = StateMachine.getCurrentState().getDoneAnswered();
        StateMachine.setCurrentState(state);
        ((MainActivity)getActivity()).setupInstructionFragment();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            tts.speak(StateMachine.getCurrentState().getQuestion(), TextToSpeech.QUEUE_ADD, null);
        } else {
            Log.e("TTS", "Initialization Failed!");
        }


    }
}
