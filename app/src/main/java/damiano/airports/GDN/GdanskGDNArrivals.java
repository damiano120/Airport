package damiano.airports.GDN;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Scanner;

import damiano.airports.DownloadHTML;
import damiano.airports.Flight;
import damiano.airports.R;

public class GdanskGDNArrivals extends AppCompatActivity {

    private ArrayList<Flight> arrivalsList = new ArrayList<>();
    private String name = "GDN Gdansk Arrivals";
    private String urlAirport = "http://www.airport.gdansk.pl/schedule/arrivals-table";
    private String htmlCode = "";
    private TextView GDNArrivalsTitleTextView, GDNArrivalsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdansk_gdnarrivals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        GDNArrivalsTitleTextView = findViewById(R.id.GDNArrivalsTitleTextView);
        GDNArrivalsTextView = findViewById(R.id.GDNArrivalsTextView);
        GDNArrivalsTitleTextView.setText(name);
        GDNArrivalsTextView.setText("Wait...");
        new DownloadFlights().execute();
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
            GDNArrivalsTextView.setText(arrivals);
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
            if (temp.contains("<td class=\"time\">")) {
                index1 = temp.indexOf("<td class=\"time\">");
                index2 = temp.indexOf("</td><td class=\"time\">", index1);
                time = temp.substring((index1 + "<td class=\"time\">".length()), index2).trim();
                index1 = temp.indexOf("<td class=\"time\">", index2);
                index2 = temp.indexOf("</td><td class=\"status \">", index1);
                expectedTime = temp.substring((index1 + "<td class=\"time\">".length()), index2).trim();
            }
            if (temp.contains("<td class=\"airport\">")) {
                index1 = temp.indexOf("<td class=\"airport\">");
                index2 = temp.indexOf("</td><td class=\"flight\">", index1);
                direction = temp.substring((index1 + "<td class=\"airport\">".length()), index2).trim();
            }
            if (temp.contains("<td class=\"flight\">")) {
                index1 = temp.indexOf("<td class=\"flight\">");
                index2 = temp.indexOf("</td><td class=\"logo\">", index1);
                flightNumber = temp.substring((index1 + "<td class=\"flight\">".length()), index2).trim();
            }
            if (temp.contains("<td class=\"status \">")) {
                index1 = temp.indexOf("<td class=\"status \">");
                index2 = temp.indexOf("<i class=\"icon", index1);
                status = temp.substring((index1 + "<td class=\"status \">".length()), index2).trim();
                String[] s = status.split(" +");
                System.out.println(status);
                System.out.println(s.length);
                if (s.length == 2) {
                    status = s[0];
                }
            }
            if (time != null && direction != null && flightNumber != null && status != null && expectedTime != null) {
                if (expectedTime.equals("&nbsp;") && status.equals("&nbsp;")) {
                    arrivalsList.add(new Flight(time, direction, flightNumber, "bd", "bd"));
                } else if (expectedTime.equals("&nbsp;")) {
                    arrivalsList.add(new Flight(time, direction, flightNumber, status, "bd"));
                } else if (status.equals("&nbsp;")) {
                    arrivalsList.add(new Flight(time, direction, flightNumber, "bd", expectedTime));
                } else {
                    arrivalsList.add(new Flight(time, direction, flightNumber, status, expectedTime));
                }
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
            arrivals += arrivalsList.get(i).getDirection() + " -> Gdansk" + "\n" +
                    arrivalsList.get(i).getTime() + "\t" +
                    arrivalsList.get(i).getFlightNumber() + "\t" +
                    arrivalsList.get(i).getStatus() + "\t" +
                    arrivalsList.get(i).getExpectedTime() + "\n\n";
        }
        return arrivals;
    }
}
