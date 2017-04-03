package habbitatvalley.com.geebelataxigeebela.models;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by cindymbonani on 2017/03/20.
 */


public class Preferencess {

    Activity activity;

    public Preferencess(Activity activity) {
        this.activity = activity;
    }

    SharedPreferences pref = activity.getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();

}
