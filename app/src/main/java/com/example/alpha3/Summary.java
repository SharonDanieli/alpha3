package com.example.alpha3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.io.FileOutputStream;
import java.io.IOException;

public class Summary extends AppCompatActivity {

    WebView theSheet;
    //ExtendedFloatingActionButton send;
    Button send;
    String htmlAsString;

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



    public void send(View view) {
        Intent t = new Intent(Intent.ACTION_SEND);
        t.setType("message/rfc822");
        t.putExtra(Intent.EXTRA_EMAIL  , new String[]{"al5493@bs.amalnet.k12.il"});
        t.putExtra(Intent.EXTRA_SUBJECT, "game result");
        t.putExtra(Intent.EXTRA_TEXT   , htmlAsString);
/*        String pathToMyAttachedFile = "temp/sample-file.pdf";
        File file = new File(root, pathToMyAttachedFile);
        if (!file.exists() || !file.canRead()) {
            return;
        }
        Uri uri = Uri.fromFile(file);
        t.putExtra(Intent.EXTRA_STREAM, uri);*/
        try {
            startActivity(Intent.createChooser(t, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Summary.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
   }
}