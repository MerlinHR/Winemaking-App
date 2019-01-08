package com.example.computer.brewing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentBrews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_brews);


        File recipeStorage = getFilesDir();

        String[] values = recipeStorage.list();

        class Recipe {
            String name, date, fileName;

            private Recipe(String name, String date, String fileName){
                this.name =name;
                this.date =date;
                this.fileName = fileName;
            }
        }

        String[] valuesName = new String[values.length];
        String[] valuesDate = new String[values.length];

        final ArrayList<Recipe> RecipeList = new ArrayList<Recipe>();

        //Recipe[] Recipes = new Recipe[values.length];


        for (int l = 0 ; l< values.length; l++) {
            if (values[l].substring(0, values[l].indexOf('~')).equals("recipe")) {
                valuesName[l] = values[l].substring(7,values[l].indexOf("~",7));
                valuesDate[l] = values[l].substring(values[l].indexOf("~", 7)+1, Math.max(values[l].indexOf("~",8+valuesName[l].length()),values[l].indexOf("~", 7)+1));
                Recipe k = new Recipe(valuesName[l],valuesDate[l],values[l]);
                RecipeList.add(k);
            } else {
                valuesName[l] = "";
                valuesDate[l] = "";
                //RecipeList.set(l,null);
                //Recipes[l] = null;
                //Recipes[l].name=valuesName[l];
                //Recipes[l].date=valuesDate[l];
            }
        }





        final ListView listview = (ListView) findViewById(R.id.listView);


        /*final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, DateList);*/
//-----------------------------------------------------------------------------------------------------------------------------------------
        final String TEXT1 = "text1";
        final String TEXT2 = "text2";

        List<Map<String,String>> RecipeMaps = new ArrayList<Map<String,String>>();

        for (Recipe i: RecipeList){
            Map<String,String> map = new HashMap<String,String>();
            map.put(TEXT1, i.name);
            map.put(TEXT2, i.date.replace(".", "/"));
            RecipeMaps.add(Collections.unmodifiableMap(map));
        }

        String[] fromMapKey = new String[] {TEXT1, TEXT2};
        int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};

        SimpleAdapter adapter = new SimpleAdapter(this, RecipeMaps, android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);
//----------------------------------------------------------------------------------------------------------------------------------------

        listview.setAdapter(adapter);




        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

               item = RecipeList.get(position).fileName;
                BrewView(view);


            }

        });

        //-----------------------------------------------------------------------------------------------
    }

    String item = "";

    /*private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }*/



    public void brewsEdit (View view) {
        Intent brewEditIntent = new Intent (this, BrewsCreate.class);
        startActivity(brewEditIntent);
    }

    public void BrewView (View view) {
        Intent brewViewIntent = new Intent (this, BrewView.class);
        brewViewIntent.putExtra("RecipeViewed", item);
        startActivity(brewViewIntent);
    }


    public boolean FileExists(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }














}
