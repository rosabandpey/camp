package com.rosa.camp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.rosa.camp.R;


import com.rosa.camp.databinding.ActivityGetprofileBinding;
import com.rosa.camp.viewModel.GetProfileViewModel;


public class GetProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGetprofileBinding getProfileActivity = DataBindingUtil.setContentView(this, R.layout.activity_getprofile);
        getProfileActivity.setGetProfileViewModel(new GetProfileViewModel()  );
        getProfileActivity.executePendingBindings();

    }


}
