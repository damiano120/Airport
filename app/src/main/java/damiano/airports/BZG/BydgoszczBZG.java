package damiano.airports.BZG;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import damiano.airports.DownloadHTML;
import damiano.airports.R;

public class BydgoszczBZG extends AppCompatActivity {

    private String airportName = "BZG Bydgoszcz Airport";
    private String urlAirport = "https://plb.pl";
    private String htmlCode;
    private TextView BZGTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bydgoszcz_bzg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BZGTitleTextView = findViewById(R.id.BZGTitleTextView);
        BZGTitleTextView.setText(airportName);
        new Flights().execute();

    }

    public void click(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.BZGButtonFlights:
                intent = new Intent(this, BydgoszczBZGFlights.class);
                startActivity(intent);
                break;
            case R.id.BZGButtonArrivals:
                intent = new Intent(this, BydgoszczBZGArrivals.class);
                startActivity(intent);
                break;
            case R.id.BZGButtonDepartures:
                intent = new Intent(this, BydgoszczBZGDepartures.class);
                startActivity(intent);
                break;
        }
    }

        public class Flights extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DownloadHTML downloadHTML = new DownloadHTML(urlAirport);
                htmlCode = downloadHTML.getHtmlCode();
                return null;
            }
        }

    public String getHtmlCode() {
        return htmlCode;
    }

    public String getUrlAirport() {
        return urlAirport;
    }
}
