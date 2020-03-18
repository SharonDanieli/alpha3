package com.example.alpha3;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    TextView[][] resultsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsTable = new TextView[6][9];

        resultsTable[0][0] = findViewById(R.id.textView13);
        resultsTable[0][1] = findViewById(R.id.textView14);
        resultsTable[0][2] = findViewById(R.id.textView15);
        resultsTable[0][3] = findViewById(R.id.textView16);
        resultsTable[0][4] = findViewById(R.id.textView17);
        resultsTable[0][5] = findViewById(R.id.textView18);
        resultsTable[0][6] = findViewById(R.id.textView19);
        resultsTable[0][7] = findViewById(R.id.textView20);
        resultsTable[0][8] = findViewById(R.id.textView21);
        resultsTable[1][0] = findViewById(R.id.textView22);
        resultsTable[1][1] = findViewById(R.id.textView23);
        resultsTable[1][2] = findViewById(R.id.textView24);
        resultsTable[1][3] = findViewById(R.id.textView25);
        resultsTable[1][4] = findViewById(R.id.textView26);
        resultsTable[1][5] = findViewById(R.id.textView27);
        resultsTable[1][6] = findViewById(R.id.textView28);
        resultsTable[1][7] = findViewById(R.id.textView29);
        resultsTable[1][8] = findViewById(R.id.textView30);
        resultsTable[2][0] = findViewById(R.id.textView31);
        resultsTable[2][1] = findViewById(R.id.textView32);
        resultsTable[2][2] = findViewById(R.id.textView33);
        resultsTable[2][3] = findViewById(R.id.textView34);
        resultsTable[2][4] = findViewById(R.id.textView35);
        resultsTable[2][5] = findViewById(R.id.textView36);
        resultsTable[2][6] = findViewById(R.id.textView37);
        resultsTable[2][7] = findViewById(R.id.textView38);
        resultsTable[2][8] = findViewById(R.id.textView39);
        resultsTable[3][0] = findViewById(R.id.textView40);
        resultsTable[3][1] = findViewById(R.id.textView41);
        resultsTable[3][2] = findViewById(R.id.textView42);
        resultsTable[3][3] = findViewById(R.id.textView43);
        resultsTable[3][4] = findViewById(R.id.textView44);
        resultsTable[3][5] = findViewById(R.id.textView45);
        resultsTable[3][6] = findViewById(R.id.textView46);
        resultsTable[3][7] = findViewById(R.id.textView47);
        resultsTable[3][8] = findViewById(R.id.textView48);
        resultsTable[4][0] = findViewById(R.id.textView49);
        resultsTable[4][1] = findViewById(R.id.textView50);
        resultsTable[4][2] = findViewById(R.id.textView51);
        resultsTable[4][3] = findViewById(R.id.textView52);
        resultsTable[4][4] = findViewById(R.id.textView53);
        resultsTable[4][5] = findViewById(R.id.textView54);
        resultsTable[4][6] = findViewById(R.id.textView55);
        resultsTable[4][7] = findViewById(R.id.textView56);
        resultsTable[4][8] = findViewById(R.id.textView57);
        resultsTable[5][0] = findViewById(R.id.textView58);
        resultsTable[5][1] = findViewById(R.id.textView59);
        resultsTable[5][2] = findViewById(R.id.textView60);
        resultsTable[5][3] = findViewById(R.id.textView61);
        resultsTable[5][4] = findViewById(R.id.textView62);
        resultsTable[5][5] = findViewById(R.id.textView63);
        resultsTable[5][6] = findViewById(R.id.textView64);
        resultsTable[5][7] = findViewById(R.id.textView65);
        resultsTable[5][8] = findViewById(R.id.textView66);
    }
}
