package com.example.computer.brewing;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BrewEdit extends AppCompatActivity {

    public BrewEdit() throws FileNotFoundException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_brews_create);


        //=============================================================================================

        final EditText eText=(EditText) findViewById(R.id.Date);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                DatePickerDialog picker = new DatePickerDialog(BrewEdit.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dateInEt = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                eText.setText(dateInEt);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                picker.show();
            }
        });


        populateEditText();


        //=============================================================================================
    }



    String fileLoaded () {
            Bundle extras = getIntent().getExtras();
            if (extras!= null) {
                return extras.getString("recipe");
            } else {
                return "";
            }

        };


    class Recipe {
        String name, date, SG, notes;
        String[] ingredientArray, quantArray;

        private Recipe(String name, String date, String SG, String notes, String[] ingredientArray, String[] quantArray){
            this.name =name;
            this.date =date;
            this.SG =SG;
            this.notes = notes;
            this.ingredientArray = ingredientArray;
            this.quantArray=quantArray;
        }
    }

    Recipe ourRecipe (String file) {

        String name = file.substring(0,file.indexOf("~"));
        file = file.substring(file.indexOf("~")+1);
        String date = file.substring(0,file.indexOf("~"));
        date = date.replace(".", "/");
        file = file.substring(file.indexOf("~")+1);
        String SG = file.substring(0,file.indexOf("~"));
        file = file.substring(file.indexOf("~")+1);
        String notes = file.substring(0,file.indexOf("~"));
        file = file.substring(file.indexOf("~")+1);

        ArrayList<String> recipeIngs = new ArrayList<String>();
        ArrayList<String> recipeQuant = new ArrayList<String>();

        int counter = 0;
        String tempQuant = "";

        while (file.indexOf("~")>0){
            if (counter%3 == 0) {
                recipeIngs.add(file.substring(0,file.indexOf("~")));
                file = file.substring(file.indexOf("~")+1);
            }else if ((counter - 1) % 3 == 0) {
                tempQuant = file.substring(0, file.indexOf("~"));
                file = file.substring(file.indexOf("~")+1);
            } else {
                recipeQuant.add(tempQuant + file.substring(0, file.indexOf("~")));
                file = file.substring(file.indexOf("~")+1);
            }
            counter++;
        }
        recipeQuant.add(tempQuant + file);

        String[] ingredientArray = new String[recipeIngs.size()];
        String[] quantArray = new String[recipeIngs.size()];

        for (int i=0; i<recipeIngs.size();i++){
            ingredientArray[i]= recipeIngs.get(i);
            quantArray[i]= recipeQuant.get(i);

        }


        return new Recipe(name, date, SG, notes, ingredientArray, quantArray){};
    }






    public int numberOfLines = 0;


    public void Add_Line(View view){

        AddLine("", "");
    }



    public void populateEditText () {

        Recipe recipeLoaded = ourRecipe(fileLoaded());
        for (int i=0; i< recipeLoaded.ingredientArray.length; i++){
            AddLine(recipeLoaded.ingredientArray[i], recipeLoaded.quantArray[i]);
        }
        EditText date = findViewById(R.id.Date);
        EditText name = findViewById(R.id.NameEdit);
        EditText SG = findViewById( R.id.SG);
        EditText notes = findViewById(R.id.Notes);

        date.setText(recipeLoaded.date);
        name.setText(recipeLoaded.name);
        SG.setText(recipeLoaded.SG);
        notes.setText(recipeLoaded.notes);

    }


    public void AddLine(String ing, String quan) {

        final String blockCharacterSet = "~/";

        final String allowCharacterSetNum = "1234567890.";

        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };

        InputFilter filterNum = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && allowCharacterSetNum.contains(("" + source))) {
                    return null;
                }
                return "";
            }
        };

        LinearLayout lli = (LinearLayout)findViewById(R.id.linearLayoutIngredients);
        LinearLayout lli4 = new LinearLayout(this);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        LinearLayout.LayoutParams p1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,3);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,5);
        LinearLayout.LayoutParams p8 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 45,16);

        lli4.setLayoutParams(p);
        lli4.setOrientation(LinearLayout.HORIZONTAL);

        EditText et = new EditText(this);
        EditText eq = new EditText(this);
        Spinner sc = new Spinner(this);

        et.setLayoutParams(p8);
        eq.setLayoutParams(p2);
        sc.setLayoutParams(p1);

        et.setText(ing);
        String quanNum = quan.replaceAll("[^0-9.]", "");
        String quanUnit = quan.replaceAll("[0-9.]", "");
        eq.setText(quanNum);

        et.setFilters(new InputFilter[]{filter});
        eq.setFilters(new InputFilter[]{filterNum});


        eq.setGravity(Gravity.CENTER);

        et.setTextColor(Color.rgb(105,105,105));
        eq.setTextColor(Color.rgb(105,105,105));

        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.units, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner

        List<String> units = Arrays.asList(getResources().getStringArray(R.array.units));

        if (units.contains(quanUnit)) {
            int spinnerPosition = adapter3.getPosition(quanUnit);
            sc.setSelection(spinnerPosition);
        }

