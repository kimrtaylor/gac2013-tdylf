package com.gac2013.tdylf.pocketparamedic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class NextFragment extends Fragment
        implements ContinuousSpeechRecognizer.RecognizedTextListener,
        TextToSpeech.OnInitListener {

    private ContinuousSpeechRecognizer csr;
    private Context context;
    private TextToSpeech tts;
    private ViewGroup rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        FragmentManager fm = getFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); i++)
            Log.e(getClass().getName(), i + " -> " + fm.getBackStackEntryAt(i).getName());

        State currentState = StateMachine.getCurrentState();

        int layoutId;
        if (StateMachine.isCurrentStateAsk())
            layoutId = R.layout.instructions;
        else
            layoutId = R.layout.done;

        rootView = (ViewGroup) inflater.inflate(layoutId, container, false);
        ((TextView) rootView.findViewById(R.id.tvInstr)).setText("" + currentState.getId());
        ((ImageView) rootView.findViewById(R.id.ivInstr)).setImageResource(currentState.getImageResId());

        setupButtonClickListeners(layoutId);

        context = getActivity().getApplicationContext();

        tts = new TextToSpeech(context, this);

        return rootView;
    }

    private void setupButtonClickListeners(int layoutId) {
        if (layoutId == R.layout.done) {
            Button btDone = (Button) rootView.findViewById(R.id.bDoneInstr);
            btDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performDoneTransition();
                }
            });
        } else {
            Button btYes = (Button) rootView.findViewById(R.id.bYesInstr);
            btYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performYesTransition();
                }
            });

            Button btNo = (Button) rootView.findViewById(R.id.bNoInstr);
            btNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    performNoTransition();
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        csr = new ContinuousSpeechRecognizer(context);
        csr.setListener(this);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                csr.startRecognition();
            }
        }, 3000);
*/
    }

    @Override
    public void onStop() {
        super.onStop();
        csr.stopRecognition();
    }

    @Override
    public void onResults(ArrayList<String> results) {

        String c = "";
        for (String s: results)
            c += s;
        Toast.makeText(context, c, Toast.LENGTH_SHORT).show();

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
        ((MainActivity) getActivity()).setupInstructionFragment();
    }

    private void performNoTransition() {
        int state = StateMachine.getCurrentState().getNoAnswered();
        StateMachine.setCurrentState(state);
        ((MainActivity) getActivity()).setupInstructionFragment();
    }

    private void performDoneTransition() {
        int state = StateMachine.getCurrentState().getDoneAnswered();
        StateMachine.setCurrentState(state);
        ((MainActivity) getActivity()).setupInstructionFragment();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");
            } else {
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        Log.e(getClass().getName(), "Utterance " + utteranceId + " completed");
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                csr.startRecognition();
                            }
                        });
                    }

                    @Override
                    public void onError(String utteranceId) {
                    }
                });

                HashMap<String, String> myHashAlarm = new HashMap<String, String>();
                myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
                myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");

                tts.speak(StateMachine.getCurrentState().getQuestion(), TextToSpeech.QUEUE_ADD, myHashAlarm);
            }
        } else {
            Log.e("TTS", "Initialization Failed!");
        }


    }
}
