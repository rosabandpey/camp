package com.rosa.camp.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.rosa.camp.R;
import com.rosa.camp.ui.adapter.ViewPagerAdapter;

import java.util.List;


public class HomeActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    TabLayout tabLayout;
    ViewPager viewPager;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        List allFragments = getSupportFragmentManager().getFragments();
        if (allFragments.isEmpty()) {
            return;
        }

        Fragment currentFragment = (Fragment) allFragments.get(allFragments.size() - 1);
        if (currentFragment instanceof PermissionsListener) {
            currentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mtoolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       tabLayout= findViewById(R.id.tab_layout);
       viewPager= findViewById(R.id.view_pager);

        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LoginFragment(),"My Camp");
        adapter.addFrag(new MapFragment(),"Map");
        adapter.addFrag(new HomeFragment(),"Home");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.profileicon);
        tabLayout.getTabAt(1).setIcon(R.drawable.mapmarker1);
        tabLayout.getTabAt(2).setIcon(R.drawable.home);

        statusCheck();


    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}