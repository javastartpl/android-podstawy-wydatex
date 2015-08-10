package pl.javastart.wydatex.location;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.javastart.wydatex.R;
import pl.javastart.wydatex.database.Location;
import pl.javastart.wydatex.database.LocationRepository;

public class LocationListFragment extends Fragment {

    private LocationAdapter adapter;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        List<Location> locations = LocationRepository.findAll(getActivity());
        adapter = new LocationAdapter(getActivity(), locations);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Location> locations = LocationRepository.findAll(getActivity());
        adapter.setLocations(locations);
        adapter.notifyDataSetChanged();
    }

}
