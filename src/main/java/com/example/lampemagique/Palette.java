package com.example.lampemagique;

import static com.example.lampemagique.Acceuil.EXTRA_COLOR;
import static com.example.lampemagique.Color_change.EXTRA_PALETTE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class Palette extends AppCompatActivity implements View.OnClickListener{
    protected int[] color_palette;
    protected Button[] buttons;

    Button button_reset_palette;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette);
        if (savedInstanceState != null) {
            color_palette= savedInstanceState.getIntArray("palette");
        } else {
            int base_color = getIntent().getIntExtra(EXTRA_PALETTE, 0);
            color_palette = generateColorPalette(base_color);
            TextView title = (TextView) findViewById(R.id.palette_title);
            title.setTextColor(base_color);
        }

        button_reset_palette = (Button) findViewById(R.id.button_reset_palette);
        button_reset_palette.setOnClickListener(this);

        Button button_return_menu = (Button) findViewById(R.id.button_return_menu);
        button_return_menu.setOnClickListener(this);

        buttons = new Button[]{findViewById(R.id.button_palette_main), findViewById(R.id.button_palette2), findViewById(R.id.button_palette3), findViewById(R.id.button_palette4), findViewById(R.id.button_palette5), findViewById(R.id.button_palette6)};


        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackgroundColor(color_palette[i]);
            buttons[i].setText("#"+Integer.toHexString(color_palette[i]).substring(2));
            if ((Color.red(color_palette[i]) + Color.blue(color_palette[i]) + Color.green(color_palette[i])) > 382){
                buttons[i].setTextColor(Color.rgb(Math.max(Color.red(color_palette[i])- 50, 0 ), Math.max(Color.green(color_palette[i])- 50, 0 ), Math.max(Color.blue(color_palette[i])- 50, 0 )));
            }else{
                buttons[i].setTextColor(Color.rgb(Math.min(Color.red(color_palette[i])+ 50, 255 ), Math.min(Color.green(color_palette[i])+ 50, 255 ), Math.min(Color.blue(color_palette[i])+ 50, 255 )));
            }
        }



    }





    public static int[] generateColorPalette(int base_color) { //takes a color as an argument, and give back the worst imaginable color palette as a result! (actually it's using a complementary color so it should be ok)
        Random r = new Random();

        int complement_color = Color.rgb(255 - Color.red(base_color), 255 - Color.green(base_color), 255 - Color.blue(base_color)); // Generate complement color
        int bright_color = Color.rgb(Math.min(Color.red(base_color)+200,255), Math.min(Color.green(base_color)+200,255), Math.min(Color.blue(base_color)+200,255)); //brigthens the base color
        int bright_color_comp = Color.rgb(Math.min(Color.red(complement_color)+190,255), Math.min(Color.green(complement_color)+190,255), Math.min(Color.blue(complement_color)+190,255)); //same for comp
        int dark_color = Color.rgb(Math.max(Color.red(base_color)-180,0), Math.max(Color.green(base_color)-180,0), Math.max(Color.blue(base_color)-180,0)); // darkens (like for text or smth)
        int random_color = Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255));


        int[] colorPalette = {base_color, complement_color, bright_color, bright_color_comp, dark_color, random_color};

        return colorPalette;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.button_reset_palette){
            Random r = new Random();
            finish();
            Intent intent = new Intent(this, Palette.class);
            intent.putExtra(EXTRA_PALETTE,  Color.rgb(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            startActivity(intent);
        }else if (id == R.id.button_return_menu) {
            Intent intent = new Intent(this, Acceuil.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("palette", color_palette);

    }
}
