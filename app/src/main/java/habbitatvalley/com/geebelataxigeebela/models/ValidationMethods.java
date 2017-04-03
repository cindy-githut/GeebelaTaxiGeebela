package habbitatvalley.com.geebelataxigeebela.models;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import habbitatvalley.com.geebelataxigeebela.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cindymbonani on 16/08/27.
 */

public class ValidationMethods {

    //get real date from timestamp
    public String formatTimeStamp(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = android.text.format.DateFormat.format("dd MMM yyyy", cal).toString();

        //get the current date
        //get the current date
        Calendar calm = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
        String todaydate = df.format(calm.getTime());

        //get yesterday's date
        Calendar calend = Calendar.getInstance();
        calend.add(Calendar.DATE, -1);
        df.format(calend.getTime()); //your formatted date here

        Log.d("DateTest",date + " >> " + todaydate);

        return date;
    }

    //making a hyperLink
    public  void makeTextViewHyperlink(TextView tv) {

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        ssb.append(tv.getText());

        ssb.setSpan(new URLSpan("#"), 0, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(ssb, TextView.BufferType.SPANNABLE);

    }
    //password encryption
    public String encryptPassword(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
    public  void changeProgressBarColor(ProgressBar bar, Activity activity){
        bar.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
    }
    public  int getImageOrientation(String imagePath){
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
