package com.example.alpha3;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Approval extends AppCompatActivity {

    EditText ref1;
    EditText ref2;
    EditText scorer;
    EditText assistantScorer;
    EditText lineJudge1;
    EditText lineJudge2;
    EditText lineJudge3;
    EditText lineJudge4;
    EditText countryRef1;
    EditText countryRef2;
    EditText countryScorer;
    EditText countryAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        ref1 = findViewById(R.id.ref1);
        ref2 = findViewById(R.id.ref2);
        scorer = findViewById(R.id.scorer);
        assistantScorer = findViewById(R.id.assistantScorer);
        lineJudge1 = findViewById(R.id.lineJudge1);
        lineJudge2 = findViewById(R.id.lineJudge2);
        lineJudge3 = findViewById(R.id.lineJudge3);
        lineJudge4 = findViewById(R.id.lineJudge4);
        countryRef1 = findViewById(R.id.countryRef1);
        countryRef2 = findViewById(R.id.countryRef2);
        countryScorer = findViewById(R.id.countryScorer);
        countryAssistant = findViewById(R.id.countryAssistant);
    }

    public void next(View view) {
        Intent t = new Intent(this, Set.class);
        t.replaceExtras(getIntent());
        t.putExtra("ref1", ref1.getText().toString());
        t.putExtra("ref2", ref2.getText().toString());
        t.putExtra("scorer", scorer.getText().toString());
        t.putExtra("assistantScorer", assistantScorer.getText().toString());
        t.putExtra("lineJudge1", lineJudge1.getText().toString());
        t.putExtra("lineJudge2", lineJudge2.getText().toString());
        t.putExtra("lineJudge3", lineJudge3.getText().toString());
        t.putExtra("lineJudge4", lineJudge4.getText().toString());
        t.putExtra("countryRef1", countryRef1.getText().toString());
        t.putExtra("countryRef2", countryRef2.getText().toString());
        t.putExtra("countryScorer", countryScorer.getText().toString());
        t.putExtra("countryAssistant", countryAssistant.getText().toString());

        startActivity(t);
    }
}
