package com.gac2013.tdylf.pocketparamedic;

import android.app.Fragment;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.List;
import java.util.Locale;

public class NextFragment extends Fragment
        implements ContinuousSpeechRecognizer.RecognizedTextListener,
        TextToSpeech.OnInitListener {

    private ContinuousSpeechRecognizer csr;
    private Context context;
    private TextToSpeech tts;
    private ViewGroup rootView;

    /*
    private boolean loaded;
    private SoundPool soundPool;
    private int soundId;
*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        State currentState = StateMachine.getCurrentState();

        int layoutId;
        if (StateMachine.isCurrentStateAsk())
            layoutId = R.layout.instructions;
        else
            layoutId = R.layout.done;

        rootView = (ViewGroup) inflater.inflate(layoutId, container, false);
        ((TextView) rootView.findViewById(R.id.tvInstr)).setText(currentState.getQuestion());
        ((ImageView) rootView.findViewById(R.id.ivInstr)).setImageResource(currentState.getImageResId());

        setupButtonClickListeners(layoutId);

        context = getActivity().getApplicationContext();

        tts = new TextToSpeech(context, this);

        /*
        loaded = false;
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        new LoadMusicAsyncTask().execute();
*/
        return rootView;
    }

    /*
    private class LoadMusicAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            soundId = soundPool.load(
                    getActivity().getApplicationContext(), R.raw.stayinalive, 1);
            return null;
        }
    }*/

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

    }

    @Override
    public void onStop() {
        super.onStop();
        if (csr != null)
            csr.stopRecognition();
    }

    private static final String[] yesIndications = new String[]{
            "yes", "yep", "yeah", "sure", "of course", "course"
    };

    private static final String[] noIndications = new String[]{
            "no", "nope", "not", "not really", "not at all", "nothing really", "nothing at all"
    };

    private static final String[] doneIndications = new String[]{
            "done", "dumb", "dan", "dawn", "okay", "ok", "next", "go on"
    };

    private boolean indicates(List<String> results, String[] patterns) {
        for (String p : patterns)
            if (results.contains(p))
                return true;
        return false;
    }


    @Override
    public void onResults(ArrayList<String> results) {

        String c = "";
        for (String s : results)
            c += s + ", ";
        Toast.makeText(context, c, Toast.LENGTH_SHORT).show();

        if (StateMachine.isCurrentStateAsk()) {
            if (indicates(results, yesIndications)) {
                Toast.makeText(context, "yes", Toast.LENGTH_SHORT).show();
                performYesTransition();

            } else if (indicates(results, noIndications)) {
                Toast.makeText(context, "no", Toast.LENGTH_SHORT).show();
                performNoTransition();
            }
        } else if (StateMachine.isCurrentStateDo()) {
            if (indicates(results, doneIndications)) {
                Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();
                performDoneTransition();
            }
        }
    }

    private void performYesTransition() {
        int state = StateMachine.getCurrentState().getYesAnswered();
        performTransition(state);
    }

    private void performNoTransition() {
        int state = StateMachine.getCurrentState().getNoAnswered();
        performTransition(state);
    }

    private void performDoneTransition() {
        int state = StateMachine.getCurrentState().getDoneAnswered();
        performTransition(state);
    }

    private void performTransition(int state) {
        StateMachine.setCurrentState(state);
        ((MainActivity) getActivity()).setupInstructionScreen();
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This language is not supported");
            } else {
                tts.setOnUtteranceProgressListener(new InternalUtteranceProgressListener());

                HashMap<String, String> myHashAlarm = new HashMap<String, String>();
                myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
                myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");

                tts.speak(StateMachine.getCurrentState().getQuestion(), TextToSpeech.QUEUE_ADD, myHashAlarm);
            }
        } else {
            Log.e("TTS", "Initialization Failed!");
        }


    }



    private class InternalUtteranceProgressListener extends UtteranceProgressListener {
        @Override
        public void onStart(String utteranceId) {
        }

        @Override
        public void onDone(String utteranceId) {
            Log.e(getClass().getName(), "Utterance " + utteranceId + " completed");

            if (StateMachine.getCurrentState().getId() == StateMachine.DO_CPR) {
                playSound();
            } else
                startVrOnUiThread();
        }

        @Override
        public void onError(String utteranceId) {
        }
    }

    private void startVrOnUiThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                csr.startRecognition();
            }
        });
    }

    private void playSound() {

        /*
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // Is the sound loaded already?
        if (loaded)
            soundPool.play(soundId, maxVolume, maxVolume, 1, 0, 1f);*/

        MediaPlayer mp = MediaPlayer.create(getActivity().getApplicationContext(),
                Uri.parse("android.resource://com.gac2013.tdylf.pocketparamedic/raw/stayalive"));


        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                startVrOnUiThread();
            }

        });

        mp.start();
    }
}
