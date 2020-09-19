package com.rosa.camp.view;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.rosa.camp.R;
import com.rosa.camp.databinding.CampProfileRegisterBinding;
import com.rosa.camp.viewModel.CampViewModel;
import com.rosa.camp.viewModel.GetProfileViewModel;


public class RegisterCampActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CampProfileRegisterBinding  campProfileRegisterBinding = DataBindingUtil.setContentView(this, R.layout.camp_profile_register);
        campProfileRegisterBinding.setCampProfileViewModel(new CampViewModel());
        campProfileRegisterBinding.executePendingBindings();


    }
}
