package com.example.alpha3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Adds a person to the Firebase real-time database
 */

public class AddPlayer extends AppCompatActivity {

    EditText fname, lname, birth, idText, team, num, country;
    Spinner roles, officials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        birth = findViewById(R.id.birth);
        idText = findViewById(R.id.id);
        team = findViewById(R.id.team);
        num = findViewById(R.id.num);
        country = findViewById(R.id.country);

        roles = findViewById(R.id.roles);
        officials = findViewById(R.id.officials);

        officials.setVisibility(View.GONE);
        country.setVisibility(View.GONE);

        ArrayAdapter<String> rolesAdp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.roles));
        ArrayAdapter<String> officialsAdp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.officials));

        roles.setAdapter(rolesAdp);
        officials.setAdapter(officialsAdp);

        roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i)
                {
                    case 0: officials.setVisibility(View.GONE);
                        country.setVisibility(View.GONE);
                        birth.setVisibility(View.VISIBLE);
                        team.setVisibility(View.VISIBLE);
                        num.setVisibility(View.VISIBLE);break;
                    case 1: officials.setVisibility(View.VISIBLE);
                        country.setVisibility(View.GONE);
                        team.setVisibility(View.VISIBLE);
                        birth.setVisibility(View.GONE);
                        num.setVisibility(View.GONE); break;
                    case 2: officials.setVisibility(View.GONE);
                        country.setVisibility(View.VISIBLE);
                        birth.setVisibility(View.GONE);
                        team.setVisibility(View.GONE);
                        num.setVisibility(View.GONE); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Uploads the person's information to the database under the appropriate branch associated with his role
     */

    public void submit(View view) {
        String fn = fname.getText().toString();
        String ln = lname.getText().toString();
        String b = birth.getText().toString();
        String id = idText.getText().toString();
        String c = country.getText().toString();
        String r = getResources().getStringArray(R.array.officials)[officials.getSelectedItemPosition()];

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Game");

        Person p;

        int t, n;
        switch (roles.getSelectedItemPosition())
        {
            case 0:t = Integer.parseInt(team.getText().toString());
                n = Integer.parseInt(num.getText().toString());
                p = new Player(fn, ln, b, id, t, n);
                Toast.makeText(this, "Player has been added", Toast.LENGTH_LONG).show();
                ref = ref.child("Players");
                break;
            case 1: t = Integer.parseInt(team.getText().toString());
                p = new Official(fn, ln, id, r, t);
                Toast.makeText(this, "Official has been added", Toast.LENGTH_LONG).show();
                ref = ref.child("Officials"); break;
            case 2: p = new Referee(fn, ln, id, c);
                Toast.makeText(this, "Referee has been added", Toast.LENGTH_LONG).show();
                ref = ref.child("Referees"); break;
            default: p = null;
        }

        ref.child(id).setValue(p);
        finish();
    }
}
