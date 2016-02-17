package com.newman.ryan.votingmachinesimullator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class VotingActivity extends AppCompatActivity {
    private RadioGroup candidateRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] candidates;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        candidateRadioGroup = (RadioGroup) findViewById(R.id.group_Candidates);

        candidates = getIntent().getExtras().getStringArray("candidates");

        shuffleCandidates(candidates);

        addCandidateButtons(candidates);
    }

    private void addCandidateButtons(String[] candidates) {
        for (int i = 0; i < candidates.length; ++i) {

            RadioButton candidateButton = new RadioButton(this);

            candidateButton.setText(candidates[i]);

            candidateRadioGroup.addView(candidateButton);
        }
    }

    private static void shuffleCandidates(String[] candidates) {
        Random random = new Random();

        for (int i = candidates.length - 1; i > 0; --i) {
            int index = random.nextInt(i + 1);

            String temp = candidates[index];
            candidates[index] = candidates[i];
            candidates[i] = temp;
        }

        Log.d("Shuffled Array", Arrays.toString(candidates));
    }
}
