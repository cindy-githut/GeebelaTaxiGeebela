package habbitatvalley.com.geebelataxigeebela.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import habbitatvalley.com.geebelataxigeebela.R;


public class IncidentOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    Button reportBadDriving, reportBadAccident, reportOther;
    ImageView info_icon;
    Toolbar toolbar;
    LinearLayout successful;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_options);

        //hide the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setToolbar();
        initializeVariables();
        setOnClicks();

        if(getIntent().hasExtra("status")){
            successful.setVisibility(View.VISIBLE);
        }

    }

    public void setOnClicks(){

        reportBadDriving.setOnClickListener(this);
        reportBadAccident.setOnClickListener(this);
        reportOther.setOnClickListener(this);

    }
    public void initializeVariables(){

        reportBadDriving = (Button) findViewById(R.id.btnBadDriving);
        reportBadAccident = (Button) findViewById(R.id.btnAccident);
        reportOther = (Button) findViewById(R.id.btnOther);
        successful = (LinearLayout) findViewById(R.id.successful);

    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.btnBadDriving:
                //we don't need this covered areas
                //showDialog();
                successful.setVisibility(View.GONE);

                Intent intent = new Intent(IncidentOptionsActivity.this, Report.class);
                intent.putExtra("option","bad driving");
                startActivity(intent);

                break;

            case R.id.btnAccident:
                successful.setVisibility(View.GONE);

                Intent accidentIntent = new Intent(IncidentOptionsActivity.this, Report.class);
                accidentIntent.putExtra("option","accident");
                startActivity(accidentIntent);

                break;

            case R.id.btnOther:

                successful.setVisibility(View.GONE);

                Intent otherIntent = new Intent(IncidentOptionsActivity.this, Report.class);
                otherIntent.putExtra("option","other");
                startActivity(otherIntent);

                break;

        }

    }


    public void setToolbar(){

        getSupportActionBar();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Incident Options");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                finish();

            }
        });


    }
}
