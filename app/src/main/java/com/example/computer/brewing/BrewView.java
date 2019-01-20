package com.example.computer.brewing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BrewView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brew_view);




        fileName=fileName();
        Display();

    }

//================================================================================================================
    public int numberOfLines = 0;

    String fileName = "";

    String fileName () {
        Bundle extras = getIntent().getExtras();
        if (extras!= null) {
            return extras.getString("RecipeViewed");
        } else {
            return "";
        }

    };




    public void Display () {
        TextView NamView = findViewById(R.id.BrewNameinView);
        TextView SGView = findViewById(R.id.SGinView);
        TextView DatView = findViewById(R.id.DateinView);
        TextView NoteView = findViewById(R.id.NotesView);

        String file = readFromFile(this, fileName);
        Recipe ourRecipe = ourRecipe(file);

        NamView.setText(ourRecipe.name);
        SGView.setText(ourRecipe.SG);
        DatView.setText(ourRecipe.date);
        NoteView.setText(ourRecipe.notes);


        for (int i=0; i< ourRecipe.ingredientArray.length; i++){
            AddLine(ourRecipe.ingredientArray[i], ourRecipe.quantArray[i]);
        }

    }

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



    public void AddLine(String ing, String quan) {

        LinearLayout lli = (LinearLayout)findViewById(R.id.linearLayoutIngredients);
        LinearLayout lli4 = new LinearLayout(this);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        LinearLayout.LayoutParams p2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,8);
        LinearLayout.LayoutParams p8 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,8);

        lli4.setLayoutParams(p);
        lli4.setOrientation(LinearLayout.HORIZONTAL);

        TextView et = new TextView(this);
        TextView eq = new TextView(this);

        et.setLayoutParams(p8);
        eq.setLayoutParams(p2);

        et.setPadding(0,0,0,8);
        eq.setPadding(0,0,0,8);

        et.setTextSize(16);
        eq.setTextSize(16);


        eq.setGravity(Gravity.CENTER);
        et.setGravity(Gravity.CENTER);


        et.setTextColor(Color.rgb(0,0,0));
        eq.setTextColor(Color.rgb(0,0,0));



        et.setText(ing);
        eq.setText(quan);


        lli4.addView(et);
        lli4.addView(eq);

        lli4.setPadding(0,0,0,0);


        lli.addView(lli4);
        numberOfLines++;
    }
//================================================================================================================









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


    public void edit (View view) {
        Intent brewEditIntent = new Intent (this, BrewEdit.class);
        String file = readFromFile(this, fileName);
        brewEditIntent.putExtra("recipe", file);
        startActivity(brewEditIntent);
    }

    public void delete (View view) {
        getApplicationContext().deleteFile(fileName);
    }




}
