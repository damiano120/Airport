package damiano.airports.GDN;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import damiano.airports.Flight;
import damiano.airports.R;

public class GdanskGDNDepartures extends AppCompatActivity {

    private ArrayList<Flight> departuresList = new ArrayList<>();
    private String name = "GDN Gdansk Departures";
    private String urlAirport = "http://www.airport.gdansk.pl/schedule/departures-table/";
    private Document htmlCode;
    private TextView GDNDeparturesTitleTextView, GDNDeparturesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdansk_gdndepartures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GDNDeparturesTitleTextView = findViewById(R.id.GDNDeparturesTitleTextView);
        GDNDeparturesTextView = findViewById(R.id.GDNDeparturesTextView);
        GDNDeparturesTitleTextView.setText(name);
        GDNDeparturesTextView.setText("Wait...");
        new DownloadFlights().execute();
    }

    public class DownloadFlights extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                htmlCode = Jsoup.connect(urlAirport).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            departures();
            String departures = "";
            departures = printText(departures);
            GDNDeparturesTextView.setText(departures);
            super.onPostExecute(aVoid);
        }
    }

    private void departures() {

        String time = null;
        String direction = null;
        String flightNumber = null;
        String status = null;
        String expectedTime = null;

//        DODAJ LOTY
        Elements times = htmlCode.select("td.time");
        Elements directions = htmlCode.select("td.airport");
        Elements flightNumbers = htmlCode.select("td.flight");
        Elements statuses = htmlCode.select("td.status");
        Elements expectedTimes = htmlCode.select("td.time");
        int j = 0;
        for (int i=0; i<directions.size(); i++){
            time = times.get(j).text();
            direction = directions.get(i).text();
            flightNumber = flightNumbers.get(i).text();
            status = statuses.get(i).text();
            j++;
            expectedTime = expectedTimes.get(j).text();
            j++;

            if (expectedTime.equals("") && status.equals("")) {
                departuresList.add(new Flight(time, direction, flightNumber, "bd", "bd"));
            } else if (expectedTime.equals("")) {
                departuresList.add(new Flight(time, direction, flightNumber, status, "bd"));
            } else if (status.equals("")) {
                departuresList.add(new Flight(time, direction, flightNumber, "bd", expectedTime));
            } else {
                departuresList.add(new Flight(time, direction, flightNumber, status, expectedTime));
            }
        }
    }

    private String printText(String departures) {
        for (int i = 0; i < departuresList.size(); i++) {
            departures += "Gdańsk -> " + departuresList.get(i).getDirection() + " \t" +
                    departuresList.get(i).getFlightNumber() + " \n" +
                    "Czas " + departuresList.get(i).getTime() + " \t" +
                    "Status " + departuresList.get(i).getStatus() + " \t" +
                    "Czas ocz. " + departuresList.get(i).getExpectedTime() + " \n\n";
        }
        return departures;
    }
}