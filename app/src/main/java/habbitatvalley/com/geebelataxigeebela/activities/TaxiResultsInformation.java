package habbitatvalley.com.geebelataxigeebela.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import habbitatvalley.com.geebelataxigeebela.R;

import static habbitatvalley.com.geebelataxigeebela.activities.HomeActivity.mypreference;

public class TaxiResultsInformation extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtDrivingDistance,txtWalkingDistance;
    LinearLayout lblDriving, lblWalking;
    EditText fromLocation,toLocation;
    TextView depart, destination;
    TextView txtdrivingTime, txtwalkingTime, txtPrice, taxiLocation;
    String longitude,latitude;
    RelativeLayout navigate;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi_results_information);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setToolbar();

        initializeVariable();

        if(getIntent().hasExtra("drivingDistance")){

            navigate.setVisibility(View.VISIBLE);

            if(getIntent().getStringExtra("drivingDistance")!= null){
                txtDrivingDistance.setText(getIntent().getStringExtra("drivingDistance"));

            }else{
                txtDrivingDistance.setText("");

            }

            if(getIntent().getStringExtra("walkingDistance")!= null){
                txtWalkingDistance.setText(getIntent().getStringExtra("walkingDistance"));

            }else{
                txtWalkingDistance.setText("");

            }

            if(getIntent().getStringExtra("drivingTime")!= null){
                txtdrivingTime.setText(getIntent().getStringExtra("drivingTime"));

            }else{
                txtdrivingTime.setText("");

            }

            if(getIntent().getStringExtra("walkingTime")!= null){
                txtwalkingTime.setText(getIntent().getStringExtra("walkingTime"));

            }else{
                txtwalkingTime.setText("");

            }


            if(getIntent().hasExtra("fromHome")){
                longitude = getIntent().getStringExtra("longitude");
                latitude = getIntent().getStringExtra("latitude");
            }


            lblDriving.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.putExtra("driving",true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

            lblWalking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });

        }else{

            navigate.setVisibility(View.GONE);

        }

        fromLocation.setText(getIntent().getStringExtra("fromLocation"));
        toLocation.setText(getIntent().getStringExtra("toLocation"));
        destination.setText(getIntent().getStringExtra("toLocation"));
        depart.setText(getIntent().getStringExtra("fromLocation"));
        txtPrice.setText("R"+ prefs.getString("taxiFare", null));
        taxiLocation.setText( prefs.getString("taxiLocation", null));
        prefs.edit().remove("taxiFare").commit();
        prefs.edit().remove("taxiLocation").commit();

    }

    public void initializeVariable(){

        prefs = getSharedPreferences(mypreference, MODE_PRIVATE);
        txtDrivingDistance = (TextView) findViewById(R.id.txtdrivingDistance);
        txtWalkingDistance = (TextView) findViewById(R.id.txtwalkingDistance);
        lblDriving = (LinearLayout) findViewById(R.id.lbldriving);
        lblWalking = (LinearLayout) findViewById(R.id.lblwalking);
        fromLocation = (EditText) findViewById(R.id.fromLocation);
        toLocation = (EditText) findViewById(R.id.destinationLocation);
        depart = (TextView) findViewById(R.id.depart);
        destination = (TextView) findViewById(R.id.destination);
        txtdrivingTime = (TextView) findViewById(R.id.txtdrivingTime);
        txtwalkingTime = (TextView) findViewById(R.id.txtwalkingTime);
        navigate = (RelativeLayout) findViewById(R.id.navigate);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        taxiLocation = (TextView) findViewById(R.id.taxiLocation);
    }
    public void setToolbar(){

        getSupportActionBar();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Taxi Results");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(getIntent().hasExtra("fromhome")){

                    finish();

                }else{

                    Intent intent = new Intent(TaxiResultsInformation.this, HomeActivity.class);
                    intent.putExtra("fromInfo", true);
                    intent.putExtra("fromLocation", fromLocation.getText().toString());
                    intent.putExtra("toLocation", toLocation.getText().toString());
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude",longitude);
                    startActivity(intent);
                    finish();

                }

            }
        });


    }
}
