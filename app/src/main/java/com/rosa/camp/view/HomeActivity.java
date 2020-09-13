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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TableLayout;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.rosa.camp.R;
import com.rosa.camp.ui.adapter.ViewPagerAdapter;

import java.util.List;


public class HomeActivity extends AppCompatActivity  {

    Toolbar mtoolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    MapFragment instance;
    DirectionFragment dInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mtoolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout= findViewById(R.id.tab_layout);
        viewPager= findViewById(R.id.view_pager);
        instance=MapFragment.newInstance();
        dInstance=DirectionFragment.newInstance();
        adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new LoginFragment(),"My Camp");
        adapter.addFrag(instance,"Map");
        adapter.addFrag(new HomeFragment(),"Home");
        adapter.addFrag(dInstance,"Home");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.profileicon);
        tabLayout.getTabAt(1).setIcon(R.drawable.mapmarker1);
        tabLayout.getTabAt(2).setIcon(R.drawable.home);

    }



}