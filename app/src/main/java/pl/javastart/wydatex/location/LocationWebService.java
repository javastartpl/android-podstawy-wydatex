package pl.javastart.wydatex.location;

import java.util.List;

import pl.javastart.wydatex.database.Location;
import retrofit.Callback;
import retrofit.http.GET;

public interface LocationWebService {

    @GET("/locations/")
    void getAll(Callback<List<Location>> callback);

    @GET("/locations/")
    List<Location> getAll();
}
