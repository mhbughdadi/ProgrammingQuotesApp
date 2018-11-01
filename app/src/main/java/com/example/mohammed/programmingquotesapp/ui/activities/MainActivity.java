package com.example.mohammed.programmingquotesapp.ui.activities;

import android.app.ActivityOptions;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mohammed.programmingquotesapp.R;
import com.example.mohammed.programmingquotesapp.data.QuoteContract;
import com.example.mohammed.programmingquotesapp.data.model.Quote;
import com.example.mohammed.programmingquotesapp.gcm.RegisterationIntentService;
import com.example.mohammed.programmingquotesapp.interfaces.ListClickListener;
import com.example.mohammed.programmingquotesapp.sync.QuoteAppSyncAdapter;
import com.example.mohammed.programmingquotesapp.ui.fragments.DetailsFragment;
import com.example.mohammed.programmingquotesapp.ui.fragments.MainListfragment;
import com.example.mohammed.programmingquotesapp.ui.fragments.SettingsFragment;
import com.example.mohammed.programmingquotesapp.utilities.Utility;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity implements ListClickListener{
    private final String TAG=MainActivity.class.getSimpleName();
    private AdView adView;
    MainListfragment mainListfragment;
    private  CollapsingToolbarLayout collapsingToolbarLayout;
    public static final String SENT_TOKEN_TO_SERVER="sentTokenToServer";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private static  final int PLAY_SERVICES_RESOLVE=10;
    private boolean mTwoPane;
    private String mLocation;
    private int mPosition= RecyclerView.NO_POSITION;
    private boolean checkGooogleApi(){
        GoogleApiAvailability googleApiAvailability=GoogleApiAvailability.getInstance();
        int resCode=googleApiAvailability.isGooglePlayServicesAvailable(this);
//        Toast.makeText(this, "res Codes  :"+resCode+" sucsses   :"+ConnectionResult.SUCCESS, Toast.LENGTH_SHORT).show();
        if(resCode!= ConnectionResult.SUCCESS)
        {
            if(googleApiAvailability.isUserResolvableError(resCode)){
                googleApiAvailability.getErrorDialog(this,resCode,PLAY_SERVICES_RESOLVE).show();
            }
            else {
                Log.i("PLAY SERVICES","your moble is not supported");
                finish();
            }
            return false;
        }
        return true;
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
//        Toast.makeText(this, "res Codes  :"+resultCode+" sucsses   :"+ConnectionResult.SUCCESS, Toast.LENGTH_SHORT).show();
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 2404).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
     @Override
    protected void onResume() {
         if ( checkPlayServices()){
             SharedPreferences shp= PreferenceManager.getDefaultSharedPreferences(this);
             boolean sent=shp.getBoolean(SENT_TOKEN_TO_SERVER,false);
             if (!sent){
                 Intent intent =new Intent(this, RegisterationIntentService.class);
                 startService(intent);
             }
         }

         if(adView!=null)
             adView.resume();
        super.onResume();


    }

    @Override
    protected void onPause() {
        if(adView!=null)
            adView.pause();
        super.onPause();
    }

    @Override
        protected void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            if(findViewById(R.id.details_card_view_container)!=null){
                mTwoPane=true;
            }
            collapsingToolbarLayout=(CollapsingToolbarLayout) findViewById(R.id.main_layout_collapsing_app_bar);
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.app_name));
            collapsingToolbarLayout.setCollapsedTitleTypeface(Typeface.createFromAsset(getAssets(),  "fonts/Courgette-Regular.ttf"));
           ConnectivityManager cm= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || !ni.isConnected()) {
            Log.w(TAG, getString(R.string.not_connnected_network));
            Utility.setNetworkState(this,QuoteAppSyncAdapter.NO_NETWORK);

        }

        QuoteAppSyncAdapter.initializeSyncAdapter(this);
        Toolbar toolbar=(Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        if ( checkPlayServices()){
            SharedPreferences shp= PreferenceManager.getDefaultSharedPreferences(this);
            boolean sent=shp.getBoolean(SENT_TOKEN_TO_SERVER,false);
            if (!sent){
                Intent intent =new Intent(this, RegisterationIntentService.class);
                startService(intent);
            }
        }
        adView= (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mainListfragment=new MainListfragment();
        mainListfragment.setListClickListener(this);
        if(null!=savedInstanceState){
            if (savedInstanceState.containsKey(getResources().getString(R.string.position))){
                Bundle bundle=new Bundle();
                bundle.putInt(getResources().getString(R.string.position),savedInstanceState.getInt(getResources().getString(R.string.position)));
                mainListfragment.setArguments(bundle);
            }
        }
        getSupportFragmentManager().beginTransaction().add(R.id.list_frame_layout,mainListfragment,null).commit();
    }

    private void openFragment(Fragment fragment, Bundle bundle) {
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.details_card_view_container,fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_about_me){
            Toast.makeText(this, getResources().getString(R.string.about_me_lable), Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(this,AboutMe.class);
            startActivity(intent);
            return  true;
        }else if (item.getItemId()==R.id.action_settings){
            Toast.makeText(this, R.string.settings_lable, Toast.LENGTH_SHORT).show();
            if(mTwoPane){
                getFragmentManager().beginTransaction().replace(R.id.details_card_view_container,new SettingsFragment()).commit();
            }else{
                Intent intent= new Intent(this,SettingsActivity.class);
                startActivity(intent);
            }

            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(getResources().getString(R.string.position),mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(Quote quote, int position, ImageView iv) {
        mPosition=position;
        Bundle bundle=new Bundle();
        bundle.putParcelable(DetailsFragment.QUOTE,quote);
        if(mTwoPane){
            DetailsFragment detailsFragment=new DetailsFragment();
            openFragment(detailsFragment,bundle);
        }else{
            Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
            intent.putExtras(bundle);
            Bundle options=ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,(View) iv,getResources().getString(R.string.icon_transition_name)).toBundle();
            startActivity(intent,options);
        }
    }
}
