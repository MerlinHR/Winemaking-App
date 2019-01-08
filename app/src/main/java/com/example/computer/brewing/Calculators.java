package com.example.computer.brewing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Calculators extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculators);
    }
    public void ABV_Calculator(View view) {
        Intent ABVIntent = new Intent(this, ABV_Calculator.class);
        startActivity(ABVIntent);
    }
    public void sugarContent_Calculator(View view) {
        Intent SugarIntent = new Intent(this, sugarContent_Calculator.class);
        startActivity(SugarIntent);
    }
    public void Sugar_Addition_Calculator(View view) {
        Intent Sugar2Intent = new Intent(this, Sugar_Addition_Calculator.class);
        startActivity(Sugar2Intent);
    }
}
