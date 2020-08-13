package com.rosa.camp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;
import com.rosa.camp.databinding.ActivityLoginBinding;
import com.rosa.camp.R;
import com.rosa.camp.viewModel.LoginViewModel;

public class LoginActivity  extends AppCompatActivity {

   // private LoginViewModel loginViewModel;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding  activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        //activityLoginBinding.setLoginViewModel(new LoginViewModel());
        activityLoginBinding.executePendingBindings();
    }

    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }




}







