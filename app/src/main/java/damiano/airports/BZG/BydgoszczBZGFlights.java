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

public class BydgoszczBZGFlights extends AppCompatActivity {

    private ArrayList<Flight> flightList = new ArrayList<>();
    private String name = "BZG Bydgoszcz Flights";
    private String urlAirport;
    private String htmlCode = "";
    private TextView BZGFlightsTitleTextView, BZGFlightsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bydgoszcz_bzgflights);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BZGFlightsTitleTextView = findViewById(R.id.BZGFlightsTitleTextView);
        BZGFlightsTextView = findViewById(R.id.BZGFlightsTextView);
        BZGFlightsTitleTextView.setText(name);
        BZGFlightsTextView.setText("Wait...");
        BydgoszczBZG bzg = new BydgoszczBZG();
        urlAirport = bzg.getUrlAirport();
        htmlCode = bzg.getHtmlCode();
        if (htmlCode == null) {
            new DownloadFlights().execute();
        } else {
            flights();
            String flights = "";
            flights = printText(flights);
            BZGFlightsTextView.setText(flights);
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
            flights();
            String flights = "";
            flights = printText(flights);
            BZGFlightsTextView.setText(flights);
            super.onPostExecute(aVoid);
        }
    }

    private void flights(){

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
            } else if (temp.contains("wylądował") || temp.contains("spodziewany") ||
                    temp.contains("wystartował") || temp.contains("opóźniony")){
                String[] s = temp.split("[\t]+");
                status = s[1];
                expectedTime = s[2];
            }else if (temp.contains("rozkładowo")) {
                status = "rozkładowo";
                expectedTime = time;
            }
            if (time != null && direction != null && flightNumber != null && status !=null && expectedTime != null){
                flightList.add(new Flight(time, direction, flightNumber, status, expectedTime));
                time = null; direction = null; flightNumber = null; status = null; expectedTime = null;
            }
        }
    }

    private String printText(String flights) {
        for (int i = 0; i < flightList.size(); i++) {
            if (flightList.get(i).getStatus().equals("wylądował") ||
                    flightList.get(i).getStatus().equals("spodziewany")){
                flights += flightList.get(i).getDirection() + " -> Bydgoszcz" + "\n" +
                        flightList.get(i).getTime() + "\t" +
                        flightList.get(i).getFlightNumber() + "\t" +
                        flightList.get(i).getStatus() + "\t" +
                        flightList.get(i).getExpectedTime() + "\n\n";
            } else {
                flights += "Bydgoszcz -> " + flightList.get(i).getDirection() + "\n" +
                        flightList.get(i).getTime() + "\t" +
                        flightList.get(i).getFlightNumber() + "\t" +
                        flightList.get(i).getStatus() + "\t" +
                        flightList.get(i).getExpectedTime() + "\n\n";
            }
        }
        return flights;
    }
}
