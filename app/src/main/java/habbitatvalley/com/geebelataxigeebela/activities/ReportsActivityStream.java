package habbitatvalley.com.geebelataxigeebela.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.adapters.StreamViewPagerAdapter;
import habbitatvalley.com.geebelataxigeebela.models.SlidingTabLayout;

public class ReportsActivityStream extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    StreamViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Accident","Bad Driving","Other"};
    int Numboftabs =3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_stream);
        setToolbar();


        findViewById(R.id.activity_reports_stream).setBackgroundColor(getResources().getColor(R.color.white));

        adapter =  new StreamViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);
        pager.setBackgroundColor(getResources().getColor(R.color.base_blue));


        if(getIntent().hasExtra("type")){

            if(getIntent().getStringExtra("type").equals("bad driving")){
                pager.setCurrentItem(1);
            }else if(getIntent().getStringExtra("type").equals("accident")){
                pager.setCurrentItem(0);
            }else{
                pager.setCurrentItem(2);
            }

        }

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.base_blue);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);



    }
    public void setToolbar(){

        getSupportActionBar();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Incidents");
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
