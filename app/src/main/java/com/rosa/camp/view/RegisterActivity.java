package com.rosa.camp.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BindingAdapter;
import androidx.databinding.DataBindingUtil;

import com.rosa.camp.R;
import com.rosa.camp.databinding.ActivityLoginBinding;
import com.rosa.camp.databinding.RegisterBinding;
import com.rosa.camp.viewModel.LoginViewModel;
import com.rosa.camp.viewModel.RejisterViewModel;

public class RegisterActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegisterBinding registerBinding = DataBindingUtil.setContentView(this, R.layout.register);
        registerBinding.setRejisterViewModel(new RejisterViewModel());
        registerBinding.executePendingBindings();

    }

    @BindingAdapter("app:onFocusChange")
    public static void onFocusChange(View view, final View.OnFocusChangeListener listener) {
        view.setOnFocusChangeListener(listener);
    }


    @BindingAdapter({"toastMessage"})
    public static void runMe(View view, String message) {
        if (message != null)
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
