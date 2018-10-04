package damiano.airports;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import damiano.airports.BZG.BydgoszczBZG;
import damiano.airports.GDN.GdanskGDN;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airports);
        mRecyclerView = (RecyclerView) findViewById(R.id.airports_recyler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Airport> airports = new ArrayList<>();
            airports.add(new Airport("BZG - Bydgoszcz", R.drawable.bzg_image));
            airports.add(new Airport("GDN - Gdańsk", R.drawable.gdn_image));
            airports.add(new Airport("POZ - Poznań", R.drawable.poz_image));
            airports.add(new Airport("LUZ - Lublin", R.drawable.luz_image));

        mAdapter = new MyAdapter(this, airports, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }
}
