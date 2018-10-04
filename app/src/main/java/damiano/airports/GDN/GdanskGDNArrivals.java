package damiano.airports.GDN;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import damiano.airports.Flight;
import damiano.airports.R;

public class GdanskGDNArrivals extends AppCompatActivity {

    private ArrayList<Flight> arrivalsList = new ArrayList<>();
    private String name = "GDN Gdansk Arrivals";
    private String urlAirport = "http://www.airport.gdansk.pl/schedule/arrivals-table";
    private Document htmlCode;

    private static final String TAG = "MainActivity";

    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imageUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdansk_gdnarrivals);
        Log.d(TAG, "onCreate: started.");
        initImageBitmaps();
    }

    private void initImageBitmaps() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.");
        new DownloadFlights().execute();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.gdn_arrivals_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapterGDNArrivals adapter = new RecyclerViewAdapterGDNArrivals(this, names, imageUrls);
        recyclerView.setAdapter(adapter);

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
            arrivals();
            super.onPostExecute(aVoid);
        }
    }

    private void arrivals() {

        String time = null;
        String direction = null;
        String flightNumber = null;
        String status = null;
        String expectedTime = null;
        String image = null;

//        DODAJ LOTY
        Elements times = htmlCode.select("td.time");
        Elements directions = htmlCode.select("td.airport");
        Elements flightNumbers = htmlCode.select("td.flight");
        Elements statuses = htmlCode.select("td.status");
        Elements expectedTimes = htmlCode.select("td.time");
        Elements images = htmlCode.select("td.logo");

        int j = 0;
        for (int i = 0; i < directions.size(); i++) {
            time = times.get(j).text();
            direction = directions.get(i).text();
            flightNumber = flightNumbers.get(i).text();
            status = statuses.get(i).text();
            j++;
            expectedTime = expectedTimes.get(j).text();
            j++;

            if (expectedTime.equals("") && status.equals("")) {
                arrivalsList.add(new Flight(time, direction, flightNumber, "bd", "bd"));
            } else if (expectedTime.equals("")) {
                arrivalsList.add(new Flight(time, direction, flightNumber, status, "bd"));
            } else if (status.equals("")) {
                arrivalsList.add(new Flight(time, direction, flightNumber, "bd", expectedTime));
            } else {
                arrivalsList.add(new Flight(time, direction, flightNumber, status, expectedTime));
            }

            image = images.get(i).toString();
            if (image.contains("<img src=\"")) {
                int index1 = image.indexOf("<img src=\"");
                int index2 = image.indexOf("\" alt=");
                image = "http://www.airport.gdansk.pl" + image.substring((index1 + "<img src=\"".length()), index2).trim();
//                image = "http://www.airport.gdansk.pl" + images.get(i).selectFirst("img[src$=.]");
            } else if (!image.contains("<img src=\"")) {
                image = "http://www.airport.gdansk.pl/img/frgt/_c33d9f874d9f0b577f6812c3.jpg";
            }
            imageUrls.add(image);
        }
        printText();
    }

    private ArrayList printText() {
        String arrival = "";
        for (int i = 0; i < arrivalsList.size(); i++) {
            arrival = arrivalsList.get(i).getDirection() + " -> Gdansk" + " \t" +
                    arrivalsList.get(i).getFlightNumber() + " \n" +
                    "Czas " + arrivalsList.get(i).getTime() + " \t" +
                    "Czas ocz. " + arrivalsList.get(i).getExpectedTime() + " \n" +
                    "Status " + arrivalsList.get(i).getStatus();
            names.add(arrival);
        }
        initRecyclerView();
        return names;
    }
}
