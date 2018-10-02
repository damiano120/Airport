package damiano.airports.BZG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import damiano.airports.DownloadHTML;
import damiano.airports.Flight;
import damiano.airports.R;

public class BydgoszczBZGDepartures extends AppCompatActivity {

    private ArrayList<Flight> departuresList = new ArrayList<>();
    private String name = "BZG Bydgoszcz Departures";
    private String urlAirport;
    private String htmlCode = "";
    private TextView BZGDeparturesTitleTextView, BZGDeparturesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bydgoszcz_bzgdepartures);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BZGDeparturesTitleTextView = findViewById(R.id.BZGDeparturesTitleTextView);
        BZGDeparturesTextView = findViewById(R.id.BZGDeparturesTextView);
        BZGDeparturesTitleTextView.setText(name);
        BZGDeparturesTextView.setText("Wait...");
        BydgoszczBZG bzg = new BydgoszczBZG();
        urlAirport = bzg.getUrlAirport();
        htmlCode = bzg.getHtmlCode();
        if (htmlCode == null) {
            new DownloadFlights().execute();
        } else {
            departures();
            String departures = "";
            departures = printText(departures);
            BZGDeparturesTextView.setText(departures);
        }
    }

    public class DownloadFlights extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            DownloadHTML downloadHTML = new DownloadHTML(urlAirport);
            htmlCode = downloadHTML.getHtmlCode();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            departures();
            String departures = "";
            departures = printText(departures);
            BZGDeparturesTextView.setText(departures);
            super.onPostExecute(aVoid);
        }
    }

    private void departures(){

        Scanner scanner = new Scanner(htmlCode);
        String time = null;
        String direction = null;
        String flightNumber = null;
        String status = null;
        String expectedTime = null;

//        DODAJ LOTY
        int index1, index2;
        String temp;
        while (scanner.hasNextLine()){
            temp = scanner.nextLine();
            if (temp.contains("time") && temp.contains("</div>")){
                index1 = temp.indexOf("\">");
                index2 = temp.indexOf("</div>");
                time = temp.substring((index1+2),index2).trim();
            } else if (temp.contains("direction") && temp.contains("</div>")) {
                index1 = temp.indexOf("\">");
                index2 = temp.indexOf("</div>");
                direction = temp.substring((index1+2),index2).trim();
            } else if (temp.contains("flightNumber") && temp.contains("</div>")){
                index1 = temp.indexOf("\">");
                index2 = temp.indexOf("</div>");
                flightNumber = temp.substring((index1+2),index2).trim();
            } else if (temp.contains("wystartował") || temp.contains("opóźniony")){
                String[] s = temp.split("[\t]+");
                status = s[1];
                expectedTime = s[2];
            }else if (temp.contains("rozkładowo")) {
                status = "rozkładowo";
                expectedTime = time;
            }
            if (time != null && direction != null && flightNumber != null && status !=null && expectedTime != null){
                departuresList.add(new Flight(time, direction, flightNumber, status, expectedTime));
                time = null; direction = null; flightNumber = null; status = null; expectedTime = null;
            }
        }
    }

    private String printText(String departures) {
        for (int i = 0; i < departuresList.size(); i++) {
            if (departuresList.get(i).getStatus().equals("wystartował") ||
                    departuresList.get(i).getStatus().equals("opóźniony") ||
                    departuresList.get(i).getStatus().equals("rozkładowo")){
                departures += "Bydgoszcz -> " + departuresList.get(i).getDirection() + "\n" +
                        departuresList.get(i).getTime() + "\t" +
                        departuresList.get(i).getFlightNumber() + "\t" +
                        departuresList.get(i).getStatus() + "\t" +
                        departuresList.get(i).getExpectedTime() + "\n\n";
            }
        }
        return departures;
    }
}