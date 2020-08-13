package com.rosa.camp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.rosa.camp.R;
import com.rosa.camp.databinding.ActivityUpdateProfileBinding;
import com.rosa.camp.databinding.RegisterBinding;
import com.rosa.camp.viewModel.RejisterViewModel;
import com.rosa.camp.viewModel.UpdateProfileViewModel;

public class UpdateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUpdateProfileBinding activityUpdateProfileBinding=DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        activityUpdateProfileBinding.setUpdateProfileViewModel(new UpdateProfileViewModel());
        activityUpdateProfileBinding.executePendingBindings();


    }
}