//set the default according to value


        sc.setAdapter(adapter3);

        Integer currentLine = numberOfLines +1;

        et.setHint("Ingredient #"+currentLine);
        eq.setHint("0");


        lli4.addView(et);
        lli4.addView(eq);
        lli4.addView(sc);

        //lli4.setId(currentLine*4);
        et.setId(numberOfLines*3);
        eq.setId(numberOfLines*3+1);
        sc.setId(numberOfLines*3+2);

        lli.addView(lli4);
        numberOfLines++;
    }


    //-----------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------


    private void writeToFile (Context context, String filename, String fileContents){

        FileOutputStream outputStream; {
            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(fileContents.getBytes());
                outputStream.close();

                CharSequence text = "Recipe Saved.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            } catch (Exception e) {
                e.printStackTrace();
            }}}

    private String readFromFile(Context context, String name) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }



    public void sav (View view) {

        EditText NameEd = findViewById(R.id.NameEdit);
        EditText DateEd = findViewById(R.id.Date);


        String Name = NameEd.getText().toString();
        String Date = DateEd.getText().toString().replace("/",".");


        if (Name.equals("")){
            CharSequence text = "Please Input Name.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        } else if (Date.equals("")){
            CharSequence text = "Please Input Date.";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
            toast.show();
        } else {
            Save();
            Intent brewsIntent = new Intent(this, CurrentBrews.class);
            startActivity(brewsIntent);
        }
    }

    public boolean FileExists(String fname){
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public void Save () {

        EditText NameEd = findViewById(R.id.NameEdit);
        EditText DateEd = findViewById(R.id.Date);
        EditText SGEd = findViewById(R.id.SG);
        EditText NotesEd = findViewById(R.id.Notes);

        String Name = NameEd.getText().toString();
        String Date = DateEd.getText().toString().replace("/",".");
        String SG = SGEd.getText().toString();
        String Notes = NotesEd.getText().toString();


        String NamDatSG = Name + "~" + Date + "~" + SG;

        String Ings = "";

        for (int i = 0; i < (numberOfLines) * 3; i++) {

            Spinner Spn = new Spinner(this);
            EditText Etx = new EditText(this);
            String k;
            if ((i - 2) % 3 == 0) {
                Spn = findViewById(i);
                k = Spn.getSelectedItem().toString();
            } else {
                Etx = findViewById(i);
                k = Etx.getText().toString();
            }
            Ings = Ings + "~" + k;
        }

        String fileContents = NamDatSG + "~" + Notes + Ings;

        int fileCounter = 0;

        while (FileExists("recipe~" + Name + "~" + Date + "~" + fileCounter + ".txt")) {

            fileCounter++;
        }

        String filename = "recipe~" + Name + "~" + Date + "~" + fileCounter + ".txt";


        writeToFile(getApplicationContext(), filename, fileContents);
    }
}
