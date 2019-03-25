package pl.javastart.wydatex.location;

import java.util.List;

import pl.javastart.wydatex.database.Location;
import retrofit2.Call;
import retrofit2.http.GET;

public interface LocationWebService {

    @GET("locations")
    Call<List<Location>> getAll();
}
