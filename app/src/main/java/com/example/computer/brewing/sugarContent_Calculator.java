package com.example.computer.brewing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class sugarContent_Calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugar_content__calculator);

    }

    private static DecimalFormat df3 = new DecimalFormat(".000");

    Boolean topsg = false;

    public void Swap(View view) {

        EditText top = findViewById(R.id.editText6);
        TextView bottom = findViewById(R.id.textView4);
        TextView topview = findViewById(R.id.textView3);
        TextView bottomview = findViewById(R.id.textView2);
        String placeholder;
        String numericplaceholder;

        if (topsg) {
            topsg = false;
        } else {
            topsg = true;
        }

        String topnum = top.getText().toString();
        String botnum = bottom.getText().toString();

        String tview = topview.getText().toString();
        String bview = bottomview.getText().toString();

        numericplaceholder = topnum;
        topnum = botnum;
        botnum = numericplaceholder;

        placeholder = tview;
        tview = bview;
        bview = placeholder;

        topview.setText(tview);
        bottomview.setText(bview);

        top.setText(topnum);
        bottom.setText(botnum);

    }


        public void Calculate(View view) {
        EditText input = findViewById(R.id.editText6);
        TextView output = findViewById(R.id.textView4);
        if (topsg) {
            Double SG = Double.parseDouble(input.getText().toString());
            Double SC = (-616.868 + (1111.14 * SG) - (630.272 * Math.pow(SG, 2)) + (135.997 * Math.pow(SG, 3)))*10;  //sugar concentration
            Integer SC1 = (int)Math.round(SC);
            output.setText(SC1.toString());
        } else {
            Double SC = Double.parseDouble(input.getText().toString());
            Double SG = 1+((SC/10)/(258-227.1*((SC/10)/258.2)));
            Integer grav = (int)Math.round(SG*1000);
            String SG1 = df3.format(Double.valueOf(grav)/1000);
            output.setText(SG1);

        }

    }


}
