package techfie.razon.tasktodo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ContactActivity extends AppCompatActivity {


    ImageView fb, twitter, google;
    LinearLayout web, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        fb  = (ImageView) findViewById(R.id.fb);
        twitter = (ImageView) findViewById(R.id.twitter);
        google = (ImageView) findViewById(R.id.google);
        web = (LinearLayout) findViewById(R.id.web);
        phone = (LinearLayout) findViewById(R.id.phone);

        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://techfie.com/"));
                startActivity(browserIntent);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:01680048364"));
                startActivity(intent);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Techfie-313156745708924/"));
                startActivity(browserIntent);
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:techfie007@gmail.com"));
                emailIntent.setType("text/plain");
               // emailIntent.putExtra(Intent.EXTRA_EMAIL, "techfie007@gmail.com");
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }
        });
        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/techfie007"));
                startActivity(browserIntent);
            }
        });

    }
}
