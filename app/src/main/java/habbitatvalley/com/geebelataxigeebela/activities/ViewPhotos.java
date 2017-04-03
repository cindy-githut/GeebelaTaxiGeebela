package habbitatvalley.com.geebelataxigeebela.activities;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.adapters.ExtendedViewPager;
import me.iwf.photopicker.widget.TouchImageView;

public class ViewPhotos extends AppCompatActivity {

     private ImageView streamimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photos);

        streamimage = (ImageView) findViewById(R.id.streamimage);
        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        setToolbar();
        if(getIntent().hasExtra("multiple")){
            //Multiple photo view here
            mViewPager.setVisibility(View.VISIBLE);
            streamimage.setVisibility(View.GONE);

            try {

                mViewPager.setAdapter(new TouchImageAdapter());

                if(getIntent().hasExtra("setSelected")){

                    if(getIntent().getStringExtra("setSelected") != null){

                        mViewPager.setCurrentItem( Integer.parseInt(getIntent().getStringExtra("setSelected")));

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }else{

            mViewPager.setVisibility(View.GONE);
            streamimage.setVisibility(View.VISIBLE);

            Glide.with(getApplicationContext())
                    .load(getIntent().getStringExtra("photourl"))
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.photo_placeholder)
                    .into(streamimage);
        }

    }
    public void setToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);


        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }

    class TouchImageAdapter extends PagerAdapter {

        private String names = getIntent().getStringExtra("allPhotos");
        private JSONArray images;

        TouchImageAdapter() throws JSONException {
            images = new JSONArray(names);
        }

        @Override
        public int getCount() {

            return images.length();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TouchImageView img = new TouchImageView(container.getContext());
            try {

                Glide.with(getApplicationContext())
                        .load(images.get(position).toString())
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.photo_placeholder)
                        .into(img);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
