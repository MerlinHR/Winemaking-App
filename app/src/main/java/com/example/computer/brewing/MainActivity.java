package com.example.computer.brewing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void calculators(View view) {
        Intent calcIntent = new Intent(this, Calculators.class);
        startActivity(calcIntent);
    }

    public void currentBrews(View view) {
        Intent brewsIntent = new Intent(this, CurrentBrews.class);
        startActivity(brewsIntent);
    }



}
