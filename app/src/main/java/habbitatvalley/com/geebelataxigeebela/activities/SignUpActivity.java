package habbitatvalley.com.geebelataxigeebela.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
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
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.pkmmte.view.CircularImageView;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.interfaces.CustomDialog;
import habbitatvalley.com.geebelataxigeebela.models.Endpoints;
import habbitatvalley.com.geebelataxigeebela.models.ValidationMethods;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = SignUpActivity.class.getSimpleName();
    private ValidationMethods validationMethods;
    private TextView txtLoginLink;
    private EditText name,surname,mobilenumber, password, confirmPassword, email;
    private Button btnSignUp;
    Uri selectedImage,tempUri;
    File imgFile, newfile;
    private TextView addphoto,change;
    private Toolbar toolbar;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    private CircularImageView profileP;
    public static final String mypreference = "mypref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        setToolbar();
        initializeVariables();

        //make clickable and hyperlinked
        validationMethods.makeTextViewHyperlink(txtLoginLink);

    }

    public void validateSignUpFields() throws Exception {

        String passwordEntered = password.getText().toString().trim();
        String confirmPasswordEntered = confirmPassword.getText().toString().trim();
//
        if(imgFile == null){
            Toast.makeText(this, "Please add a profile picture.", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(name.getText().toString())) {

            name.setError("First name can not be empty");
            name.findFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(name.getWindowToken(), 0);
        }
        else if (TextUtils.isEmpty(surname.getText().toString())) {

            surname.setError("Last name can not be empty");
            surname.findFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(surname.getWindowToken(), 0);

        }else if(!isEmailValid(email.getText().toString(), email)){

            email.findFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(email.getWindowToken(), 0);

        }
        else if (TextUtils.isEmpty(mobilenumber.getText().toString())) {

            mobilenumber.setError("Mobile number can not be empty");
            mobilenumber.findFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mobilenumber.getWindowToken(), 0);

        }
        else if (TextUtils.isEmpty(passwordEntered) || passwordEntered.length() < 6) {

            password.setError("Password should be 6 characters long");
            password.findFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
        } else if(TextUtils.isEmpty(confirmPasswordEntered)){

            confirmPassword.setError("Please confirm your password");
            confirmPassword.findFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(confirmPassword.getWindowToken(), 0);

        }else if(!confirmPasswordEntered.equals(passwordEntered)){

            confirmPassword.setError("Password does not match");
            confirmPassword.findFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(confirmPassword.getWindowToken(), 0);

        }else if (TextUtils.isEmpty(email.getText().toString()) == true) {

            email.setError("email address can not be empty");
            email.findFocus();

        }else {

            upload();

        }


    }
    public boolean isEmailValid(String email,TextView view) {

        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        if(!check)
        {
            view.setError("The email address that you have entered is invalid. Please enter a valid email address");
            view.findFocus();

        }
        return check;
    }
    public void openGalleryPics(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");

        startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Profile Photo"), 100);
    }


    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private final OkHttpClient client = new OkHttpClient();

    public void upload() throws Exception {

        progressDialog.show();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("firstname", name.getText().toString())
                .addFormDataPart("lastname", surname.getText().toString())
                .addFormDataPart("email", email.getText().toString())
                .addFormDataPart("password", confirmPassword.getText().toString())
                .addFormDataPart("cellNo", mobilenumber.getText().toString())
                .addFormDataPart("file", newfile.getName(),RequestBody.create(MEDIA_TYPE_PNG, newfile))
                .build();

        Request request = new Request.Builder()
                .header("appToken", new Endpoints().appToken)
                .url(new Endpoints().base_url+"users")
                .post(requestBody)
                .build();

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
                getContentResolver().delete(tempUri, null, null);

                if (!response.isSuccessful()) {

                    SignUpActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(SignUpActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();

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

                    Intent intent = new Intent(SignUpActivity.this,IncidentOptionsActivity.class);
                    intent.putExtra("status","successful");
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    public void initializeVariables(){

        validationMethods = new ValidationMethods();

        txtLoginLink = (TextView) findViewById(R.id.txtLoginLink);
        name = (EditText) findViewById(R.id.edfirstname);
        surname = (EditText) findViewById(R.id.edlastname);
        password = (EditText) findViewById(R.id.edpassword);
        confirmPassword = (EditText) findViewById(R.id.edconfirmpassword);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        email = (EditText) findViewById(R.id.email);
        profileP = (CircularImageView) findViewById(R.id.profilePicture);
        addphoto= (TextView) findViewById(R.id.addphoto);
        change = (TextView) findViewById(R.id.change);
        mobilenumber = (EditText) findViewById(R.id.mobilenumber);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Registering....");

        profileP.setOnClickListener(this);
        change.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        txtLoginLink.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.profilePicture:
                try{
                    if (ContextCompat.checkSelfPermission(SignUpActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        if (ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(SignUpActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            Log.d("Accesss","deniedStart");
                            ActivityCompat.requestPermissions(SignUpActivity.this,
                                    new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    200);

                        } else {

                            // No explanation needed, we can request the permission.

                            ActivityCompat.requestPermissions(SignUpActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    200);


                        }
                    }else{

                        openGalleryPics();

                    }

                }catch(Exception exc){

                }

                break;
            case R.id.change:

                openGalleryPics();

                break;
            case R.id.btnSignUp:

                try {
                    validateSignUpFields();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.txtLoginLink:

                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish();

                break;

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 200: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d("Accesss","continue");
                  openGalleryPics();

                } else {

                    Log.d("Accesss", "required");
                    final CustomDialog codeDialog = new CustomDialog(SignUpActivity.this,
                            "Notification","By denying access to this files, you won't be able to attach a picture." , "Accept","Cancel");

                    codeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // Dismiss the view
                            if (codeDialog.getButtonClicked() == codeDialog.RIGHT_BUTTON) {

                                codeDialog.dismiss();

                            }else{

                                ActivityCompat.requestPermissions(SignUpActivity.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        200);

                            }
                        }

                    });

                    codeDialog.show();
                    codeDialog.setCanceledOnTouchOutside(false);

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 100) {

            if(resultCode == RESULT_OK) {
                selectedImage = data.getData();

                imgFile = new File(getPath(selectedImage));

                long fileSizeInMB =calculateFileSizeInMB(imgFile);

                if (fileSizeInMB < 0){
                    //enable audio creation

                    Toast.makeText(this, "File too small", Toast.LENGTH_LONG).show();

                }else if(fileSizeInMB <= 1){

                    performCrop(Uri.fromFile(imgFile));

                }else{
                    Toast.makeText(this, "You cannot upload a photo of more than 1MB, try a smaller size photo", Toast.LENGTH_LONG).show();

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

                        profileP.setImageBitmap(thePic);
                        addphoto.setVisibility(View.GONE);
                        change.setVisibility(View.VISIBLE);

                    }

                }catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public String getRealPathFromURI(Uri uri) {

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);

    }

    public static long calculateFileSizeInMB(File file){

        // Get length of file in bytes
        long size = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = size / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;

        return fileSizeInMB;

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
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
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

    public void setToolbar(){

        getSupportActionBar();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Sign Up");
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
