package com.example.alpha3;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GameInfo extends AppCompatActivity {

    Button time;
    Spinner select1, select2, phase, category;
    EditText nameText, cityText, codeText, hallText, numberText;
    Switch menOrWomen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        menOrWomen = findViewById(R.id.menOrWomen);

        nameText = findViewById(R.id.nameText);
        cityText = findViewById(R.id.cityText);
        codeText = findViewById(R.id.codeText);
        hallText = findViewById(R.id.hallText);
        numberText = findViewById(R.id.numberText);

        time = findViewById(R.id.time);
        select1  = findViewById(R.id.select1);
        select2  = findViewById(R.id.select2);
        phase  = findViewById(R.id.phase);
        category  = findViewById(R.id.category);

        ArrayAdapter<CharSequence> phaseAdapter = ArrayAdapter.createFromResource(this,
                R.array.phase, android.R.layout.simple_spinner_item);
        phaseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        phase.setAdapter(phaseAdapter);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryAdapter);

        final List<String> teamList = new ArrayList<>();

        DatabaseReference r = FirebaseDatabase.getInstance().getReference("Game");
        r.child("Teams").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Team t = ds.getValue(Team.class);
                    teamList.add(t.num + " - " + t.name);
                }
                ArrayAdapter<String> teamAdp = new ArrayAdapter<>(GameInfo.this, R.layout.support_simple_spinner_dropdown_item, teamList);
                select1.setAdapter(teamAdp);
                select2.setAdapter(teamAdp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void chooseTime(View view) {
        TimePickerDialog picker = new TimePickerDialog(GameInfo.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        time.setText(sHour + ":" + sMinute);
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        picker.show();
    }

    public void next(View view) {

        Info info = new Info(nameText.getText().toString(),
                cityText.getText().toString(),
                codeText.getText().toString(),
                hallText.getText().toString(),
                (byte)phase.getSelectedItemPosition(),
                Byte.parseByte(numberText.getText().toString()),
                (byte)category.getSelectedItemPosition(),
                menOrWomen.isChecked(),
                time.getText().toString(),
                (String)select1.getSelectedItem(),
                (String)select2.getSelectedItem());

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Games");
        String id = ref.push().getKey();
        ref = ref.child(id).child("Info");
        ref.setValue(info);

        Intent t = new Intent(this, TeamsInfo.class);

        t.putExtra("team1", info.team1);
        t.putExtra("team2", info.team2);
        t.putExtra("id", id);
        startActivity(t);
    }
}
