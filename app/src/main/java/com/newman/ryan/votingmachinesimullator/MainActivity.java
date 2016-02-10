package com.newman.ryan.votingmachinesimullator;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import java.io.File;

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
    private static final int minPasswordLength = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = this.getSharedPreferences("com.newman.ryan.votingmachinesimullator",
                Context.MODE_PRIVATE);
        editor = getPreferences(MODE_PRIVATE).edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set the flag to false while development for debugging purposes
        prefs.edit().putBoolean("firstTimeUsing", true).apply();

        if (isRooted()) {
            createUnsecureDialog();
        } else {
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
    }

    /*
    Add the candidates to SharedPreferences with the key being their name with no spaces and
    an initial value of 0. This will be changed with each vote for that candidate by one
     */
    private void addCandidates(String[] candidates) {
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

                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.invalidPasswordToast, Toast.LENGTH_SHORT).show();
                } else if (password.contains(" ") || password.length() < minPasswordLength) {
                    Toast.makeText(getApplicationContext(), R.string.invalidPassword, Toast.LENGTH_SHORT).show();
                } else {
                    editor.putString("adminPassword", password).commit();
                    passwordDialog.dismiss();
                }
            }
        });
    }

    private void createUnsecureDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Unsecure Phone")
                .setMessage(R.string.unsecureMessage)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        uninstallApp();
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void uninstallApp() {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:com.newman.ryan.votingmachinesimullator"));
        startActivity(intent);
    }

    /**
     * Checks if the device is rooted.
     *
     * http://stackoverflow.com/questions/19288463/how-to-check-if-android-phone-is-rooted
     *
     * @return true if device is rooted, otherwise false
     */
    private static boolean isRooted() {
        return findBinary("su");
    }

    public static boolean findBinary(String binaryName) {
        boolean found = false;

        String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};

        for (String where : places) {
            if (new File(where + binaryName).exists()) {
                found = true;
                break;
            }
        }

        return found;
    }
}
