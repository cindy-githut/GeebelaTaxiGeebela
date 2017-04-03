package habbitatvalley.com.geebelataxigeebela.adapters;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;
import org.json.JSONArray;
import java.util.ArrayList;
import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.activities.ActionOptionsActivity;
import habbitatvalley.com.geebelataxigeebela.activities.IncidentOptionsActivity;
import habbitatvalley.com.geebelataxigeebela.activities.Report;
import habbitatvalley.com.geebelataxigeebela.activities.SignUpActivity;
import habbitatvalley.com.geebelataxigeebela.activities.ViewPhotos;
import habbitatvalley.com.geebelataxigeebela.models.MultipleimageItem;
import habbitatvalley.com.geebelataxigeebela.models.ReportsListItem;
import habbitatvalley.com.geebelataxigeebela.models.ValidationMethods;

import static android.content.Context.MODE_PRIVATE;
import static habbitatvalley.com.geebelataxigeebela.activities.HomeActivity.mypreference;

public class ReportsListAdapter extends BaseAdapter {

    private Context context;
    public static final String TAG = ReportsListAdapter.class.getSimpleName();

    ReportsListItem currentFriend;
    protected ArrayList<ReportsListItem> listFriends;
    LayoutInflater inflater;
    Activity activity;
    SharedPreferences preferences;
    String type ="other";

    public ReportsListAdapter(Context context, ArrayList<ReportsListItem> listFriends, Activity activity) {

        this.listFriends = listFriends;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.activity = activity;
        preferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());

    }

    @Override
    public int getCount() {
        if (listFriends != null) {
            return listFriends.size();
        }
        return 0;
    }

    @Override
    public ReportsListItem getItem(int position) {
            return listFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = this.inflater.inflate(R.layout.reports_adapter,
                    parent, false);


            holder.txtFullNames = (TextView) convertView
                    .findViewById(R.id.txtText);
            holder.txttitle = (TextView) convertView.findViewById(R.id.txttitle);
            holder.body = (TextView) convertView.findViewById(R.id.txtmaintext);
            holder.txttime = (TextView) convertView.findViewById(R.id.txttime);

            holder.iv_stream_photo = (ImageView) convertView
                    .findViewById(R.id.iv_stream_photo);
//            holder.txtaddress = (TextView) convertView
//                    .findViewById(R.id.txtaddress);
            holder.ppicture = (CircularImageView) convertView
                    .findViewById(R.id.ppicture);

            holder.statusCardView = (CardView) convertView
                    .findViewById(R.id.statusCardView);
            holder.edstatus = (EditText) convertView
                    .findViewById(R.id.edstatus);
            holder.txtaction = (TextView) convertView
                    .findViewById(R.id.txtaction);

            convertView.setTag(holder);
            holder.multiphotoslayout = (LinearLayout) convertView.
                    findViewById(R.id.multiphotoslayout);

            holder.multiplePhotosScrol = (HorizontalScrollView) convertView.findViewById(R.id.multiplePhotosScrol);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Current friend
        currentFriend = listFriends.get(position);
        //sets the users frinend fullname
        holder.txtFullNames.setText(currentFriend.getFriendsName());
        holder.txttitle.setText(currentFriend.getTitle());
        holder.txtaction.setText(currentFriend.getReportType());
        type = currentFriend.getReportType();
        if(currentFriend.getTime() == null || currentFriend.getTime().equals("null") || currentFriend.getTime().equals("")){

            holder.txttime.setText("today");

        }else{

            //convert unix epoch timestamp (seconds) to milliseconds
           // long timestamp = Long.parseLong() * 1000L;

            holder.txttime.setText(new ValidationMethods().formatTimeStamp(Long.parseLong(currentFriend.getTime())));
        }


        try{

            if(currentFriend.getTitle().equals("")){

                holder.txttitle.setVisibility(View.GONE);
            }else{
                holder.txttitle.setVisibility(View.VISIBLE);

            }
        }catch(Exception exxc){
            holder.txttitle.setVisibility(View.GONE);

        }


        holder.body.setText(currentFriend.getBody());

        if(currentFriend.getBody().equals("")){
            holder.body.setVisibility(View.GONE);
        }else{
            holder.body.setVisibility(View.VISIBLE);

        }

        //holder.txtaddress.setText(currentFriend.getSpazaAddress());




        if(currentFriend.getPhotoAttached().equals("")){

            Glide.with(context)
                    .load(R.drawable.photo_placeholder)
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.photo_placeholder)
                    .into(holder.iv_stream_photo);
            holder.iv_stream_photo.setVisibility(View.GONE);

        }else{

            Glide.with(context)
                    .load(currentFriend.getPhotoAttached())
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.photo_placeholder)
                    .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(holder.iv_stream_photo);
            holder.iv_stream_photo.setVisibility(View.VISIBLE);

        }

        if(currentFriend.getFriendsPicture().equals("") || currentFriend.getFriendsPicture().equals("null") || currentFriend.getFriendsPicture() == null){

            Glide.with(context)
                    .load(R.drawable.ico_profpic_placeholder)
                    .centerCrop()
                    .placeholder(R.drawable.ico_profpic_placeholder)
                    .into(holder.ppicture);
        }else{

                Glide.with(context)
                        .load(currentFriend.getFriendsPicture())
                        .centerCrop()
                        .placeholder(R.drawable.ico_profpic_placeholder)
                        .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                        .into(holder.ppicture);

        }

        if(currentFriend.getShowUpdateStatus()){
            holder.statusCardView.setVisibility(View.VISIBLE);
        }else{
            holder.statusCardView.setVisibility(View.GONE);
        }
        holder.multiplePhotosScrol.setTag(position);

        holder.iv_stream_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentv = new Intent(context, ViewPhotos.class);
                intentv.putExtra("photourl", currentFriend.getPhotoAttached());
                intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentv);

            }
        });

        if (currentFriend.getShowMultiple() == true) {

            //multiple attachments
            holder.multiplePhotosScrol.setVisibility(View.VISIBLE);
            holder.iv_stream_photo.setVisibility(View.GONE);

                ArrayList<MultipleimageItem> photos = new ArrayList<MultipleimageItem>();

                MultipleimageItem item;

                FragmentManager fm;

            try {

                Log.d("App", currentFriend.getMultiple_photos());
                JSONArray setMultiple_photos = new JSONArray(currentFriend.getMultiple_photos());
                JSONArray setMultiple_photoss = new JSONArray();

                for (int i = 0; i < setMultiple_photos.length(); i++) {
                    setMultiple_photoss.put(setMultiple_photos.getJSONObject(i).getString("fileName"));

                }
                        for (int i = 0; i < setMultiple_photos.length(); i++) {

                            item = new MultipleimageItem();
                            item.setPhotourl(setMultiple_photos.getJSONObject(i).getString("fileName"));
                            item.setPhotos(setMultiple_photoss);
                            photos.add(item);

                        }

                        multiplePhotosList(photos, context, holder);


                } catch (Exception e) {
                    e.printStackTrace();
                }

        }else{
            holder.multiplePhotosScrol.setVisibility(View.GONE);

        }

        holder.edstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent accidentIntent = new Intent(activity, Report.class);

                if(currentFriend.getReportType().contains("bad")){
                    accidentIntent.putExtra("option","bad driving");
                }else if(currentFriend.getReportType().contains("accident")){
                    accidentIntent.putExtra("option","accident");
                }else{
                    accidentIntent.putExtra("option","other");
                }

                SharedPreferences prefs = activity.getSharedPreferences(mypreference, MODE_PRIVATE);

                if (prefs.getString("sessionToken", null) != null) {

                    activity.startActivity(accidentIntent);

                }else{
                    activity.startActivity(new Intent(activity,SignUpActivity.class));

                }
            }
        });
