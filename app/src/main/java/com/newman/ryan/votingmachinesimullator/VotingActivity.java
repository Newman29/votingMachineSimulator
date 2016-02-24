package com.newman.ryan.votingmachinesimullator;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

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
                if (candidateRadioGroup.getCheckedRadioButtonId() == -1) {
                    //None is selected
                    createInvalidSelectionDialog();
                } else {
                    String selectedCandidate = getSelection();
                    Log.d("Selected Candidate ", selectedCandidate);

                    if (submitVote(selectedCandidate)) {
                        createThankYouDialog();
                    } else {
                        createErrorSubmittingVoteDialog();
                    }
                }
            }
        });
    }

    private boolean submitVote(String selectedCandidate) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            String sharedPrefsKey = selectedCandidate.replace(" ", "");
            boolean hasKeyValue = prefs.contains(sharedPrefsKey);

            if (!hasKeyValue) {
                return false;
            } else {
                int voteCount = prefs.getInt(sharedPrefsKey, -1);

                Log.d("Pre count ", Integer.toString(voteCount));

                if (voteCount == -1) {
                    return false;
                }

                ++voteCount;

                Log.d("Post count ", Integer.toString(voteCount));

                editor.putInt(sharedPrefsKey, voteCount);
                editor.apply();
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createErrorSubmittingVoteDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Error Submitting Vote")
                .setMessage(getString(R.string.errorSubmittingVote))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void createThankYouDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Vote Submitted")
                .setMessage(getString(R.string.voteConfirmation))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private String getSelection() {
        int selectionID = candidateRadioGroup.getCheckedRadioButtonId();
        View selectedRadioButton = candidateRadioGroup.findViewById(selectionID);
        int radioID = candidateRadioGroup.indexOfChild(selectedRadioButton);
        RadioButton selection = (RadioButton) candidateRadioGroup.getChildAt(radioID);
        return (String) selection.getText();
    }

    private void createInvalidSelectionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Invalid Selection")
                .setMessage(getString(R.string.invalidSelection))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
            candidateButton.setPadding(5, 50, 25, 25);

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
