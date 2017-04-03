package habbitatvalley.com.geebelataxigeebela.models;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cindymbonani on 2017/03/27.
 */

public class ApiHearders {

    String accessToken = null;
    public static final String mypreference = "mypref";

    public ApiHearders() {

    }

    public void addMultipleHearders(Activity activity , OkHttpClient.Builder httpClient){

        SharedPreferences prefs = activity.getSharedPreferences(mypreference, MODE_PRIVATE);
        accessToken = prefs.getString("sessionToken", null);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("appToken", new Endpoints().appToken)
                        .header("sessionToken",accessToken)
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

    }
}
