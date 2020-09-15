package com.rosa.camp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.rosa.camp.R;
import com.rosa.camp.generated.callback.OnClickListener;

public class DirectionActivity extends AppCompatActivity implements View.OnClickListener {

    Button backToMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        backToMap=findViewById(R.id.backToMap);
        backToMap.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.backToMap:

                Intent intent=new Intent(this,HomeActivity.class);
                startActivity(intent);
                break;
        }
        }
}