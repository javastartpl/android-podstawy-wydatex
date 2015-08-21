package pl.javastart.wydatex.location;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.javastart.wydatex.R;
import pl.javastart.wydatex.database.Location;
import pl.javastart.wydatex.database.LocationRepository;
import retrofit.RestAdapter;

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

        updateList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateLocationsAsyncTask asyncTask = new UpdateLocationsAsyncTask();
        asyncTask.execute();
    }

    private void updateList() {
        List<Location> locations = LocationRepository.findAll(getActivity());
        adapter.setLocations(locations);
        adapter.notifyDataSetChanged();
    }


    private class UpdateLocationsAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://webservice-javastartpl.rhcloud.com")
                    .build();

            LocationWebService service = restAdapter.create(LocationWebService.class);

            boolean newDataAdded = false;

            List<Location> locations = null;

            try {
                locations = service.getAll();
            } catch (RuntimeException e) {
                e.printStackTrace();;
            }

            List<Location> databaseLocations = LocationRepository.findAll(getActivity());
            Set<String> locationNames = new HashSet<>();
            for (Location databaseLocation : databaseLocations) {
                locationNames.add(databaseLocation.getName());
            }

            for (Location location : locations) {
                if (!locationNames.contains(location.getName())) {
                    LocationRepository.insert(getActivity(), location);
                    newDataAdded = true;
                }
            }

            return newDataAdded;
        }

        @Override
        protected void onPostExecute(Boolean newDataAdded) {
            if(newDataAdded) {
                updateList();
            }
        }
    }
}
