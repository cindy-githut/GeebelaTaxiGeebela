package habbitatvalley.com.geebelataxigeebela.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.activities.IncidentOptionsActivity;
import habbitatvalley.com.geebelataxigeebela.activities.LoginActivity;
import habbitatvalley.com.geebelataxigeebela.activities.SignUpActivity;
import habbitatvalley.com.geebelataxigeebela.adapters.ReportsListAdapter;
import habbitatvalley.com.geebelataxigeebela.models.ApiHearders;
import habbitatvalley.com.geebelataxigeebela.models.ReportsListItem;
import habbitatvalley.com.geebelataxigeebela.models.ValidationMethods;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpFragment extends Fragment {

    public static final String TAG = SignUpFragment.class.getSimpleName();
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


    // Saved Instance State
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View convertView =inflater.inflate(R.layout.signup,container,false);

        initializeVariables(convertView);

        //make clickable and hyperlinked
        validationMethods.makeTextViewHyperlink(txtLoginLink);

        return convertView;
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


    public void initializeVariables(View convertView){

        validationMethods = new ValidationMethods();

        txtLoginLink = (TextView) convertView.findViewById(R.id.txtLoginLink);
        name = (EditText) convertView.findViewById(R.id.edfirstname);
        surname = (EditText) convertView.findViewById(R.id.edlastname);
        password = (EditText) convertView.findViewById(R.id.edpassword);
        confirmPassword = (EditText) convertView.findViewById(R.id.edconfirmpassword);
        btnSignUp = (Button) convertView.findViewById(R.id.btnSignUp);
        email = (EditText) convertView.findViewById(R.id.email);
        profileP = (CircularImageView) convertView.findViewById(R.id.profilePicture);
        addphoto= (TextView) convertView.findViewById(R.id.addphoto);
        change = (TextView) convertView.findViewById(R.id.change);
        mobilenumber = (EditText) convertView.findViewById(R.id.mobilenumber);
        progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading....");

    }

}