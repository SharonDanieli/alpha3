package com.example.alpha3;

import android.app.TimePickerDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GameInfo extends AppCompatActivity {

    Button time;
    TextInputEditText nameText, cityText, codeText, hallText, numberText;
    TextInputLayout nameText1, cityText1, codeText1, hallText1, numberText1, firstT, secondT;
    SwitchMaterial menOrWomen;
    AutoCompleteTextView select1, select2, phase, category;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null)
            signIn();

        menOrWomen = findViewById(R.id.menOrWomen);
        select1 = findViewById(R.id.select1);
        select2 = findViewById(R.id.select2);
        firstT = findViewById(R.id.firstT);
        secondT = findViewById(R.id.secondT);

        nameText1 = findViewById(R.id.nameText1);
        cityText1 = findViewById(R.id.cityText1);
        codeText1 = findViewById(R.id.codeText1);
        hallText1 = findViewById(R.id.hallText1);
        numberText1 = findViewById(R.id.numText1);

        nameText = findViewById(R.id.nameText);
        cityText = findViewById(R.id.cityText);
        codeText = findViewById(R.id.codeText);
        hallText = findViewById(R.id.hallText);
        numberText = findViewById(R.id.numText);

        time = findViewById(R.id.time);

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

        String locale = getResources().getConfiguration().locale.getISO3Country();
        codeText.setText(locale);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Sign out");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = item.getTitle().toString();
        if (title.equals("Sign out"))
        {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Toast.makeText(GameInfo.this, "You have signed out", Toast.LENGTH_LONG).show();
                            signIn();
                        }
                    });
        }
        return super.onOptionsItemSelected(item);
    }


    private void signIn() {
        Intent t = new Intent(this, SignUp.class);
        startActivity(t);
    }

    public void chooseTime(View view) {
        TimePickerDialog picker = new TimePickerDialog(GameInfo.this,android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                        time.setText(sHour + ":" + sMinute);
                        time.setError(null);
                        time.clearFocus();
                    }
                }, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true);
        picker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        picker.show();
    }

    public void next(View view) {

        if (TextUtils.isEmpty(cityText.getText().toString()) || TextUtils.isEmpty(codeText.getText().toString()) || TextUtils.isEmpty(hallText.getText().toString()) || TextUtils.isEmpty(time.getText().toString()) || TextUtils.isEmpty(select1.getText().toString()) || TextUtils.isEmpty(select2.getText().toString())) {
            if (TextUtils.isEmpty(cityText.getText().toString())) {
                cityText1.setError("Please enter city");
            }
            if (TextUtils.isEmpty(codeText.getText().toString())) {
                codeText1.setError("Please enter country code");
            }
            if (TextUtils.isEmpty(hallText.getText().toString())) {
                hallText1.setError("Please enter the hall name");
            }
            if (time.getText().toString().equals("Choose time")) {
                time.setError("Please enter the scheduled start time");
            }
            if (TextUtils.isEmpty(select1.getText().toString())) {
                firstT.setError("Please choose a team");
            }
            if (TextUtils.isEmpty(select2.getText().toString())) {
                secondT.setError("Please choose a team");
            }
        }
        else {
            int mNumber;
            if (TextUtils.isEmpty(numberText.getText().toString())) {
                 mNumber = 0;
            }
            else {
                mNumber = Byte.parseByte(numberText.getText().toString());
            }
            Info info = new Info(nameText.getText().toString(),
                    cityText.getText().toString(),
                    codeText.getText().toString(),
                    hallText.getText().toString(),
                    phase.toString(),
                    mNumber,
                    category.toString(),
                    menOrWomen.isChecked(),
                    time.getText().toString(),
                    select1.getText().toString(),
                    select2.getText().toString());

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Games");
            String id = ref.push().getKey();
            ref = ref.child(id).child("Info");
            ref.setValue(info);

            Intent intent = new Intent(this, TeamsInfo.class);
            intent.putExtra("sheet", updateScoreSheet());
            intent.putExtra("team1", info.team1);
            intent.putExtra("team2", info.team2);
            intent.putExtra("id", id);
            startActivity(intent);
        }
    }

    public String updateScoreSheet() {
        //get html content
        String htmlAsString = getString(R.string.html);
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDate.format(new Date());

        htmlAsString = htmlAsString.replace("nameComp", nameText.getText().toString());
        htmlAsString = htmlAsString.replace("cityText", cityText.getText().toString());
        htmlAsString = htmlAsString.replace("codeText", codeText.getText().toString());
        htmlAsString = htmlAsString.replace("hallText", hallText.getText().toString());
        if (phase.toString().equals("Pool/Phase")) htmlAsString = htmlAsString.replace("poph","");
        else htmlAsString = htmlAsString.replace("poph", phase.toString());
        htmlAsString = htmlAsString.replace("matchNum", numberText.getText().toString());
        if (menOrWomen.isChecked()) htmlAsString = htmlAsString.replace("menWo", "Women");
        else htmlAsString = htmlAsString.replace("menWo", "Men");
        if (category.toString().equals("Category")) htmlAsString = htmlAsString.replace("cateText","");
        else htmlAsString = htmlAsString.replace("cateText", category.toString());
        htmlAsString = htmlAsString.replace("dateText", date.toString());
        htmlAsString = htmlAsString.replace("timeText", time.getText().toString());

        htmlAsString = htmlAsString.replaceAll("team1", select1.toString());
        htmlAsString = htmlAsString.replaceAll("team2", select2.toString());

        return htmlAsString;
    }
}
