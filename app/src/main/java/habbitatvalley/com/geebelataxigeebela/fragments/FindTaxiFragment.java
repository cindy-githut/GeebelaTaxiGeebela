package habbitatvalley.com.geebelataxigeebela.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.activities.GPSTracker;
import habbitatvalley.com.geebelataxigeebela.activities.TaxiResultsInformation;
import habbitatvalley.com.geebelataxigeebela.adapters.PlacesAdapter;
import habbitatvalley.com.geebelataxigeebela.interfaces.ExpandableHeightGridView;
import habbitatvalley.com.geebelataxigeebela.models.Endpoints;
import habbitatvalley.com.geebelataxigeebela.models.PlacesItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static habbitatvalley.com.geebelataxigeebela.activities.HomeActivity.mypreference;

public class FindTaxiFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker gps;
    double latitude, longitude;
    SupportMapFragment mapFragment = null;
    private Button btnFindTaxi;
    private EditText fromLocation, toLocation;
    private LinearLayout textboxes;
    private ExpandableHeightGridView lvplaces, lvtoplaces;
    PlacesAdapter adapter;
    String citiy,province;
    ArrayList<String> places;
    PlacesItem placesItem;
    ArrayList<PlacesItem> searchresults;
    SharedPreferences.Editor editor = null;
    String tolocation = "";
    String fromlocation = "";
    ProgressDialog taxi_dialog;

    // Saved Instance State
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View convertView =inflater.inflate(R.layout.findtaxi_fragment,container,false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        initializeVariables(convertView);
//
//        getCurrentLocation();
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        try{
            fromLocation.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {


                    //  Toast.makeText(getApplicationContext(),"searching"+ charSequence.toString(), Toast.LENGTH_SHORT).show();

                    if (!charSequence.toString().equals("")) {

                        lvplaces.setVisibility(View.VISIBLE);
                        lvtoplaces.setVisibility(View.GONE);

                        if (adapter != null) {

                            adapter.places.clear();
                        }

                        if (charSequence.toString().length() >= 3) {

                            searchPlaces(charSequence.toString(), "fromLocation");

                        }


                    } else {

                        lvplaces.setVisibility(View.GONE);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            toLocation.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                    if (!charSequence.toString().equals("")) {

                        lvtoplaces.setVisibility(View.VISIBLE);
                        lvplaces.setVisibility(View.GONE);

                        if (adapter != null) {

                            adapter.places.clear();
                        }

                        if (charSequence.toString().length() >= 3) {

                            searchPlaces(charSequence.toString(), "toLocation");

                        }


                    } else {

                        lvtoplaces.setVisibility(View.GONE);
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }catch(Exception ec){

            Log.e("SaveError",ec.getMessage());
        }

        lvplaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editor.putString("selectedFromLocation", adapter.getItem(i).getName());
                editor.commit();
                fromLocation.setText("");
                fromLocation.append(adapter.getItem(i).getName());
                lvplaces.setVisibility(View.GONE);

            }
        });

        lvtoplaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                editor.putString("selectedToLocation", adapter.getItem(i).getName());
                editor.commit();
                toLocation.setText("");
                toLocation.append(adapter.getItem(i).getName());
                lvtoplaces.setVisibility(View.GONE);

            }
        });

        btnFindTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tolocation = toLocation.getText().toString().trim();
                fromlocation = fromLocation.getText().toString().trim();

                if (TextUtils.isEmpty(toLocation.getText().toString()) == true) {

                    toLocation.setError("destination address can not be empty");
                    toLocation.findFocus();

                }else if(TextUtils.isEmpty(fromLocation.getText().toString())){

                    fromLocation.setError("to address can not be empty");
                    fromLocation.findFocus();

                }else{

                    findTaxi();

                }

            }
        });


        return convertView;
    }

    public void initializeVariables(View convertView){

        btnFindTaxi = (Button) convertView.findViewById(R.id.btnFindTaxi);
        fromLocation = (EditText) convertView.findViewById(R.id.fromLocation);
        toLocation = (EditText) convertView.findViewById(R.id.destinationLocation);
        textboxes = (LinearLayout) convertView.findViewById(R.id.textboxes);
        lvplaces = (ExpandableHeightGridView) convertView.findViewById(R.id.lvplaces);
        lvplaces.setExpanded(true);
        lvtoplaces = (ExpandableHeightGridView) convertView.findViewById(R.id.lvtoplaces);
        lvtoplaces.setExpanded(true);
        places = new ArrayList<String>();
        searchresults = new ArrayList<PlacesItem>();
        editor = getActivity().getSharedPreferences(mypreference, MODE_PRIVATE).edit();

        taxi_dialog = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
        taxi_dialog.setMessage("Finding a taxi, please wait...");
        taxi_dialog.setCanceledOnTouchOutside(false);

    }

    public void findTaxi(){


        lvplaces.setVisibility(View.GONE);
        lvtoplaces.setVisibility(View.GONE);
        taxi_dialog.show();

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        String url = "http://41.185.27.102:8080/api/v1/routes/?from="+fromLocation.getText().toString().replaceAll(" ","").replaceAll("&","%26")+"&to="+toLocation.getText().toString().replaceAll(" ","").replaceAll("&","%26");

        Request request = new Request.Builder()
                .url(url)
                .header("appToken", new Endpoints().appToken)
                .build();
        OkHttpClient client = httpClient.build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();

                taxi_dialog.dismiss();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Toast.makeText(getActivity(), "Taxi information not found, please try near by places or select from our place list that shows up on search", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                taxi_dialog.dismiss();

                if (!response.isSuccessful()) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            Toast.makeText(getActivity(), "Taxi information not found, please try near by places or select from our place list that shows up on search", Toast.LENGTH_LONG).show();
                        }
                    });
                    throw new IOException("Unexpected code " + response);

                }else{

                    String responseObject =  response.body().string();
                    System.out.println(responseObject);

                    Log.d("TAG",response.toString());


                    try {
                        JSONObject responseObjectData = new JSONObject(responseObject);

                        if(responseObjectData.getString("message").equals("Request successful")){

                            if(responseObjectData.getString("data") != null && !responseObjectData.getString("data").equals("null")){
                                JSONArray list = responseObjectData.getJSONArray("data");

                                if(list.length() < 1){

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            Toast.makeText(getActivity(), "Taxi information not found.", Toast.LENGTH_LONG).show();
                                        }
                                    });

                                }else{


                                    JSONObject  results;
                                    String taxiLocation = "";
                                    String taxiPrice = "";

                                    for(int i = 0 ; i <list.length() ; i++){

                                        results = list.getJSONObject(i);

                                        JSONArray taxiInfo  = results.getJSONArray("taxiRanksInfo");

                                        for(int z = 0 ; z < taxiInfo.length() ; z++){

                                            taxiLocation = taxiInfo.getJSONObject(z).getString("taxilocation");
                                            taxiPrice = taxiInfo.getJSONObject(z).getString("price");

                                        }
                                    }

                                    editor.putString("taxiLocation", taxiLocation);
                                    editor.putString("taxiFare", taxiPrice);
                                    editor.commit();

                                    Intent intent = new Intent(getActivity(), TaxiResultsInformation.class);
                                    intent.putExtra("fromLocation", fromlocation);
                                    intent.putExtra("toLocation", tolocation);
                                    intent.putExtra("fromhome", true);

                                    startActivityForResult(intent,100);

                                }


                            }else{

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(getActivity(), "Taxi information not found, please try near by places or select from our place list that shows up on search", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }


                        }else{

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Taxi information not found, please try near by places or select from our place list that shows up on search", Toast.LENGTH_LONG).show();

                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


    }
    public void searchPlaces(String query, final String whatLocation){

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            String url = "http://41.185.27.102:8080/api/v1/places/?query="+query.replaceAll(" ","%20");

            Request request = new Request.Builder()
                    .url(url)
                    .header("appToken", new Endpoints().appToken)
                    .build();
            OkHttpClient client = httpClient.build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                        }
                    });
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    String responseObject =  response.body().string();
                    System.out.println(responseObject);

                        Log.d("TAG",response.toString());
                        try {

                            JSONObject list = new JSONObject(responseObject);
                            JSONObject friend;
                            JSONObject dataStream = list.getJSONObject("data");
                            if (dataStream.length() > 0) {

                                if (adapter != null) {

                                    adapter.places.clear();
                                }

                                try {

                                    JSONArray contentArray  = dataStream.getJSONArray("content");

                                    for(int i = 0 ; i <contentArray.length() ; i++){

                                        JSONArray arrayyye  = contentArray.getJSONArray(i);
                                        for(int z = 0 ; z < arrayyye.length() ; z++){

                                            String data = arrayyye.getJSONObject(z).getString("name");

                                            if(z == 1){

                                                citiy = data;

                                            }else{
                                                province = data;

                                            }

                                            places.add(data);


                                            placesItem = new PlacesItem();
                                        }


                                        placesItem.setName(citiy + ", " + province);
                                        searchresults.add(placesItem);
                                    }

                                    adapter = new PlacesAdapter(getActivity().getApplicationContext(), searchresults);

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            if(whatLocation.equals("fromLocation")){
                                                lvplaces.setAdapter(adapter);

                                            }else{
                                                lvtoplaces.setAdapter(adapter);

                                            }


                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }else{
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(getActivity(),"No results found",Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }
            });


    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
//
//        mMap = googleMap;
//
//        // Add a marker in your current location and move the camera
//        Log.e("tagrr", "ggggg");
//
//        LatLng location = new LatLng(Double.parseDouble("-26.0964475"), Double.parseDouble( "28.0011458"));
//        Log.e("tagrr", "ggggg1");
//        mMap.addMarker(new MarkerOptions().position(location).title(getAddress(Double.parseDouble("-26.0964475"),Double.parseDouble( "28.0011458"))));
//        Log.e("tagrr", "ggggg2");
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));

    }

    /**
     * Get address name from lat and long
     */

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getAddressLine(0) + ", "+  address.getLocality() + " " + address.getPostalCode());
            }

        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }
    /**
     * get current location to pin on the map
     */
    private void getCurrentLocation(){

        // create class object
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.latitude;
            longitude = gps.longitude;

            Log.d("AddressHere",latitude + ":"+ longitude + ":"+getAddress(Double.parseDouble("-26.0964475"),Double.parseDouble( "28.0011458")));

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}