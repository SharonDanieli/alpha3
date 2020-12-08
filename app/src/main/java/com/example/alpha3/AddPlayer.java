package com.example.alpha3;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds a person to the Firebase real-time database
 */

public class AddPlayer extends AppCompatActivity {

    TextInputLayout fnameText1, lnameText1, birthText1, idText1, shirtNum1, countryText1;
    TextInputEditText fnameText, lnameText, birthText, idText, shirtNum, countryText;
    Spinner roles, officials;
    SmartMaterialSpinner teams;
    //SmartMaterialSpinner spEmptyItem;
    List<String> teamsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        fnameText1 = findViewById(R.id.fnameText1);
        lnameText1 = findViewById(R.id.lnameText1);
        birthText1 = findViewById(R.id.birthText1);
        idText1 = findViewById(R.id.idText1);
        //teamNum1 = findViewById(R.id.teamNum1);
        shirtNum1 = findViewById(R.id.shirtNum1);
        countryText1 = findViewById(R.id.countryText1);

        fnameText = findViewById(R.id.fnameText);
        lnameText = findViewById(R.id.lnameText);
        birthText = findViewById(R.id.birthText);
        idText = findViewById(R.id.idText);
        //teamNum = findViewById(R.id.teamNum);
        shirtNum = findViewById(R.id.shirtNum);
        countryText = findViewById(R.id.countryText);

        roles = findViewById(R.id.roles);
        officials = findViewById(R.id.officials);

        officials.setVisibility(View.GONE);
        countryText1.setVisibility(View.GONE);

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
                        countryText1.setVisibility(View.GONE);
                        birthText1.setVisibility(View.VISIBLE);
                        teams.setVisibility(View.VISIBLE);
                        shirtNum1.setVisibility(View.VISIBLE);break;
                    case 1: officials.setVisibility(View.VISIBLE);
                        countryText1.setVisibility(View.GONE);
                        teams.setVisibility(View.VISIBLE);
                        birthText1.setVisibility(View.GONE);
                        shirtNum1.setVisibility(View.GONE); break;
                    case 2: officials.setVisibility(View.GONE);
                        countryText1.setVisibility(View.VISIBLE);
                        birthText1.setVisibility(View.GONE);
                        teams.setVisibility(View.GONE);
                        shirtNum1.setVisibility(View.GONE); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initSpinner();
    }

    private void initSpinner() {
        teams = findViewById(R.id.sp_provinces);
        //spEmptyItem = findViewById(R.id.sp_empty_item);
        final List<String> teamList = new ArrayList<>();

        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game");
        r.child("Teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Team t = ds.getValue(Team.class);
                    teamList.add(t.num + " - " + t.name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        teams.setItem(teamList);

        teams.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
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
        String fn = fnameText.getText().toString();
        String ln = lnameText.getText().toString();
        String b = birthText.getText().toString();
        String id = idText.getText().toString();
        String c = countryText.getText().toString();
        String r = getResources().getStringArray(R.array.officials)[officials.getSelectedItemPosition()];

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Game");

        Person p;

        int t, n;
        switch (roles.getSelectedItemPosition())
        {
            case 0:t = Integer.parseInt(teams.toString());
                n = Integer.parseInt(shirtNum.getText().toString());
                p = new Player(fn, ln, b, id, t, n);
                Toast.makeText(this, "Player has been added", Toast.LENGTH_LONG).show();
                ref = ref.child("Players");
                break;
            case 1: t = Integer.parseInt(teams.toString());
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
