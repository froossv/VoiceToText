package com.vathsan.voicetotext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

 public class MainActivity extends AppCompatActivity {

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                finish();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        final EditText editText = findViewById(R.id.editText);
        final ImageButton mic = findViewById(R.id.imageButton3);
        final Spinner dropDown = findViewById(R.id.dropDown);

        final SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }

            @Override
            public void onError(int error) {
                editText.setText("Sorry! Please Try Again.");
            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null){
                    editText.setText(matches.get(0));
                }
            }
        });

        List<String> langList = new ArrayList<String>();
        langList.add("English"); langList.add("Hindi"); langList.add("Tamil");
        ArrayAdapter<String> langListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,langList);
        langListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDown.setAdapter(langListAdapter);
        dropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US"); break;
                    case 1: speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"hi-IN"); break;
                    case 2: speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ta-IN"); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
            }
        });

        mic.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                        editText.setHint("Start Talking to View Input");
                        break;
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        editText.setText("");
                        editText.setHint("Listening..");
                        break;
                }
                return false;
            }
        });
    }


}
