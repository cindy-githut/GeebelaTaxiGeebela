package habbitatvalley.com.geebelataxigeebela.activities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cindymbonani on 2017/03/22.
 */

class ServerResponse {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    @SerializedName("success")

    boolean success;


}
