package habbitatvalley.com.geebelataxigeebela.activities;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by cindymbonani on 2017/03/22.
 */

public interface ApiService {

    @Multipart
    @POST("/retrofit_tutorial/retrofit_client.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file, @Part("file")RequestBody name);

}
