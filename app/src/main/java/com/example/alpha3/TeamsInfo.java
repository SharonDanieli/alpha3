package com.example.alpha3;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TeamsInfo extends AppCompatActivity {

    DatabaseReference r;


    SignaturePad signaturePad;
    TextView teamName1, teamName2;
    ListView team1, team2;
    Button signButton;

    List<String> teamList;
    List<Player> players1;
    List<Player> players2;
    ArrayAdapter<String> teamAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_info);

        signButton = findViewById(R.id.signButton);

        teamName1 = findViewById(R.id.teamName1);
        teamName2 = findViewById(R.id.teamName2);
        team1 = findViewById(R.id.team1);
        team2 = findViewById(R.id.team2);

        signaturePad = findViewById(R.id.signature_pad);

        teamList = new ArrayList<>();
        players1 = new ArrayList<>();
        players2 = new ArrayList<>();
        r = FirebaseDatabase.getInstance().getReference("Game");

        Intent t = getIntent();

        teamName1.setText(t.getStringExtra("team1"));
teamName2.setText(t.getStringExtra("team2"));

        int id = Integer.parseInt(t.getStringExtra("team1").split(" - ")[0]);
        r.child("Players").orderByChild("team").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                players1.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    players1.add(ds.getValue(Player.class));
                PlayerList adp = new PlayerList(TeamsInfo.this, players1);
                team1.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        id = Integer.parseInt(t.getStringExtra("team2").split(" - ")[0]);
        r.child("Players").orderByChild("team").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                players2.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    players2.add(ds.getValue(Player.class));
                PlayerList adp = new PlayerList(TeamsInfo.this, players2);
                team2.setAdapter(adp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sign(View view) {

        Bitmap signature = signaturePad.getTransparentSignatureBitmap();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        StorageReference r = FirebaseStorage.getInstance().getReference(ref.push().getKey() + ".png");

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG, 100, bao); // bmp is bitmap from user image file
        byte[] image = bao.toByteArray();

        r.putBytes(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(TeamsInfo.this, "Uploaded", Toast.LENGTH_LONG).show();
            }
        });

        Toast.makeText(TeamsInfo.this, ":D", Toast.LENGTH_SHORT).show();
        Intent t = new Intent(this, Approval.class);
        t.replaceExtras(getIntent());
        startActivity(t);
    }

}
