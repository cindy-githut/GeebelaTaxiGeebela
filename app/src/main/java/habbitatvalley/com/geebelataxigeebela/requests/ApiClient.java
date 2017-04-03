package habbitatvalley.com.geebelataxigeebela.requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cindymbonani on 2017/03/22.
 */

public class ApiClient {

    public static final String BASE_URL = "http://41.185.27.102:8080/api/v1/users/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit;
    }
}
