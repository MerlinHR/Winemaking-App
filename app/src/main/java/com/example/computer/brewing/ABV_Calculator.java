package com.example.computer.brewing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ABV_Calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abv__calculator);
    }

    Boolean dry = false;

    private static DecimalFormat df2 = new DecimalFormat(".00");

    public void Drybox(View view) {

        EditText etFG = findViewById(R.id.editText4);
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.Dry:

                if (checked) {
                    dry = true;
                    etFG.setEnabled(false);
                }
                else {
                    dry = false;
                    etFG.setEnabled(true);
                }
                break;


        }
    }

    public void Calculate(View view) {
        EditText etOG = findViewById(R.id.editText3);
        EditText etFG = findViewById(R.id.editText4);

        Double OG = Double.parseDouble(etOG.getText().toString());
        Double FG = Double.parseDouble(etFG.getText().toString());

        Double OE = -616.868 + (1111.14 * OG ) - (630.272 * Math.pow(OG,2)) + (135.997 * Math.pow(OG,3));  //original extract

        Double AE = -616.868 + (1111.14 * FG ) - (630.272 * Math.pow(FG,2)) + (135.997 * Math.pow(FG,3));  //apparent extract

        Double q = .22 + (.001 * OE); //attenuation coefficient

        Double RE = ( (q * OE) + AE) / (1 + q);  //real extract

        if (dry){
            RE = 0.0 ;
        }

        Double ABW = (OE - RE) /  ( 2.0665 - (.010665 * OE)  );

        Integer ABVround = (int) Math.round(((ABW * (FG / 0.794)*100)));

        String ABV = df2.format(Double.valueOf(ABVround)/100);

        String Result = ABV.concat("%");

        TextView ABVView = findViewById(R.id.textView5);

        ABVView.setText(Result);
    }
}


