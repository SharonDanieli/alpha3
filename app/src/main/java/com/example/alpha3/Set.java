package com.example.alpha3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public class Set extends AppCompatActivity {

    TextView name1, name2, set1, set2;
    Button points1, points2;
    RadioButton serve1, serve2;

    int pt1, pt2, limit, s1, s2;

    public static final int LIMIT = 25;
    public static final int SETLIMIT = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        points1 = findViewById(R.id.points1);
        points2 = findViewById(R.id.points2);
        set1 = findViewById(R.id.set1);
        set2 = findViewById(R.id.set2);
        serve1 = findViewById(R.id.serve1);
        serve2 = findViewById(R.id.serve2);



        pt1 = 0;
        pt2 = 0;
        s1 = 0;
        s2 = 0;
        limit = LIMIT;

        Intent t = getIntent();
        if (t != null)
        {
            name1.setText(t.getStringExtra("name1"));
            name2.setText(t.getStringExtra("name2"));
        }

        points1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pt1 < limit - 1)
                {
                    pt1++;
                    if (pt1 == pt2 && pt2 >= LIMIT - 1)
                        limit++;
                }
                else {
                    pt1 = 0;
                    pt2 = 0;
                    s1++;
                    set1.setText("" + s1);
                    set2.setText("" + s2);
                    points2.setText("" + pt2);
                    limit = LIMIT;
                    checkWin();
                }
                if (pt1 == pt2 && pt1 >= LIMIT - 1)
                    limit++;
                points1.setText("" + pt1);
            }
        });
        points2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pt2 < limit - 1)
                {
                    pt2++;
                    if (pt1 == pt2 && pt2 >= LIMIT - 1)
                        limit++; // r.i.p %
                }
                else {
                    pt1 = 0;
                    pt2 = 0;
                    s2++;
                    set1.setText("" + s1);
                    set2.setText("" + s2);
                    points1.setText("" + pt1);
                    limit = LIMIT;
                    checkWin();
                }
                points2.setText("" + pt2);
            }
        });
    }
    public void checkWin()
    {
        Intent t = new Intent(this, Results.class);
        if (s1 == SETLIMIT) {
            t.putExtra("winner", name1.getText().toString());
            startActivity(t);
        }
        else if (s2 == SETLIMIT) {
            t.putExtra("winner", name2.getText().toString());
            startActivity(t);
        }
    }
}
