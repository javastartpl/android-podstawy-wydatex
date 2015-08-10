package pl.javastart.wydatex.location;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.javastart.wydatex.R;
import pl.javastart.wydatex.database.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private int mBackground;
    private List<Location> locations;

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public final TextView name;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText();
        }
    }

    public LocationAdapter(Context context, List<Location> items) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, typedValue, true);
        mBackground = typedValue.resourceId;
        locations = items;
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder holder, int position) {
        final Location location = locations.get(position);
        holder.name.setText(location.getName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, LocationActivity.class);
                intent.putExtra(LocationActivity.EXTRA_LOCATION_ID, location.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
