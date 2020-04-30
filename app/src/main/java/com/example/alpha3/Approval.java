package com.example.alpha3;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Approval extends AppCompatActivity {

    TextInputLayout ref11, scorer1;
    TextInputEditText ref1, ref2, scorer, assistantScorer, lineJudge1, lineJudge2, lineJudge3, lineJudge4, countryRef1, countryRef2, countryScorer, countryAssistant;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);

        ref1 = findViewById(R.id.ref1);
        ref11 = findViewById(R.id.ref11);
        ref2 = findViewById(R.id.ref2);
        scorer = findViewById(R.id.scorer);
        scorer1 = findViewById(R.id.scorer1);
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
        if (TextUtils.isEmpty(ref1.getText().toString()) || TextUtils.isEmpty(scorer.getText().toString())) {
            if (TextUtils.isEmpty(ref1.getText().toString())) {
                ref11.setError("Please enter the name of the 1st referee");
            }
            if (TextUtils.isEmpty(scorer.getText().toString())) {
                scorer1.setError("Please enter your name");
            }
        }
        else {
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
            t.putExtra("sheet", updateScoreSheet());
            startActivity(t);
        }
    }

    public String updateScoreSheet() {
        //get html content
        Intent a = getIntent();
        String htmlAsString = a.getStringExtra("sheet");
        StringBuilder htmlManipulation = new StringBuilder(htmlAsString);

        if (!ref1.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref1name"), htmlManipulation.indexOf("ref1name") + "ref1name".length(), ref1.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref1name"), htmlManipulation.indexOf("ref1name") + "ref1name".length(), "");

        if (!ref2.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref2name"), htmlManipulation.indexOf("ref2name") + "ref2name".length(), ref2.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref2name"), htmlManipulation.indexOf("ref2name") + "ref1name".length(), "");

        if (!scorer.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("scorerName"), htmlManipulation.indexOf("scorerName") + "scorerName".length(), scorer.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("scorerName"), htmlManipulation.indexOf("scorerName") + "scoreName".length(), "");

        if (!assistantScorer.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("asName"), htmlManipulation.indexOf("asName") + "asName".length(), assistantScorer.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("asName"), htmlManipulation.indexOf("asName") + "asName".length(), "");

        if (!countryRef1.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref1count"), htmlManipulation.indexOf("ref1count") + "ref1count".length(), countryRef1.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref1count"), htmlManipulation.indexOf("ref1count") + "ref1count".length(), "");

        if (!countryRef2.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref2count"), htmlManipulation.indexOf("ref2count") + "ref2count".length(), countryRef2.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ref2count"), htmlManipulation.indexOf("ref2count") + "ref2count".length(), "");

        if (!countryScorer.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("scorercount"), htmlManipulation.indexOf("scorercount") + "scorercount".length(), countryScorer.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("scorercount"), htmlManipulation.indexOf("scorercount") + "scorercount".length(), "");

        if (!countryAssistant.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ascount"), htmlManipulation.indexOf("ascount") + "ascount".length(), countryAssistant.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("ascount"), htmlManipulation.indexOf("ascount") + "ascount".length(), "");

        if (!lineJudge1.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej1"), htmlManipulation.indexOf("linej1") + "linej1".length(), lineJudge1.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej1"), htmlManipulation.indexOf("linej1") + "linej1".length(), "");

        if (!lineJudge2.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej2"), htmlManipulation.indexOf("linej2") + "linej2".length(), lineJudge2.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej2"), htmlManipulation.indexOf("linej2") + "linej2".length(), "");

        if (!lineJudge3.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej3"), htmlManipulation.indexOf("linej3") + "linej3".length(), lineJudge3.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej3"), htmlManipulation.indexOf("linej3") + "linej3".length(), "");

        if (!lineJudge4.getText().toString().equals("")) htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej4"), htmlManipulation.indexOf("linej4") + "linej4".length(), lineJudge4.getText().toString());
        else htmlManipulation = htmlManipulation.replace(htmlManipulation.indexOf("linej4"), htmlManipulation.indexOf("linej4") + "linej4".length(), "");

        htmlAsString = htmlManipulation.toString();
        return htmlAsString;
    }
}
