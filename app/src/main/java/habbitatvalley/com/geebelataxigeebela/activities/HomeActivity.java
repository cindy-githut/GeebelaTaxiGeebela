package habbitatvalley.com.geebelataxigeebela.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.pkmmte.view.CircularImageView;
import habbitatvalley.com.geebelataxigeebela.R;
import habbitatvalley.com.geebelataxigeebela.fragments.CoveredAreasFragment;
import habbitatvalley.com.geebelataxigeebela.fragments.FindTaxiFragment;
import habbitatvalley.com.geebelataxigeebela.fragments.OptionsFragment;
import habbitatvalley.com.geebelataxigeebela.interfaces.CustomDialog;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawer;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private SharedPreferences prefs = null;
    NavigationView navigationView = null;
    String accessToken = "";
    Toolbar toolbar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = null;
        Class fragmentClass;
        fragmentClass = FindTaxiFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        navigationView();

    }

    @Override
    protected void onResume() {

        super.onResume();

        navigationView();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void navigationView(){
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        prefs = getSharedPreferences(mypreference, MODE_PRIVATE);
        // LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View hView =  navigationView.getHeaderView(0);
        TextView nav_user = (TextView)hView.findViewById(R.id.displayname);
        TextView emailaddress = (TextView) hView.findViewById(R.id.emailaddress);
        CircularImageView profilepicture = (CircularImageView) hView.findViewById(R.id.profilepicture);
        ImageView manage = (ImageView) hView.findViewById(R.id.manage);

        if(prefs.getString("sessionToken", null) != null){

            nav_user.setText(prefs.getString("firstname", null) + " " + prefs.getString("lastname", null));
            emailaddress.setText(prefs.getString("email", null));
            manage.setVisibility(View.VISIBLE);

            Glide.with(this).load(prefs.getString("profileAvatar", null))
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .dontTransform()
                    .placeholder(R.drawable.ico_profpic_placeholder)
                    .error(R.drawable.ico_profpic_placeholder)
                    .into(profilepicture);

            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);

        }else{

            manage.setVisibility(View.GONE);
            navigationView.getMenu().findItem(R.id.nav_logout).setVisible(false);

        }


        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Closing drawer on item click
                mDrawer.closeDrawers();
                startActivityForResult(new Intent(HomeActivity.this, ProfileActivity.class), 10);

            }
        });


        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment fragment = null;
                Class fragmentClass;

                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                //Closing drawer on item click
                mDrawer.closeDrawers();

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){


                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_findtaxi:

                        fragmentClass = FindTaxiFragment.class;

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Insert the fragment by replacing any existing fragment
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                        return true;

                    // For rest of the options we just show a toast on click

                    case R.id.nav_report:


                        if (prefs.getString("sessionToken", null) != null) {

                            fragmentClass = OptionsFragment.class;

                            try {
                                fragment = (Fragment) fragmentClass.newInstance();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            // Insert the fragment by replacing any existing fragment
                             fragmentManager = getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();


                        } else {

                            startActivity(new Intent(HomeActivity.this, SignUpActivity.class));

                        }

                        return true;
                    case R.id.nav_reports:


                        startActivity(new Intent(HomeActivity.this, ReportsActivityStream.class));
                        return true;

                    case R.id.nav_areas:

                        fragmentClass = CoveredAreasFragment.class;

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Insert the fragment by replacing any existing fragment
                         fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

                        return true;
                    case R.id.nav_logout:


                        final CustomDialog logoutDialog = new CustomDialog(HomeActivity.this,
                                "Confirmation", "Are you sure you want to logout?", "CANCEL", "YES");

                        logoutDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                // Dismiss the view
                                if (logoutDialog.getButtonClicked() == logoutDialog.RIGHT_BUTTON) {

                                    logoutDialog.dismiss();
                                    SharedPreferences.Editor editor = getSharedPreferences(mypreference, MODE_PRIVATE).edit();
                                    editor.clear();
                                    editor.commit(); // commit changes
                                    finish();

                                } else {

                                    logoutDialog.dismiss();

                                }
                            }
                        });

                        logoutDialog.show();
                        logoutDialog.setCanceledOnTouchOutside(false);

                        return true;

                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {

            if (resultCode == -1) {

                Toast.makeText(HomeActivity.this, "Profile Successfully Updated.", Toast.LENGTH_SHORT).show();


            } else if (resultCode == 0) {
            }

        }
    }

}
