package habbitatvalley.com.geebelataxigeebela.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.models.Endpoints;
import habbitatvalley.com.geebelataxigeebela.models.ValidationMethods;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.view.View.VISIBLE;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private TextView txtSignUpLink,
            btnForgotPassword;
    private Button btnsignin;
    private ValidationMethods validationMethods;
    private EditText edpassword,edusername;
    private ProgressBar loaingLogin;
    public ProgressDialog progressDialog;
    public static final String mypreference = "mypref";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        validationMethods = new ValidationMethods();

        initializeVariables();

        //make clickable and hyperlinked
        validationMethods.makeTextViewHyperlink(txtSignUpLink);
        validationMethods.makeTextViewHyperlink(btnForgotPassword);

        onClickMethods();
    }

    public void onClickMethods(){

        txtSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));

            }
        });

        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check for a valid password, if the user entered one.
                if (TextUtils.isEmpty(edusername.getText().toString()) == true) {

                    edusername.setError("Email Address can not be empty");
                    edusername.findFocus();

                }else if (TextUtils.isEmpty(edpassword.getText().toString()) == true) {

                    edpassword.setError("Email Address can not be empty");
                    edpassword.findFocus();

                }else{

                    login(edusername.getText().toString(), edpassword.getText().toString());

                }
            }
        });

    }
    public void initializeVariables(){

        txtSignUpLink = (TextView) findViewById(R.id.txtSignUp);
        btnsignin = (Button) findViewById(R.id.btnFindBus);
        btnForgotPassword = (TextView) findViewById(R.id.btnForgotPassword);
        edpassword = (EditText) findViewById(R.id.edpassword);
        edusername = (EditText) findViewById(R.id.edusername);
        loaingLogin = (ProgressBar) findViewById(R.id.loaingLogin);
        progressDialog = new ProgressDialog(this,R.style.AppCompatAlertDialogStyle);
        progressDialog.setMessage("Logging you in...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void login(final String email, final String password) {

        loaingLogin.setVisibility(VISIBLE);
        btnsignin.setVisibility(View.GONE);
        //check if the password entered is the password on the system
        JSONObject loginRequestPayload = new JSONObject();
        try {

            loginRequestPayload.put("email", email);
            loginRequestPayload.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody body = RequestBody.create(JSON, loginRequestPayload.toString());
        Request request  = new Request.Builder()
                .url(new Endpoints().base_url + "users/login")
                .header("appToken", new Endpoints().appToken)
                .post(body)
                .build();

            // Get a handler that can be used to post to the main thread
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {


                    LoginActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loaingLogin.setVisibility(View.GONE);
                            btnsignin.setVisibility(View.VISIBLE);
                        }
                    });

                    if (!response.isSuccessful()) {


                        throw new IOException("Unexpected code " + response);

                    }else{

                        String responseData = response.body().string();
                        System.out.println(responseData);

                        JSONObject responseObject = null;
                        SharedPreferences.Editor editor = getSharedPreferences(mypreference, MODE_PRIVATE).edit();

                        try {
                            responseObject = new JSONObject(responseData).getJSONObject("data");

                            editor.putString("sessionToken",responseObject.getString("sessionToken"));
                            editor.putString("firstname", responseObject.getString("firstname"));
                            editor.putString("lastname", responseObject.getString("lastname"));
                            editor.putString("email", responseObject.getString("email"));
                            editor.putString("number", responseObject.getString("cellNo"));
                            editor.putString("profileAvatar", responseObject.getString("profilePicName"));
                            editor.putString("userid", responseObject.getString("userid"));
                            editor.commit();

                            startActivity(new Intent(LoginActivity.this, Report.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    }
}
