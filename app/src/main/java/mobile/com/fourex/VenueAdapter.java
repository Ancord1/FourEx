package mobile.com.fourex;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.ItemHolder> {
    private List<Venue> venues;

    public VenueAdapter(List<Venue> venues){
        this.venues = venues;
    }

    @Override
    public VenueAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.venue_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.name.setText(venues.get(position).getName());
        holder.address.setText(venues.get(position).getAddress());
        holder.distance.setText((venues.get(position).getDistance()));
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder{
        TextView name, address, distance;

        public ItemHolder(View v){
            super(v);
            name = (TextView) v.findViewById(R.id.tvName);
            address = (TextView) v.findViewById(R.id.tvAddress);
            distance = (TextView) v.findViewById(R.id.tvDistance);
        }
    }
}
