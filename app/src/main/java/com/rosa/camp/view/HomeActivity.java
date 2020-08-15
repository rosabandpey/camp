package com.rosa.camp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;
import com.rosa.camp.R;
import com.rosa.camp.ui.adapter.ViewPagerAdapter;



public class HomeActivity extends AppCompatActivity {

    Toolbar mtoolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

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
    }



}