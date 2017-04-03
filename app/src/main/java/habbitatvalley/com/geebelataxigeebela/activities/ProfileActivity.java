package habbitatvalley.com.geebelataxigeebela.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.models.ApiHearders;
import habbitatvalley.com.geebelataxigeebela.models.Endpoints;
import habbitatvalley.com.geebelataxigeebela.models.ValidationMethods;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static habbitatvalley.com.geebelataxigeebela.activities.HomeActivity.mypreference;

public class ProfileActivity extends AppCompatActivity {

    private Button reportBadDriving, reportBadAccident, reportOther;
    private ImageView info_icon;
    private Toolbar toolbar;
    private LinearLayout successful;
    private ImageView changeProfilePicture;
    Uri selectedImage,tempUri;
    File imgFile, newfile;
    private CircularImageView profilePicture;
    private EditText name, lname, num, email;;
    private Button save;
    SharedPreferences prefs = null;
    ProgressDialog progressDialog;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_fragment);

        setToolbar();
        initializeVariables();
        prefs = getSharedPreferences(mypreference, MODE_PRIVATE);

        name.append(prefs.getString("firstname", null));
        lname.append(prefs.getString("lastname", null));
        email.append(prefs.getString("email", null));
        num.append(prefs.getString("number", null));

        Glide.with(this).load(prefs.getString("profileAvatar", null))
                .thumbnail(0.1f)
                .dontAnimate()
                .dontTransform()
                .placeholder(R.drawable.ico_profpic_placeholder)
                .error(R.drawable.ico_profpic_placeholder)
                .into(profilePicture);

    }
    public void setToolbar(){

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);

        toolbar.setTitle("My Profile");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }
    private void openGallery(){

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Profile Photo"), 100);

    }

    public String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }

    public void initializeVariables(){

        changeProfilePicture = (ImageView) findViewById(R.id.changeProfilePicture);
        changeProfilePicture.bringToFront();
        profilePicture = (CircularImageView) findViewById(R.id.profilePicture);
        name = (EditText) findViewById(R.id.firstname);
        lname = (EditText) findViewById(R.id.lastname);
        email = (EditText) findViewById(R.id.email);
        num = (EditText) findViewById(R.id.number);
        save = (Button) findViewById(R.id.btnsave);
        progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Uploading, please wait....");


        changeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        Log.d("Accesss","deniedStart");
                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                200);

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(ProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                200);

                    }

                }else{

                    openGallery();

                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    upload();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void upload() throws Exception {

        progressDialog.show();

        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("firstname", name.getText().toString())
                .addFormDataPart("lastname", lname.getText().toString())
                .addFormDataPart("email", email.getText().toString())
                .addFormDataPart("cellNo", num.getText().toString());

                if(newfile != null){
                    buildernew.addFormDataPart("file", newfile.getName(), RequestBody.create(MEDIA_TYPE_PNG, newfile));
                }


        MultipartBody requestBody = buildernew.build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        new ApiHearders().addMultipleHearders(ProfileActivity.this, httpClient);

        Request request = new Request.Builder()
                .url(new Endpoints().base_url+"users/" + prefs.getString("userid", null))
                .post(requestBody)
                .build();

        OkHttpClient client = httpClient.build();

        // Get a handler that can be used to post to the main thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                progressDialog.dismiss();
                if(tempUri != null){
                    getContentResolver().delete(tempUri, null, null);

                }

                if (!response.isSuccessful()) {

                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(ProfileActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                        }
                    });

                    throw new IOException("Unexpected code " + response);

                }else{

                    String responseData = response.body().string();
                    System.out.println(responseData);
                    JSONObject responseObject = null;
                    SharedPreferences.Editor editor = getSharedPreferences(mypreference, MODE_PRIVATE).edit();

                    try {
                        responseObject = new JSONObject(responseData);

                        editor.putString("sessionToken", responseObject.getJSONObject("data").getString("sessionToken"));
                        editor.putString("firstname", responseObject.getJSONObject("data").getString("firstname"));
                        editor.putString("lastname", responseObject.getJSONObject("data").getString("lastname"));
                        editor.putString("email", responseObject.getJSONObject("data").getString("email"));
                        editor.putString("number", responseObject.getJSONObject("data").getString("cellNo"));
                        editor.putString("profileAvatar", responseObject.getJSONObject("data").getString("profilePicName"));
                        editor.putString("userid", responseObject.getJSONObject("data").getString("userid"));
                        editor.commit();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ProfileActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(ProfileActivity.this, "Profile Successfully Updated", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK, getIntent());
                            finish();
                        }
                    });

                }
            }
        });

    }
    public String getPath(Uri uri) {

        if( uri == null ) {
            return null;
        }

        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        return uri.getPath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if (requestCode == 100) {

            if(resultCode == RESULT_OK) {
                selectedImage = data.getData();

                imgFile = new File(getPath(selectedImage));

                long fileSizeInMB =new SignUpActivity().calculateFileSizeInMB(imgFile);

                if (fileSizeInMB < 0){
                    //enable audio creation

                    Toast.makeText(ProfileActivity.this, "File too small", Toast.LENGTH_LONG).show();

                }else if(fileSizeInMB <= 1){

                    performCrop(Uri.fromFile(imgFile));

                }else{
                    Toast.makeText(ProfileActivity.this, "You cannot upload a photo of more than 1MB, try a smaller size photo", Toast.LENGTH_LONG).show();

                }


            }

        }
        if (requestCode == 2) {

            if(resultCode == RESULT_OK) {
                //Create an instance of bundle and get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap from extras
                Bitmap thePic = extras.getParcelable("data");

                Matrix matrix = new Matrix();
                matrix.postRotate(new ValidationMethods().getImageOrientation(getPath(selectedImage)));


                try{

                    if (imgFile.exists()) {

                        tempUri = new ValidationMethods().getImageUri(getApplicationContext(), thePic);

                        newfile = new File(getRealPathFromURI(tempUri));

                        profilePicture.setImageBitmap(thePic);

                    }

                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }


    private void performCrop(Uri picUri){
        try {
//call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on returngetImageOrientation
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 2);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "oops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