//        holder.edstatus.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//
//                final Intent accidentIntent = new Intent(activity, Report.class);
//
//                if(currentFriend.getReportType().contains("bad")){
//                    accidentIntent.putExtra("option","bad driving");
//                }else if(currentFriend.getReportType().contains("accident")){
//                    accidentIntent.putExtra("option","accident");
//                }else{
//                    accidentIntent.putExtra("option","other");
//                }
//
//                SharedPreferences prefs = activity.getSharedPreferences(mypreference, MODE_PRIVATE);
//
//                if (prefs.getString("sessionToken", null) != null) {
//
//                    activity.startActivity(accidentIntent);
//
//                }else{
//                    activity.startActivity(new Intent(activity,SignUpActivity.class));
//
//                }
//
//                return true;
//            }
//        });

//        holder.edstatus.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    // Always use a TextKeyListener when clearing a TextView to prevent android
//                    // warnings in the log
//                    TextKeyListener.clear((holder.edstatus).getText());
//                    final Intent accidentIntent = new Intent(activity, Report.class);
//
//                    accidentIntent.putExtra("option","accident");
//                    activity.startActivity(accidentIntent);
//                }
//            }
//        });


        return convertView;
    }

    private void multiplePhotosList(final ArrayList<MultipleimageItem> photo, final Context context, final ReportsListAdapter.ViewHolder holder) {

        final LinearLayout photosLayout = (LinearLayout) holder.multiphotoslayout;
        int position = 0;

        try {
            photosLayout.removeAllViews();

        } catch (Exception exc) {

        }

        for (MultipleimageItem eachPhoto : photo) {

            View view = LayoutInflater.from(context).inflate(R.layout.multiplephotositem, null);
            ImageView photoImage = (ImageView) view.findViewById(R.id.imageView55);

            if (eachPhoto.getPhotourl().equals("") || eachPhoto.getPhotourl() == null || eachPhoto.getPhotourl().equals("null")) {

                Glide.with(context)
                        .load(R.drawable.photo_placeholder)
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.photo_placeholder)
                        .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                        .into(photoImage);
            } else {

                Glide.with(context)
                        .load(eachPhoto.getPhotourl())
                        .thumbnail(0.1f)
                        .placeholder(R.drawable.photo_placeholder)
                        .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                        .into(photoImage);
            }

            final int index = position;
            final MultipleimageItem photoRec = eachPhoto;

            final int finalPosition = position;

            photoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent intentv = new Intent(context, ViewPhotos.class);
                    intentv.putExtra("photourl", photoRec.getPhotourl());
                    intentv.putExtra("multiple", "correct");
                    String posi = finalPosition + "";
                    intentv.putExtra("setSelected", posi);
                    intentv.putExtra("allPhotos", photoRec.getPhotos().toString());
                    intentv.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentv);


                }
            });

            photosLayout.addView(view);
            position++;
        }
    }
    public class ViewHolder {
        TextView txtFullNames, txttitle, body, txttime, txtaction;
        TextView txtMutualFriends;
        ImageView iv_stream_photo;
        TextView txtaddress;
        CardView statusCardView;
        EditText edstatus;
        CircularImageView ppicture;
        LinearLayout multiphotoslayout;
        HorizontalScrollView multiplePhotosScrol;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
