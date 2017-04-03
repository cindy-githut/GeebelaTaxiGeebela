package habbitatvalley.com.geebelataxigeebela.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.adapters.ReportsListAdapter;
import habbitatvalley.com.geebelataxigeebela.models.ApiHearders;
import habbitatvalley.com.geebelataxigeebela.models.Endpoints;
import habbitatvalley.com.geebelataxigeebela.models.ReportsListItem;
import habbitatvalley.com.geebelataxigeebela.models.ValidationMethods;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BadDrivingFragment extends Fragment {

    public static final String TAG = BadDrivingFragment.class.getSimpleName();
    TextView mSearchText;
    ProgressBar load;
    int currentPosition = 0;
    ReportsListAdapter listAdapter;
    ArrayList<String> usernames;
    public ArrayList<ReportsListItem> listItemArray;
    ReportsListItem listItemContents;
    TextView txtNoFriends;
    ListView listViewFriends;
    Boolean loadmore = true;
    Boolean isloading = false;
    int currentScrollState;
    int previousTotal= 0;
    RelativeLayout loadingmore;
    String loadmoreurl = "";
    ProgressDialog progressDialog;
    Boolean friendsListFetched = false;
    ImageView create_spaza;
    SharedPreferences prefs = null;


    // Saved Instance State
    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putBoolean("friendsListFetched", friendsListFetched);
        outState.putParcelableArrayList("listItems", listItemArray);
        super.onSaveInstanceState(outState);

    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View convertView =inflater.inflate(R.layout.activity_all_reports,container,false);

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        InputMethodManager inputManager = (InputMethodManager) convertView
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        IBinder binder = convertView.getWindowToken();
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS);

        txtNoFriends = (TextView) convertView.findViewById(R.id.txtNoFriends);

        listItemArray = new ArrayList<ReportsListItem>();
        loadingmore = (RelativeLayout) convertView.findViewById(R.id.loadingmore);
        loadingmore.setVisibility(View.GONE);

        load = (ProgressBar) convertView.findViewById(R.id.load);
        new ValidationMethods().changeProgressBarColor(load, getActivity());

        mSearchText = new TextView(getActivity());

        listViewFriends = (ListView) convertView.findViewById(R.id.list_friends);

        listViewFriends.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                currentScrollState = SCROLL_STATE_IDLE;
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(loadmore){

                    if(totalItemCount > previousTotal){
                        loadmore = false;
                        previousTotal = totalItemCount;
                    }

                }

                Log.d("loadmorevalue",""+loadmore + "TotalList " + totalItemCount);

                if ( !loadmore && currentScrollState == SCROLL_STATE_IDLE && (totalItemCount - visibleItemCount) <= firstVisibleItem) {

                    if(!loadmoreurl.equals("")) {

                        // getStreamData(true, false, true);
                        loadingmore.setVisibility(View.VISIBLE);

                        loadmore = true;
                        isloading = true;
                        getAllReports(true);
                    }

                }
            }
        });

        usernames= new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");

        if (!friendsListFetched) {
            getAllReports(true);
        }
        return convertView;
    }


    private void getAllReports(final Boolean isRefresh){

        if(isloading == false) {

            txtNoFriends.setVisibility(View.GONE);
            listItemArray.clear();


            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            //new ApiHearders().addMultipleHearders(getActivity(),httpClient);

            Request request = new Request.Builder()
                    .url(new Endpoints().base_url+"comments/?&type=bad driving&size=30")
                    .header("appToken", new Endpoints().appToken)

                    .build();
            OkHttpClient client = httpClient.build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            load.setVisibility(View.GONE);

                        }
                    });
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                        String responseObject =  response.body().string();
                        System.out.println(responseObject);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            load.setVisibility(View.GONE);

                        }
                    });
                        friendsListFetched = true;


                        try {
                             JSONObject data = new JSONObject(responseObject);
                            JSONArray spazaList = data.getJSONArray("data");

                            try {
                                usernames = new ArrayList<>();

                                listItemArray.clear();

                                for (int i = 0; i < spazaList.length(); i++) {

                                    //JSONObject  = spazaList.getJSONObject(i);

                                    listItemContents = new ReportsListItem();
                                    listItemContents.setReportType("reported bad driving");

                                    if (i == 0) {

                                        listItemContents.setShowUpdateStatus(true);

                                    } else {

                                        listItemContents.setShowUpdateStatus(false);

                                    }
                                    listItemContents.setShowMultiple(false);

                                    listItemContents.setFriendsPicture(spazaList.getJSONObject(i).getJSONObject("author").getString("profilePicName"));
                                    listItemContents.setSpazaAddress("");

                                    if(spazaList.getJSONObject(i).getJSONObject("comment").getString("createdDate") == null || spazaList.getJSONObject(i).getJSONObject("comment").getString("createdDate").equals("null") || spazaList.getJSONObject(i).getJSONObject("comment").getString("createdDate").equals("")){

                                        listItemContents.setTime(spazaList.getJSONObject(i).getJSONObject("comment").getString("lagacyCreatedDate"));

                                    }else{

                                        listItemContents.setTime(spazaList.getJSONObject(i).getJSONObject("comment").getString("createdDate"));

                                    }

                                    JSONArray photos = spazaList.getJSONObject(i).getJSONObject("comment").getJSONArray("photos");

                                    Log.d("PhotosSize", photos.length()+"");
                                    if(photos.length() < 1){
                                        listItemContents.setPhotoAttached("");
                                    }else if(photos.length() == 1){
                                        listItemContents.setPhotoAttached(photos.getJSONObject(0).getString("fileName"));
                                    }else if(photos.length() > 1){
                                        listItemContents.setPhotoAttached("");
                                        listItemContents.setShowMultiple(true);
                                        Log.d("AllPhotos", photos.toString());
                                        listItemContents.setMultiple_photos(photos.toString());
                                    }

                                    listItemContents.setFriendsName(spazaList.getJSONObject(i).getJSONObject("author").getString("firstname") + " " + spazaList.getJSONObject(i).getJSONObject("author").getString("lastname"));

                                    listItemContents.setTitle(spazaList.getJSONObject(i).getJSONObject("comment").getString("title"));
                                    listItemContents.setBody(spazaList.getJSONObject(i).getJSONObject("comment").getString("body"));

                                    listItemContents.setFriendsId(spazaList.getJSONObject(i).getJSONObject("author").getString("userid"));

                                    listItemArray.add(listItemContents);


                                }

                                listAdapter = new ReportsListAdapter(getActivity().getApplicationContext(), listItemArray, getActivity());

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        listViewFriends.setAdapter(listAdapter);

                                    }
                                });



                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "" + e.getMessage());
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                }
            });

        }else{


        }
    }


    /***********************************************************************************************************/
    private void forRowsInList() {
        try {

            listAdapter = new ReportsListAdapter(getActivity().getApplicationContext(), listItemArray, getActivity());
            listViewFriends.setAdapter(listAdapter);

            if(isloading == false){


                listViewFriends.setSelection(0);

            }else{

                Log.d("loadselectionloadHere", "    " + currentPosition);
                listViewFriends.setSelection(currentPosition-1);

                loadingmore.setVisibility(View.GONE);

            }


        } catch (Exception ex) {
            // Log.e(FRIENDS_FRAGMENT, ex.getLocalizedMessage());
        }
    }

}