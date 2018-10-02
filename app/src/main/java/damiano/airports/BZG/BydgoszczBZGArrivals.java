package damiano.airports.BZG;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

import damiano.airports.DownloadHTML;
import damiano.airports.Flight;
import damiano.airports.R;

public class BydgoszczBZGArrivals extends AppCompatActivity {

    private ArrayList<Flight> arrivalsList = new ArrayList<>();
    private String name = "BZG Bydgoszcz Arrivals";
    private String urlAirport;
    private String htmlCode;
    private TextView BZGArrivalsTitleTextView, BZGArrivalsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bydgoszcz_bzgarrivals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        BZGArrivalsTitleTextView = findViewById(R.id.BZGArrivalsTitleTextView);
        BZGArrivalsTextView = findViewById(R.id.BZGArrivalsTextView);
        BZGArrivalsTitleTextView.setText(name);
        BZGArrivalsTextView.setText("Wait...");
        BydgoszczBZG bzg = new BydgoszczBZG();
        urlAirport = bzg.getUrlAirport();
        htmlCode = bzg.getHtmlCode();
        if (htmlCode == null) {
            new DownloadFlights().execute();
        } else {
            arrivals();
            String arrivals = "";
            arrivals = printText(arrivals);
            BZGArrivalsTextView.setText(arrivals);
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
            arrivals();
            String arrivals = "";
            arrivals = printText(arrivals);
            BZGArrivalsTextView.setText(arrivals);
            super.onPostExecute(aVoid);
        }
    }

    private void arrivals() {

        Scanner scanner = new Scanner(htmlCode);
        String time = null;
        String direction = null;
        String flightNumber = null;
        String status = null;
        String expectedTime = null;

//        DODAJ LOTY
        int index1, index2;
        String temp;
        while (scanner.hasNextLine()) {
            temp = scanner.nextLine();
            if (temp.contains("time") && temp.contains("</div>")) {
                index1 = temp.indexOf("\">");
                index2 = temp.indexOf("</div>");
                time = temp.substring((index1 + 2), index2).trim();
            } else if (temp.contains("direction") && temp.contains("</div>")) {
                index1 = temp.indexOf("\">");
                index2 = temp.indexOf("</div>");
                direction = temp.substring((index1 + 2), index2).trim();
            } else if (temp.contains("flightNumber") && temp.contains("</div>")) {
                index1 = temp.indexOf("\">");
                index2 = temp.indexOf("</div>");
                flightNumber = temp.substring((index1 + 2), index2).trim();
            } else if (temp.contains("wylądował") || temp.contains("spodziewany")) {
                String[] s = temp.split("[\t]+");
                status = s[1];
                expectedTime = s[2];
            }
            if (time != null && direction != null && flightNumber != null && status != null && expectedTime != null) {
                arrivalsList.add(new Flight(time, direction, flightNumber, status, expectedTime));
                time = null;
                direction = null;
                flightNumber = null;
                status = null;
                expectedTime = null;
            }
        }
    }

    private String printText(String arrivals) {
        for (int i = 0; i < arrivalsList.size(); i++) {
            if (arrivalsList.get(i).getStatus().equals("wylądował") ||
                    arrivalsList.get(i).getStatus().equals("spodziewany")) {
                arrivals += arrivalsList.get(i).getDirection() + " -> Bydgoszcz" + "\n" +
                        arrivalsList.get(i).getTime() + "\t" +
                        arrivalsList.get(i).getFlightNumber() + "\t" +
                        arrivalsList.get(i).getStatus() + "\t" +
                        arrivalsList.get(i).getExpectedTime() + "\n\n";
            }
        }
        return arrivals;
    }
}