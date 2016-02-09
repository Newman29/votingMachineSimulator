package com.newman.ryan.votingmachinesimullator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
