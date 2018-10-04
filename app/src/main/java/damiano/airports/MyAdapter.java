package damiano.airports;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import damiano.airports.BZG.BydgoszczBZG;
import damiano.airports.GDN.GdanskGDN;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<Airport> airports;
    private RecyclerView recyclerView;
    private Context context;

    public MyAdapter(Context context, ArrayList<Airport> airports, RecyclerView recyclerView) {
        this.airports = airports;
        this.recyclerView = recyclerView;
        this.context = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView airportImage;
        public TextView airportName;

        public MyViewHolder(View pItem) {
            super(pItem);
            airportImage = pItem.findViewById(R.id.airportImage);
            airportName = pItem.findViewById(R.id.airportName);
        }
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.content_airports, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int choicePosition = recyclerView.getChildAdapterPosition(v);
                Intent intent;
                switch (choicePosition) {
                    case 0:
                        intent = new Intent(context, BydgoszczBZG.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(context, GdanskGDN.class);
                        context.startActivity(intent);
                        break;
                }
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.airportName.setText(airports.get(i).getName());
        Glide.with(context)
                .load(airports.get(i).getUrlImage())
                .into(myViewHolder.airportImage);
    }

    @Override
    public int getItemCount() {
        return airports.size();
    }
}
