package com.example.eyad.ministryofinteriorproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.eyad.ministryofinteriorproject.Activities.ActivityOne;
import com.example.osama.ministryofinteriorproject.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddShipActivity extends AppCompatActivity {

    TextView jarItem,lanshItem,haskaItem,maltashItem,snarItem,bolsItem,
            zydhItem,hbaryItem,glmbatItem,mnwfelItem,salfeh,shrimp,help,light,tourism,tanseq;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Monadi.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_add_ship);

        jarItem = findViewById(R.id.jarItem);
        lanshItem = findViewById(R.id.lanshItem);
        haskaItem = findViewById(R.id.haskaItem);
        maltashItem = findViewById(R.id.maltashItem);
        snarItem = findViewById(R.id.snarItem);
        bolsItem = findViewById(R.id.bolsItem);
        zydhItem = findViewById(R.id.zydhItem);
        hbaryItem = findViewById(R.id.hbaryItem);
        glmbatItem = findViewById(R.id.glmbatItem);
        mnwfelItem = findViewById(R.id.mnwfelItem);
        salfeh = findViewById(R.id.salfeh);
        shrimp = findViewById(R.id.shrimp);
        help = findViewById(R.id.help);
        light = findViewById(R.id.light);
        tourism = findViewById(R.id.tourism);
        tanseq = findViewById(R.id.tanseq);

        getSupportActionBar().setTitle("منظمة شؤون الصيادين - إضافة مركبة");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridLayout mainGrid;

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        //Set Event
        setSingleEvent(mainGrid);


    }


    private void setSingleEvent(GridLayout mainGrid){
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (finalI){

                        case 0 :
                            Intent intent0 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent0.putExtra("shipType",jarItem.getText());
                            startActivity(intent0);
                            break;
                        case 1 :
                            Intent intent1 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent1.putExtra("shipType",lanshItem.getText());
                            startActivity(intent1);
                            //startActivity(new Intent(getApplicationContext(),ActivityTwo.class));
                            break;
                        case 2 :
                            Intent intent2 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent2.putExtra("shipType",haskaItem.getText());
                            startActivity(intent2);
                            //startActivity(new Intent(getApplicationContext(),ActivityThree.class));
                            break;
                        case 3 :
                            Intent intent3 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent3.putExtra("shipType",maltashItem.getText());
                            startActivity(intent3);
                            //startActivity(new Intent(getApplicationContext(),ActivityFour.class));
                            break;
                        case 4 :
                            Intent intent4 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent4.putExtra("shipType",snarItem.getText());
                            startActivity(intent4);
                            //startActivity(new Intent(getApplicationContext(),ActivityFive.class));
                            break;
                        case 5 :
                            Intent intent5 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent5.putExtra("shipType",bolsItem.getText());
                            startActivity(intent5);
                            //startActivity(new Intent(getApplicationContext(),ActivitySix.class));
                            break;
                        case 6 :
                            Intent intent6 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent6.putExtra("shipType",zydhItem.getText());
                            startActivity(intent6);
                            //startActivity(new Intent(getApplicationContext(),ActivitySeven.class));
                            break;
                        case 7 :
                            Intent intent7 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent7.putExtra("shipType",hbaryItem.getText());
                            startActivity(intent7);
                            //startActivity(new Intent(getApplicationContext(),ActivityEight.class));
                            break;
                        case 8 :
                            Intent intent8 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent8.putExtra("shipType",glmbatItem.getText());
                            startActivity(intent8);
                            //startActivity(new Intent(getApplicationContext(),ActivityNine.class));
                            break;
                        case 9 :
                            Intent intent9 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent9.putExtra("shipType",mnwfelItem.getText());
                            startActivity(intent9);
                            //startActivity(new Intent(getApplicationContext(),ActivityTen.class));
                            break;
                        case 10 :
                            Intent intent10 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent10.putExtra("shipType",salfeh.getText());
                            startActivity(intent10);
                            //startActivity(new Intent(getApplicationContext(),ActivityEleven.class));
                            break;
                        case 11 :
                            Intent intent11 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent11.putExtra("shipType",shrimp.getText());
                            startActivity(intent11);
                            //startActivity(new Intent(getApplicationContext(),ActivityTwelve.class));
                            break;
                        case 12 :
                            Intent intent12 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent12.putExtra("shipType",help.getText());
                            startActivity(intent12);
                            //startActivity(new Intent(getApplicationContext(),ActivityThirteen.class));
                            break;
                        case 13 :
                            Intent intent13 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent13.putExtra("shipType",light.getText());
                            startActivity(intent13);
                            //startActivity(new Intent(getApplicationContext(),ActivityFourteen.class));
                            break;
                        case 14 :
                            Intent intent14 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent14.putExtra("shipType",tourism.getText());
                            startActivity(intent14);
                            //startActivity(new Intent(getApplicationContext(),ActivityFifteen.class));
                            break;
                        case 15 :
                            Intent intent15 = new Intent(getApplicationContext(),ActivityOne.class);
                            intent15.putExtra("shipType",tanseq.getText());
                            startActivity(intent15);
                            //startActivity(new Intent(getApplicationContext(),ActivitySixteen.class));
                            break;
                    }


                }
            });
        }
    }

}
