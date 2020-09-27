package com.rosa.camp.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.rosa.camp.R;
import com.rosa.camp.databinding.CampProfileRegisterBinding;
import com.rosa.camp.viewModel.CampViewModel;
import com.rosa.camp.viewModel.GetProfileViewModel;


public class RegisterCampActivity extends AppCompatActivity {

    private CampViewModel viewModel;
    private static final int PICK_IMAGE_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CampProfileRegisterBinding  campProfileRegisterBinding = DataBindingUtil.setContentView(this, R.layout.camp_profile_register);
        viewModel=new CampViewModel();
        campProfileRegisterBinding.setCampProfileViewModel(viewModel);
        campProfileRegisterBinding.executePendingBindings();


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
// get data via the key
        Double latitude = extras.getDouble("latitude",0);
        Double longtitude = extras.getDouble("Longtitude",0);

        if (latitude != null) {
            // do something with the data
            Log.d("latitude",latitude.toString());
        }
        if (longtitude != null) {
            // do something with the data
            Log.d("Longtitude",longtitude.toString());
        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        viewModel.onActivityResult(requestCode, resultCode, data);
    }

}
