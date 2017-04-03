package habbitatvalley.com.geebelataxigeebela.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import com.bumptech.glide.RequestManager;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.adapters.NewPostPhotoAdapter;
import habbitatvalley.com.geebelataxigeebela.models.ApiHearders;
import habbitatvalley.com.geebelataxigeebela.models.Endpoints;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Retrofit;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Report extends AppCompatActivity {

    private RequestManager glide;
    private Toolbar toolbar;
    private Spinner spinner;
    private LinearLayout attach_photo;
    public static final int CAMERA_REQUEST = 3;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private ArrayList<Uri> tempUris = new ArrayList<>();
    private RelativeLayout firstimage, secondimage, thirdimage;
    private LinearLayout  twoPhotosAttached, threePhotosAttached;
    ProgressDialog progressDialog;

    int imageSize;
    ImageView image1, image2, image3;
    private ImageView imgremove1, imgremove2, imgremove3;
    RecyclerView recyclerView;
    NewPostPhotoAdapter photoAdapter;
    List<String> photos = null;
    EditText edtitle, edbody;
    ProgressDialog dialog;
    Uri tempUri;
    File imgFile;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);

        //hide the keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setToolbar();
        initializeVariables();

        if(getIntent().hasExtra("option")){
            if(getIntent().getStringExtra("option").equals("bad driving")){
                spinner.setSelection(0);
            }else if(getIntent().getStringExtra("option").equals("accident")){
                spinner.setSelection(1);
            }else {
                spinner.setSelection(2);
            }
        }


        attach_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    if (ContextCompat.checkSelfPermission(Report.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(Report.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            // Show an expanation to the user *asynchronously* -- don't block
                            // this thread waiting for the user's response! After the user
                            // sees the explanation, try again to request the permission.

                            Log.d("Accesss","deniedStart");
                            ActivityCompat.requestPermissions(Report.this,
                                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(Report.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    1);
                            Log.d("Accesss", "accepted");

                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                    }else{

                        PhotoPicker.builder()
                                .setPhotoCount(3)
                                .setShowCamera(true)
                                .setSelected(selectedPhotos)
                                .start(Report.this);

                    }
                }catch(Exception exc){

                }


            }
        });
    }

    public void initializeVariables(){

        spinner = (Spinner) findViewById(R.id.spinner);
        attach_photo = (LinearLayout) findViewById(R.id.attach_photo);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        photoAdapter = new NewPostPhotoAdapter(this, selectedPhotos);
        edtitle = (EditText) findViewById(R.id.edtitle);
        edbody = (EditText) findViewById(R.id.edbody);
        firstimage = (RelativeLayout) findViewById(R.id.firstimage);
        secondimage = (RelativeLayout) findViewById(R.id.secondimage);
        thirdimage = (RelativeLayout) findViewById(R.id.thirdimage);
        image1 = (ImageView) findViewById(R.id.imgpreview1);
        image2 = (ImageView) findViewById(R.id.imgpreview2);
        image3 = (ImageView) findViewById(R.id.imgpreview3);
        imgremove1 = (ImageView) findViewById(R.id.imgremove1);
        imgremove2 = (ImageView) findViewById(R.id.imgremove2);
        imgremove3 = (ImageView) findViewById(R.id.imgremove3);

        dialog = new ProgressDialog(Report.this);
        dialog.setMessage("Posting.....");
        dialog.setCanceledOnTouchOutside(false);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

    }
    public void setToolbar(){

        getSupportActionBar();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Report Incident");
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_material);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_post) {

            if (TextUtils.isEmpty(edtitle.getText().toString())) {

                edtitle.setError("Ttitle can not be empty");
                edtitle.findFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtitle.getWindowToken(), 0);
            } else if (TextUtils.isEmpty(edbody.getText().toString())) {

                edbody.setError("Body text can not be empty");
                edbody.findFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edbody.getWindowToken(), 0);

            }else{

                uploadIcident();

            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_incident, menu);

        return true;
    }

    private void setColumnNumber(int columnNumber) {
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE) {

            if(resultCode == RESULT_OK){


                if (data != null) {
                    photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                }

                selectedPhotos.clear();

                if (photos != null) {

                    selectedPhotos.addAll(photos);

                }

                if(selectedPhotos.size() <= 0){

                    recyclerView.setVisibility(GONE);

                }else{

                    if(selectedPhotos.size() == 1){

                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, OrientationHelper.VERTICAL));


                    }else if(selectedPhotos.size() == 2 ){

                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));


                    }else if(selectedPhotos.size() == 3 ){

                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL));


                    }


                    recyclerView.setVisibility(VISIBLE);

                }

                photoAdapter.notifyDataSetChanged();

            }

        }

    }


    private void uploadIcident(){

        dialog.show();
        String type = "other";

        if(spinner.getSelectedItem().toString().equals("Report Bad Driving")){
            type = "bad driving";
        }else if(spinner.getSelectedItem().toString().equals("Report Accident")){
            type = "accident";
        }else{
            type = "other";
        }

        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", edtitle.getText().toString())
                .addFormDataPart("body", edbody.getText().toString())
                .addFormDataPart("commentType", type);

        if(recyclerView.getVisibility() == VISIBLE) {
            for (int i = 0; i < selectedPhotos.size(); i++) {

                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // down sizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                final Bitmap bitmap = BitmapFactory.decodeFile(selectedPhotos.get(i).toString());
                Matrix matrix = new Matrix();
                matrix.postRotate(getImageOrientation(selectedPhotos.get(i).toString()));


                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                tempUri = getImageUri(getApplicationContext(), rotatedBitmap);
                //getRealPathFromURI(Uri.parse(photos.get(0)))
                imgFile = new File(getRealPathFromURI(tempUri));

                tempUris.add(tempUri);

                buildernew.addFormDataPart("files", imgFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, imgFile));


            }
        }

        MultipartBody requestBody = buildernew.build();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        new ApiHearders().addMultipleHearders(this, httpClient);

        Request request = new Request.Builder()
                .url(new Endpoints().base_url+"comments")
                .post(requestBody)
                .build();

        OkHttpClient client = httpClient.build();

        // Get a handler that can be used to post to the main thread
        final String finalType = type;

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                dialog.dismiss();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                dialog.dismiss();

                    try{
                        if(recyclerView.getVisibility() == VISIBLE) {
                            for (int i = 0; i < tempUris.size(); i++) {

                                if (tempUris.size() > 0) {

                                    getContentResolver().delete(tempUris.get(i), null, null);

                                }
                            }

                            if (tempUris != null) {
                                tempUris.clear();
                            }
                        }

                    }catch(Exception exc){

                    }

                if (!response.isSuccessful()) {


                    Report.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(Report.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

                        }
                    });


                    throw new IOException("Unexpected code " + response);

                }else{

                    String responseData = response.body().string();
                    System.out.println(responseData);

                    Intent intent = new Intent(Report.this, ReportsActivityStream.class);
                    intent.putExtra("posted","successfule");

                    if(getIntent().hasExtra("option")){
                        intent.putExtra("type", finalType);
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public static int getImageOrientation(String imagePath){
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
    public String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }
}
