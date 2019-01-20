package com.example.computer.brewing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Sugar_Addition_Calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar__addition__calculator);
    }
    public void Calculate(View view) {

        EditText etOG = findViewById(R.id.editText);
        EditText etRG = findViewById(R.id.editText5);
        EditText etVol = findViewById(R.id.editText2);

        Double OG = Double.parseDouble(etOG.getText().toString());
        Double RG = Double.parseDouble(etRG.getText().toString());
        Double Vol = Double.parseDouble(etVol.getText().toString());


        Double OE = -616.868 + (1111.14 * OG ) - (630.272 * Math.pow(OG,2)) + (135.997 * Math.pow(OG,3));  //original extract

        Double RE = -616.868 + (1111.14 * RG ) - (630.272 * Math.pow(RG,2)) + (135.997 * Math.pow(RG,3));  //required extract

        Double Mass = OG*Vol*1000;

        Double SR = ((RE/100)*Mass-(OE/100)*Mass)/(1-RE/100); //Sugar Required

        TextView SRView = findViewById(R.id.textView10);

        Integer SRint = (int)Math.round(SR);

        String Result = SRint.toString().concat("g");

        SRView.setText(Result);

        Double FV = (Mass+SR)/RG;

        Double FVl = Double.valueOf((int)Math.round(FV/10))/100;

        TextView FinVol = findViewById(R.id.textView12);

        FinVol.setText(FVl.toString().concat("l"));
    }
}
