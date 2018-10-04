package damiano.airports.GDN;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import damiano.airports.R;

public class GdanskGDN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdansk_gdn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void clickGDN(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.GDNArrivalsImage:
                intent = new Intent(this, GdanskGDNArrivals.class);
                startActivity(intent);
                break;
            case R.id.GDNArrivalsTextView:
                intent = new Intent(this, GdanskGDNArrivals.class);
                startActivity(intent);
                break;
            case R.id.GDNDeparturesImage:
                intent = new Intent(this, GdanskGDNDepartures.class);
                startActivity(intent);
                break;
            case R.id.GDNDeparturesTextView:
                intent = new Intent(this, GdanskGDNDepartures.class);
                startActivity(intent);
                break;
        }
    }
}
