package damiano.airports;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadHTML {

    private String urlAirport;
    private String htmlCode;

    public DownloadHTML(String urlAirport) {
        this.urlAirport = urlAirport;
    }

    public String getHtmlCode() {
        try {
            URL url = new URL(urlAirport);
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
        return htmlCode;
    }
}
