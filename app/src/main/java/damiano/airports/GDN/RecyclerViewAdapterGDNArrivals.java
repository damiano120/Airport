package damiano.airports.GDN;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import damiano.airports.Flight;
import damiano.airports.FlightDetalis;
import damiano.airports.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterGDNArrivals extends RecyclerView.Adapter<RecyclerViewAdapterGDNArrivals.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterGDNArrivals";

    private ArrayList<String> descriptions;
    private ArrayList<String> images;
    private Context context;
    private ArrayList<Flight> arrivalsList;

    public RecyclerViewAdapterGDNArrivals(Context context, ArrayList<String> imageNames, ArrayList<String> images, ArrayList<Flight> arrivalsList) {
        this.descriptions = imageNames;
        this.images = images;
        this.context = context;
        this.arrivalsList = arrivalsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_gdansk_gdnarrivals, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(context)
                .asDrawable()
                .load(images.get(i))
                .into(viewHolder.image);

        viewHolder.imageName.setText(descriptions.get(i));

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + descriptions.get(i));

                Toast.makeText(context, descriptions.get(i), Toast.LENGTH_LONG).show();

                GdanskGDNArrivals gdnArrivals = new GdanskGDNArrivals();
                Intent intent = new Intent(context, FlightDetalis.class);
//                intent.putExtra("image_url", images.get(i));
//                intent.putExtra("image_name", descriptions.get(i));
                intent.putExtra("flightNumber", arrivalsList.get(i).getFlightNumber());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return descriptions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.gdn_arrivals_image);
            imageName = itemView.findViewById(R.id.gdn_arrivals_image_name);
            parentLayout = itemView.findViewById(R.id.gdn_arrivals_parent_layout);
        }
    }
}
