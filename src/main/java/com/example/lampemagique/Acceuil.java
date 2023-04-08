package com.example.lampemagique;

import static com.example.lampemagique.Color_change.EXTRA_PALETTE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Button;
import android.os.Bundle;

import java.util.Random;


public class Acceuil extends AppCompatActivity implements View.OnClickListener {
    Switch night_mode_switch;
    public static final String EXTRA_COLOR = "com.example.lampemagique.COLOR_COLOR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);

        night_mode_switch = (Switch) findViewById(R.id.theme_switch);
        Button button_red = (Button) findViewById(R.id.button_red);
        Button button_green = (Button) findViewById(R.id.button_green);
        Button button_blue = (Button) findViewById(R.id.button_blue);
        Button button_palette = (Button) findViewById(R.id.button_palette);

        button_red.setOnClickListener(this);
        button_green.setOnClickListener(this);
        button_blue.setOnClickListener(this);
        button_palette.setOnClickListener(this);

        if (getResources().getConfiguration().isNightModeActive()){
            night_mode_switch.setChecked(true);
        }

        night_mode_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean switch_on) {
                if(switch_on){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }


    public void onClick(View v) {

        int id = v.getId();

        int color = 0; // 0 is red, 1 is green, 2 is blue
        if (id == R.id.button_palette){
            Random r = new Random();
            Intent intent = new Intent(this, Palette.class);
            intent.putExtra(EXTRA_PALETTE, 0xFF000000 + r.nextInt(0xFFFFFF));
            startActivity(intent);
        }else {

            if (id == R.id.button_green) {
                color = Color.GREEN;
            } else if (id == R.id.button_blue) {
                color = Color.BLUE;
            } else if (id == R.id.button_red) {
                color = Color.RED;
            }

            Intent intent = new Intent(this, Color_change.class);
            intent.putExtra(EXTRA_COLOR, color);
            startActivity(intent);
        }
    }

}