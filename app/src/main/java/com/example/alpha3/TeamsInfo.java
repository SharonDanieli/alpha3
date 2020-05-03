package com.example.alpha3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class TeamsInfo extends AppCompatActivity {

    DatabaseReference r;

    SignaturePad signaturePadTc1, signaturePadTc2, signaturePadC1, signaturePadC2;
    TextView teamName1, teamName2;
    ListView team1, team2;
    Button signButton;
    List<String> teamList;
    List<Player> players1;
    List<Player> players2;

    /**
     * Links the variables in Java to the components in xml and initializes the attributes. Updates player lists according to the database.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teams_info);

        signButton = findViewById(R.id.signButton);

        teamName1 = findViewById(R.id.teamName1);
        teamName2 = findViewById(R.id.teamName2);
        team1 = findViewById(R.id.team1);
        team2 = findViewById(R.id.team2);

        signaturePadTc1 = findViewById(R.id.signature_tc1);
        signaturePadTc2 = findViewById(R.id.signature_c1);
        signaturePadC1 = findViewById(R.id.signature_tc2);
        signaturePadC2 = findViewById(R.id.signature_c2);

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

    /**
     * Moves to the next activity and calls the method {@link #updateScoreSheet()}.
     */
    public void sign(View view) {
        Intent t = new Intent(this, Approval.class);
        t.replaceExtras(getIntent());
        t.putExtra("sheet", updateScoreSheet());
        startActivity(t);
    }

    /**
     * Updates the HTML file.
     * @return The updated HTML file.
     */
    public String updateScoreSheet() {
        //get html content
        Intent a = getIntent();
        String htmlAsString = a.getStringExtra("sheet");
        StringBuilder htmlManipulation = new StringBuilder(htmlAsString);


        int start = htmlManipulation.indexOf("num1-1");
        for (int i = 0; i < players1.size(); i++) {
            htmlManipulation = htmlManipulation.replace(start, start + "num1-1".length(), players1.get(i).toString());
            start = htmlManipulation.indexOf("num1-1");
        }

        for (int j = 0; j < 12 - players1.size(); j++) {
            htmlManipulation = htmlManipulation.replace(start, start + "num1-1".length(), "");
            start = htmlManipulation.indexOf("num1-1");
        }

        int start2 = htmlManipulation.indexOf("num2-1");
        for (int i = 0; i < players2.size(); i++) {
            htmlManipulation = htmlManipulation.replace(start2, start2 + "num2-1".length(), players2.get(i).toString());
            start2 = htmlManipulation.indexOf("num2-1");
        }

        for (int j = 0; j < 12 - players2.size(); j++) {
            htmlManipulation = htmlManipulation.replace(start2, start2 + "num2-1".length(), "");
            start2 = htmlManipulation.indexOf("num2-1");
        }


        Bitmap signature1 = signaturePadTc1.getTransparentSignatureBitmap();
        Bitmap signature2 = signaturePadTc2.getTransparentSignatureBitmap();
        Bitmap signature3 = signaturePadC1.getTransparentSignatureBitmap();
        Bitmap signature4 = signaturePadC2.getTransparentSignatureBitmap();

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref4 = FirebaseDatabase.getInstance().getReference();
        StorageReference r1 = FirebaseStorage.getInstance().getReference(ref1.push().getKey() + ".png");
        StorageReference r2 = FirebaseStorage.getInstance().getReference(ref2.push().getKey() + ".png");
        StorageReference r3 = FirebaseStorage.getInstance().getReference(ref3.push().getKey() + ".png");
        StorageReference r4 = FirebaseStorage.getInstance().getReference(ref4.push().getKey() + ".png");

        ByteArrayOutputStream bao1 = new ByteArrayOutputStream();
        signature1.compress(Bitmap.CompressFormat.PNG, 100, bao1); // bmp is bitmap from user image file
        byte[] image1 = bao1.toByteArray();
        String imgageBase64 = Base64.encodeToString(image1, Base64.DEFAULT);
        String theImage1 = "data:image/png;base64," + imgageBase64;
        htmlManipulation = htmlManipulation.replace(htmlAsString.indexOf("tc1sign"), htmlAsString.indexOf("tc1sign") + "tc1sign".length(), theImage1);

        ByteArrayOutputStream bao2 = new ByteArrayOutputStream();
        signature2.compress(Bitmap.CompressFormat.PNG, 100, bao2); // bmp is bitmap from user image file
        byte[] image2 = bao2.toByteArray();
        ByteArrayOutputStream bao3 = new ByteArrayOutputStream();
        signature3.compress(Bitmap.CompressFormat.PNG, 100, bao3); // bmp is bitmap from user image file
        byte[] image3 = bao3.toByteArray();
        ByteArrayOutputStream bao4 = new ByteArrayOutputStream();
        signature4.compress(Bitmap.CompressFormat.PNG, 100, bao4); // bmp is bitmap from user image file
        byte[] image4 = bao4.toByteArray();

        r1.putBytes(image1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(TeamsInfo.this, "Uploaded", Toast.LENGTH_LONG).show();
            }
        });
        r2.putBytes(image2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(TeamsInfo.this, "Uploaded", Toast.LENGTH_LONG).show();
            }
        });
        r3.putBytes(image3).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(TeamsInfo.this, "Uploaded", Toast.LENGTH_LONG).show();
            }
        });
        r4.putBytes(image4).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(TeamsInfo.this, "Uploaded", Toast.LENGTH_LONG).show();
            }
        });



        htmlAsString = htmlManipulation.toString();
        return htmlAsString;
    }
}