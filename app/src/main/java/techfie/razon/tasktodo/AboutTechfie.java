package techfie.razon.tasktodo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class AboutTechfie extends AppCompatActivity {

    ImageView contact, home;
    MyTextView anotherCOntact, anotherHome;

    MyTextView load_innova,load_techfie, get_aj;

    String innovaURL = "http://innovaepc.com/";
    String techfieURL = "http://techfie.com/";
    String a2jURL = "https://play.google.com/store/apps/details?id=razon.admissionju";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_techfie);

        contact = (ImageView) findViewById(R.id.contact);
        home = (ImageView) findViewById(R.id.home);
        anotherCOntact = (MyTextView) findViewById(R.id.anotherCOntact);
        anotherHome = (MyTextView) findViewById(R.id.anotherHome);

        load_innova = (MyTextView) findViewById(R.id.load_innova);
        load_techfie = (MyTextView) findViewById(R.id.load_techfie);
        get_aj = (MyTextView) findViewById(R.id.get_aj);

        load_innova.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(innovaURL));
                startActivity(browserIntent);
            }
        });
        load_techfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(techfieURL));
                startActivity(browserIntent);
            }
        });
        get_aj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(a2jURL));
                startActivity(browserIntent);
            }
        });


        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutTechfie.this, ContactActivity.class));
            }
        });
        anotherCOntact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutTechfie.this, ContactActivity.class));
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutTechfie.this, TaskActivity.class));
            }
        });
        anotherHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutTechfie.this, TaskActivity.class));
            }
        });

    }

}
