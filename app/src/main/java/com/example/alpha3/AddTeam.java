package com.example.alpha3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

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
 * Adds a team to the Firebase real-time database
 */

public class AddTeam extends AppCompatActivity {

    TextInputLayout nameText1, numText1, emailText1;
    TextInputEditText nameText, numText, emailText;
    SmartMaterialSpinner cityText;
    List<String> provinceList;
    /**
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        nameText1 = findViewById(R.id.nameText1);
        numText1 = findViewById(R.id.numText1);
        emailText1 = findViewById(R.id.emailText1);

        nameText = findViewById(R.id.nameText);
        numText = findViewById(R.id.numText);
        emailText = findViewById(R.id.emailText);

        initSpinner();
    }

    private void initSpinner() {
        cityText = findViewById(R.id.cityText);
        //spEmptyItem = findViewById(R.id.sp_empty_item);
        provinceList = new ArrayList<>();

        provinceList.add("Be'er Sheva");
        provinceList.add("Tel Aviv");
        provinceList.add("Kfar Saba");
        provinceList.add("Bat Yam");

        cityText.setItem(provinceList);

        cityText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**
     * Uploads the team's information to the database under the teams branch
     */

    public void submit(View view) {
        final String name = nameText.getText().toString();
        final String city = cityText.toString();
        final int num = Integer.parseInt(numText.getText().toString());
        final String email = emailText.getText().toString();
        final DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game").child("Teams");

        r.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("" + num).getValue() == null)
                {
                    Team t = new Team(num, name, city, email);
                    r.child("" + num).setValue(t);
                    finish();
                    Toast.makeText(AddTeam.this, "Team added successfully", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(AddTeam.this, "Team already exists", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
