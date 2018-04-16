package afeka.com.hw1;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class Game extends AppCompatActivity {

    private String name;
    private int age;
    private int points = 0;
    private int gameMode;
    private int index = 0;

    private TextView nameField;
    private TextView ageField;

    private ArrayList<ImageView> list;
    private ArrayList<Integer> imageData;

    int firstCard,secondCard;

    int idFirst, idSecond;

    int cardNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        nameField = (TextView) findViewById(R.id.name_fld);
        ageField = (TextView) findViewById(R.id.age_fld);

        Intent intent = getIntent();
        gameMode = intent.getExtras().getInt("mode");
        name = intent.getStringExtra("name");
        age = intent.getExtras().getInt("age");

        nameField.setText(name);
        ageField.setText(String.valueOf(age));

        LinearLayout gamePanel = (LinearLayout) findViewById(R.id.rows);
        gamePanel.setWeightSum(gameMode);

        list = new ArrayList<>(gameMode * gameMode);
        imageData = new ArrayList<>(gameMode * gameMode);


            for(int i = 0 ; i < gameMode ; i ++)
                {
                    LinearLayout temp = new LinearLayout(this);
                    temp.setWeightSum(gameMode);

                    temp.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    lp.weight = 1;
                    temp.setLayoutParams(lp);

                    for(int j = 0 ; j < gameMode ; j ++){

                        int id = Integer.parseInt(String.valueOf(i)+String.valueOf(j));
                        String imgName = "img_" + id;

                        ImageView img = new ImageView(this);
                        img.setImageResource(R.drawable.img_back);

                        img.setTag(index++);
                        img.setId(id);

                        img.setLayoutParams(lp);
                        temp.addView(img);
                        list.add(img);
                    }

                    gamePanel.addView(temp);


                }




        loadCards();

        Collections.shuffle(imageData);

       for(ImageView img:list){
           img.setOnClickListener(imgListener);
       }

        //   for(int i = 0 ; i < gameMode ; i ++)
         //      for(int j = 0 ; j < gameMode ; j ++){
          //     list.set(idx++,(ImageView) findViewById(getResources().getIdentifier(String.valueOf(i) + String.valueOf(j),"id",getPackageName())));

         //   }



    }

    private View.OnClickListener imgListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            for (int i = 0 ; i < list.size() ; i ++){
                if(v.getId() == list.get(i).getId()){
                    int theCard =  (int)v.getTag();
                    doStuff(list.get(i),theCard);
                }
            }
        }
    };

    private void loadCards(){

        for(int i = 0 ; i < gameMode*gameMode/2; i ++){
                imageData.add(getResources().getIdentifier("img_" + (i+1),"drawable",getPackageName()));

            }
        for(int i = 0 ; i < gameMode*gameMode/2; i ++){
            imageData.add(getResources().getIdentifier("img_" + (i+1),"drawable",getPackageName()));

        }

        if(gameMode % 2 == 1)
            imageData.add((getResources().getIdentifier("img_13","drawable",getPackageName())));



    }

    private void doStuff(ImageView im, int card){

            im.setImageResource(imageData.get(card));


        if(cardNumber == 1) {
            firstCard = imageData.get(card);
            cardNumber = 2;
            idFirst = card;
            im.setEnabled(false);
        } else if(cardNumber == 2){
            secondCard = imageData.get(card);
            cardNumber = 1;
            idSecond = card;

            for(ImageView iv:list)
                iv.setEnabled(false);

            Handler handler = new Handler();



                handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            },1000);



        }


    }
    private void calculate(){
        if(firstCard == secondCard){
            list.get(idFirst).setActivated(true);
            list.get(idSecond).setActivated(true);
            points++;
        }

            for(ImageView iv:list) {
                if (!iv.isActivated()) {
                    iv.setEnabled(true);
                    iv.setImageResource(R.drawable.img_back);
                }
            }



        checkEnd();

    }
    private void checkEnd(){
        boolean finish = false;

       if(points == gameMode*gameMode/2){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Game.this);
            alertDialogBuilder.setMessage("YOU WIN!!!!").setCancelable(false).setPositiveButton("NEW", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialogInterface, int i){
                    Intent intent = new Intent(getApplicationContext(),Game.class);
                    intent.putExtra("name",name);
                    intent.putExtra("age",age);
                    intent.putExtra("mode",gameMode);
                    startActivity(intent);
                    finish();
                }
            }).setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
