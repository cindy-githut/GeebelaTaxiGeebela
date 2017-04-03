package habbitatvalley.com.geebelataxigeebela.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import habbitatvalley.com.geebelataxigeebela.R;

public class ActionOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout findtaxi;
    private Button findbus, report, proced;
    private ImageView info_icon;
    private ProgressBar findtaxiloading;
    private TextView taxitext;
    private LinearLayout optionslayout;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String accessToken = "accessToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_options);

        initializeVariables();
        setOnClicks();



    }

    public void setOnClicks(){

        findtaxi.setOnClickListener(this);
        findbus.setOnClickListener(this);
        report.setOnClickListener(this);
        findtaxiloading.setVisibility(View.GONE);
        taxitext.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        findtaxiloading.setVisibility(View.GONE);
        taxitext.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        findtaxiloading.setVisibility(View.GONE);
        taxitext.setVisibility(View.VISIBLE);

    }

    public void initializeVariables(){

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        findtaxi = (RelativeLayout) findViewById(R.id.btnFindTaxi);
        findbus = (Button) findViewById(R.id.btnFindBus);
        report = (Button) findViewById(R.id.btnReport);
        findtaxiloading = (ProgressBar) findViewById(R.id.findtaxiloading);
        taxitext = (TextView) findViewById(R.id.taxitext);
        optionslayout = (LinearLayout) findViewById(R.id.optionslayout);

        Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
        optionslayout.startAnimation(startAnimation);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnFindTaxi:

                startActivity(new Intent(ActionOptionsActivity.this, HomeActivity.class));

//
//
//                if (ContextCompat.checkSelfPermission(ActionOptionsActivity.this,
//                        Manifest.permission.ACCESS_FINE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//
//                    // Should we show an explanation?
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(ActionOptionsActivity.this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                        ActivityCompat.requestPermissions(ActionOptionsActivity.this,
//                                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
//                                200);
//
//                    } else {
//
//                        // No explanation needed, we can request the permission.
//
//                        ActivityCompat.requestPermissions(ActionOptionsActivity.this,
//                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                200);
//
//                    }
//
//                }else{
//
//
//                }

                break;
            case R.id.btnFindBus:

                break;
            case R.id.btnReport:

                SharedPreferences prefs = getSharedPreferences(mypreference, MODE_PRIVATE);

                if (prefs.getString("sessionToken", null) != null) {

                    startActivity(new Intent(ActionOptionsActivity.this, IncidentOptionsActivity.class));

                }else{

                    startActivity(new Intent(ActionOptionsActivity.this, SignUpActivity.class));

                }

                break;

        }

    }

}
