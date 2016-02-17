package com.newman.ryan.votingmachinesimullator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
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
        Button cancelVote, submitVote;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        candidateRadioGroup = (RadioGroup) findViewById(R.id.group_Candidates);
        cancelVote = (Button) findViewById(R.id.btn_cancelVote);
        submitVote = (Button) findViewById(R.id.btn_submitVote);

        candidates = getIntent().getExtras().getStringArray("candidates");

        setCancelClickListener(cancelVote);
        setSubmitClickListener(submitVote);

        shuffleCandidates(candidates);

        addCandidateButtons(candidates);
    }

    private void setSubmitClickListener(Button submitVote) {
        submitVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setCancelClickListener(Button cancelVote) {
        cancelVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addCandidateButtons(String[] candidates) {
        for (String candidate : candidates) {

            RadioButton candidateButton = new RadioButton(this);

            candidateButton.setText(candidate);
            candidateButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
            candidateButton.setPadding(25, 25, 25, 25);

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
