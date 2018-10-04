package damiano.airports.GDN;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import damiano.airports.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterGDNArrivals extends RecyclerView.Adapter<RecyclerViewAdapterGDNArrivals.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapterGDNArrivals";

    private ArrayList<String> imageNames;
    private ArrayList<String> images;
    private Context context;

    public RecyclerViewAdapterGDNArrivals(Context context, ArrayList<String> imageNames, ArrayList<String> images) {
        this.imageNames = imageNames;
        this.images = images;
        this.context = context;
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

        viewHolder.imageName.setText(imageNames.get(i));

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + imageNames.get(i));

                Toast.makeText(context, imageNames.get(i), Toast.LENGTH_LONG).show();

//                Intent intent = new Intent(context, GdanskGDNArrival.class);
//                intent.putExtra("image_url", images.get(i));
//                intent.putExtra("image_name", imageNames.get(i));
//                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
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
