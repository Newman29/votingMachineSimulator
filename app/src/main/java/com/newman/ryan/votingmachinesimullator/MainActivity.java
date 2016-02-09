package com.newman.ryan.votingmachinesimullator;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private String[] republicanCandidates = {
        "Jeb Bush",
        "Ben Carson",
        "Chris Christie",
        "Ted Cruz",
        "Carly Fiorina",
        "John Kasich",
        "Marco Rubio",
        "Donald Trump"
    };

    private String[] democratCandidates = {
        "Bernie Sanders",
        "Hillary Clinton"
    };

    //Declare the SharedPreferences and Editor
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = this.getSharedPreferences("com.newman.ryan.votingmachinesimullator",
                Context.MODE_PRIVATE);
        editor = getPreferences(MODE_PRIVATE).edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the flag to false while development for debugging purposes
        prefs.edit().putBoolean("firstTimeUsing", true).apply();

        //Try and detect if this is the users first time launching the app
        if (prefs.getBoolean("firstTimeUsing", true)) {
            Log.d("First time launching: ", "yes");

            addCandidates(democratCandidates);
            addCandidates(republicanCandidates);

            createPasswordDialog();

            prefs.edit().putBoolean("firstTimeUsing", false).apply();
        } else {
            Log.d("First time launching: ", "no");
        }
    }

    /*
    Add the candidates to SharedPreferences with the key being their name with no spaces and
    an initial value of 0. This will be changed with each vote for that candidate by one
     */
    private void addCandidates (String[] candidates) {
        for (String candidate : candidates) {
            boolean hasCandidate = prefs.contains(candidate.replace(" ", ""));

            if (!hasCandidate) {
                editor.putInt(candidate.replace(" ", ""), 0);
            }
        }

        editor.commit();
    }

    private void createPasswordDialog() {
        final AlertDialog passwordDialog = new AlertDialog.Builder(this)
                .setTitle("Enter An Administrative Password")
                .setView(R.layout.admin_password_dialog)
                .create();

        passwordDialog.show();

        Button confirmPassword = (Button) passwordDialog.findViewById(R.id.btn_confirmPassword);
        confirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // handle button click
                EditText passwordInput = (EditText) passwordDialog.findViewById(R.id.edit_adminPassword);
                String password = passwordInput.getText().toString();
            }
        });
    }
}
