package com.example.tomek.warcaby;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private RadioGroup radioGroup;
    private Button btnStart;
    private Button twoRows;
    private Button threeRows;
    private int howManyRows=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        radioGroup = (RadioGroup) findViewById(R.id.choose_rows);

        btnStart = (Button)findViewById(R.id.start_button);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //int selectedId = radioGroup.getCheckedRadioButtonId();

                DraughtsLogic newDraught = new DraughtsLogic();
                //Intent myIntent = new Intent(v.getContext(), DraughtsLogic.class);
                //startActivityForResult(myIntent, 0);
                Intent i = new Intent(MainActivity.this, DraughtsLogic.class);
                i.putExtra("key", howManyRows);
                startActivity(i);
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.two_rows:
                if (checked)
                    howManyRows=2;
                break;
            case R.id.three_rows:
                if (checked)
                    howManyRows=3;
                break;
        }
    }

}