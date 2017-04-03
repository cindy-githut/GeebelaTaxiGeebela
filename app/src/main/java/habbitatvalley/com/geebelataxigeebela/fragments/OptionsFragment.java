package habbitatvalley.com.geebelataxigeebela.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.activities.IncidentOptionsActivity;
import habbitatvalley.com.geebelataxigeebela.activities.Report;
import habbitatvalley.com.geebelataxigeebela.models.ValidationMethods;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

public class OptionsFragment extends Fragment {

    private Button reportBadDriving, reportBadAccident, reportOther;
    private ImageView info_icon;
    private Toolbar toolbar;
    private LinearLayout successful;
    private Toolbar app_bar;

    // Saved Instance State
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View convertView =inflater.inflate(R.layout.activity_incident_options,container,false);

        initializeVariables(convertView);


        return convertView;
    }

    public void initializeVariables(View convertView){

        app_bar = (Toolbar) convertView.findViewById(R.id.app_bar);
        app_bar.setVisibility(View.GONE);
        reportBadDriving = (Button) convertView.findViewById(R.id.btnBadDriving);
        reportBadAccident = (Button) convertView.findViewById(R.id.btnAccident);
        reportOther = (Button) convertView.findViewById(R.id.btnOther);
        successful = (LinearLayout) convertView.findViewById(R.id.successful);

        reportBadDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Report.class);
                intent.putExtra("option","bad driving");
                startActivity(intent);
            }
        });

        reportBadAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Report.class);
                intent.putExtra("option","accident");
                startActivity(intent);
            }
        });

        reportOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Report.class);
                intent.putExtra("option","other");
                startActivity(intent);
            }
        });
    }

}