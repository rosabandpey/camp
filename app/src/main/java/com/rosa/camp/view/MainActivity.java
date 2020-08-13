package com.rosa.camp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rosa.camp.R;

public class MainActivity extends AppCompatActivity {

    Button googleButton;
    Button loginButton;
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleButton=findViewById(R.id.googleButton);
        loginButton=findViewById(R.id.cirLoginButton);
        regButton=findViewById(R.id.cirRegisterButton);

    }

    public void clickedButton(View view) {

        if (view.getId()==googleButton.getId()){


            Intent intent=new Intent(this,GoogleSignInActivity.class);
            startActivity(intent);

        }
        if (view.getId()==loginButton.getId()){

            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);


        }

        if (view.getId()==regButton.getId()){

            Intent intent=new Intent(this,RegisterActivity.class);
            startActivity(intent);

        }

    }
}
