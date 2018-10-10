package damiano.airports;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

public class FlightDetalis extends AppCompatActivity {

    private static final String TAG = "FlightDetails";

    private String urlAirport = "https://origin.flightaware.com/live/flight/";
    private String flightNumber;
    private String htmlCode;
    private Scanner scanner;
    private TextView titleTV, airlineTV, departureTimesTV, arrivalTimesTV, aircraftInformationTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_details);
        Log.d(TAG, "onCreate: started");
        titleTV = findViewById(R.id.titleTextView);
        airlineTV = findViewById(R.id.airlineTextView);
        departureTimesTV = findViewById(R.id.departureTimesTextView);
        arrivalTimesTV = findViewById(R.id.arrivalTimesTextView);
        aircraftInformationTV = findViewById(R.id.aircraftInformationTextView);
        getIncomingIntent();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");
        if (getIntent().hasExtra("flightNumber")){
            Log.d(TAG, "getIncomingIntent: found intent extras");

            flightNumber = getIntent().getStringExtra("flightNumber");
            new DownloadFlights().execute();
        }
    }

    private class DownloadFlights extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Log.d(TAG, "DownloadFlights: try download html code");
                URL url = new URL(urlAirport+flightNumber);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String stringBuffer;
                String stringText = "";
                while ((stringBuffer = bufferedReader.readLine()) != null) {
                    stringText += (stringBuffer + "\n");
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                htmlCode = stringText;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getflightDetails();
            super.onPostExecute(aVoid);
        }
    }

    private void getflightDetails() {
        Log.d(TAG, "getflightDetails: get flight details");
        String title = null;
        String airline = null;
        String departureTimes = null;
        String arrivalTimes = null;
        String aircraftInformation = null;

        title = getTitle(title);
        airline = getAirline(airline);
        departureTimes = getDepartureTimes(departureTimes);
        arrivalTimes = getArrivalTimes(arrivalTimes);
        aircraftInformation = getAircraftInformation(aircraftInformation);

        setFlightDetails(title, airline, departureTimes, arrivalTimes, aircraftInformation);
    }

    private String getTitle(String title) {
        Log.d(TAG, "getTitle: get title");

        scanner = new Scanner(htmlCode);
        int index1, index2;
        String temp;
        while (scanner.hasNextLine()) {
            temp = scanner.nextLine();
            System.out.println(temp);
            if (title == null && temp.contains("<title>")) {
                index1 = temp.indexOf("<title>");
                index2 = temp.indexOf("</title>");
                title = temp.substring((index1 + "<title>".length()), index2).trim();
                break;
            }
        }
        return title;
    }

    private String getAirline(String airline) {
        Log.d(TAG, "getAirline: get airline");

        scanner = new Scanner(htmlCode);
        int index1;
        int index2;
        String temp;
        while (scanner.hasNextLine()) {
            temp = scanner.nextLine();
            System.out.println(temp);
            if (airline == null && (index1 = temp.indexOf("\"airline\":{\"fullName\":\"")) != -1) {
                index2 = temp.indexOf("\",\"shortName\":", index1);
                airline = temp.substring((index1 + "\"airline\":{\"fullName\":\"".length()), index2).trim();
                break;
            }
        }
        return airline;
    }

    private String getDepartureTimes(String departureTimes) {
        Log.d(TAG, "getDepartureTimes: get departure times");

        return departureTimes;
    }

    private String getArrivalTimes(String arrivalTimes) {
        Log.d(TAG, "getArrivalTimes: get arrival times");

        return arrivalTimes;
    }

    private String getAircraftInformation(String aircraftInformation) {
        Log.d(TAG, "getAircraftInformation: get aircraft information");

        return aircraftInformation;
    }

    private void setFlightDetails(String title, String airline, String departureTimes, String arrivalTimes, String aircraftInformation){
        Log.d(TAG, "setFlightDetails: set flight details");

        titleTV.setText(title);
        airlineTV.setText(airline);
    }
}
