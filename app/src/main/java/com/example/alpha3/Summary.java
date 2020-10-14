package com.example.alpha3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.itextpdf.text.DocumentException;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XmlSerializer;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Summary extends AppCompatActivity {

    WebView theSheet;
    //ExtendedFloatingActionButton send;
    Button send;
    String htmlAsString;

    /**
     * Links the components and displays the HTML file.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        send = findViewById(R.id.button2);

        Intent r = getIntent();

        htmlAsString = r.getStringExtra("sheet");

        theSheet = findViewById(R.id.theSheet);
        theSheet.loadDataWithBaseURL(null, htmlAsString, "text/html", "charset=utf-8", null);



        // Clean the html to use in the flying saucer converting tool
// get the element you want to serialize
       /* HtmlCleaner cleaner = new HtmlCleaner();
        TagNode rootTagNode = cleaner.clean(htmlAsString);
// set up properties for the serializer (optional, see online docs)
        CleanerProperties cleanerProperties = cleaner.getProperties();
// use the getAsString method on an XmlSerializer class
        XmlSerializer xmlSerializer = new PrettyXmlSerializer(cleanerProperties);
        String cleanedHtml = xmlSerializer.getAsString(rootTagNode);

        final byte[] bytes;
        try {
            bytes = generatePDFFrom(cleanedHtml);
            FileOutputStream fos = new FileOutputStream("sample-file.pdf");
            fos.write(bytes);
            send.show();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        */

    }
    /*private static byte[] generatePDFFrom(String html) throws IOException, DocumentException {
        final ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        try (ByteArrayOutputStream fos = new ByteArrayOutputStream(html.length())) {
            renderer.createPDF(fos);
            return fos.toByteArray();
        }
    }

     */

    /**
     * Creates a message to it attaches the HTML file, allowing it to be emailed.
     */
    public void send(View view) {
        // Get permissions for reading and writing
        int request_code = 1;
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, request_code);
        }

        // Generate hash for unique filename
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(this, "Error creating filename. Results not sent successfully.", Toast.LENGTH_LONG);
            return;
        }
        digest.update(String.valueOf(new Random().nextInt(1000000)).getBytes());
        byte messageDigest[] = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2)
                h = "0" + h;
            hexString.append(h);
        }

        // Create the file results.html
        String filename="results-" + hexString.toString() + ".html";
        File results_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        if (results_file.exists()) {
            results_file.delete();
        }
        try {
            results_file.createNewFile();
        } catch (IOException e) {
            Toast.makeText(Summary.this, "Error creating file for sending. Results not sent successfully.", Toast.LENGTH_LONG);
            return;
        }
        // Write the results to the new file created
        try {
            FileOutputStream stream = new FileOutputStream(results_file);
            try {
                stream.write(htmlAsString.getBytes());
            } catch (IOException e) {
                Toast.makeText(Summary.this, "Error writing results for file. Results not sent successfully.", Toast.LENGTH_LONG);
                results_file.delete();
                return;
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(Summary.this, "Error writing results for file. Results not sent successfully.", Toast.LENGTH_LONG);
            results_file.delete();
            return;
        }


        Uri path = Uri.fromFile(results_file);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"al5493@bs.amalnet.k12.il"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
        // the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Game Results");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
        try {
            startActivity(Intent.createChooser(emailIntent , "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Summary.this, "There are no email clients installed. Results not sent successfully.", Toast.LENGTH_SHORT).show();
        }
        results_file.delete();
   }
}