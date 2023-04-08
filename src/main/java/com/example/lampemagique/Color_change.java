package com.example.lampemagique;

import static com.example.lampemagique.Acceuil.EXTRA_COLOR;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Color_change extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_PALETTE = "com.example.lampemagique.PALETTE_EXTRA";
    protected Button main_button;


    protected int button_red = 255;

    protected int button_blue = 0;
    protected int button_green = 0;
    String server_address = "chadok.info";
    int server_port = 9998;
    String lamp = "05";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            button_red = savedInstanceState.getInt("red", 0);
            button_green = savedInstanceState.getInt("green", 0);
            button_blue = savedInstanceState.getInt("blue", 0);
        } else {
            int base_color = getIntent().getIntExtra(EXTRA_COLOR, 0);

            button_red = Color.red(base_color);
            button_green = Color.green(base_color);
            button_blue = Color.blue(base_color);

        }
        setContentView(R.layout.color_change);

        main_button = findViewById(R.id.main_button);

        Button button_more_red = (Button) findViewById(R.id.button_more_red);
        Button button_more_green = (Button) findViewById(R.id.button_more_green);
        Button button_more_blue = (Button) findViewById(R.id.button_more_blue);

        Button button_less_red = (Button) findViewById(R.id.button_less_red);
        Button button_less_green = (Button) findViewById(R.id.button_less_green);
        Button button_less_blue = (Button) findViewById(R.id.button_less_blue);

        Button button_palette = (Button) findViewById(R.id.button_palette);

        button_more_red.setOnClickListener(this);
        button_more_green.setOnClickListener(this);
        button_more_blue.setOnClickListener(this);
        button_less_red.setOnClickListener(this);
        button_less_green.setOnClickListener(this);
        button_less_blue.setOnClickListener(this);

        button_palette.setOnClickListener(this);

        Button button_return_menu = (Button) findViewById(R.id.button_return_menu);

        button_return_menu.setOnClickListener(this);

        main_button.setOnClickListener(this);

        set_main_color();

    }
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.button_more_red) {
            button_red = Math.min(button_red + 16, 255);
        } else if (id == R.id.button_more_green) {
            button_green = Math.min(button_green + 16, 255);
        } else if (id == R.id.button_more_blue) {
            button_blue = Math.min(button_blue + 16, 255);
        } else if (id == R.id.button_less_red) {
            button_red = Math.max(button_red - 16, 0);
        } else if (id == R.id.button_less_green) {
            button_green = Math.max(button_green - 16, 0);
        } else if (id == R.id.button_less_blue) {
            button_blue = Math.max(button_blue - 16, 0);
        }else if(id == R.id.main_button){
            start_rainbow_light();
        } else if (id == R.id.button_palette) {
            Intent intent = new Intent(this, Palette.class);
            intent.putExtra(EXTRA_PALETTE, Color.rgb(button_red, button_green, button_blue));
            startActivity(intent);
        } else if (id == R.id.button_return_menu) {
            Intent intent = new Intent(this, Acceuil.class);
            startActivity(intent);
        }


        set_main_color();



    }

    private void start_rainbow_light(){
        new Thread(new Runnable() {
            final int initial_color = Color.rgb(button_red, button_green, button_blue);
            @Override
            public void run() {
                //Added orange and indigo hex values, this starts and ends on initial color so that the end of the animation is seemless :)) (could probably make that into the loop but i'm too lazy for that, you'll get the hard-coded version)
                final int[] rainbow_colors = {initial_color, Color.RED,Color.parseColor("#FFA500"), Color.YELLOW, Color.GREEN, Color.BLUE,Color.parseColor("#4B0082"), Color.MAGENTA, initial_color};


                final int color_steps = 75; //number of shades between each color in the rainbow sequence, 75 is just because its smoother with 2sec, this should probably be dependant on either the duration of the animation or the number of colors if i make this user customizable

                for (int i = 0; i < rainbow_colors.length; i++) {
                    int start_color = rainbow_colors[i];
                    int end_color = (i == rainbow_colors.length - 1) ? rainbow_colors[0] : rainbow_colors[i + 1];
                    for (int j = 0; j <= color_steps; j++) {
                        float ratio = (float) j / (float) color_steps; //ratio between the two colors
                        button_red = (int) (Color.red(start_color) * (1 - ratio) + Color.red(end_color) * ratio);
                        button_green = (int) (Color.green(start_color) * (1 - ratio) + Color.green(end_color) * ratio);
                        button_blue = (int) (Color.blue(start_color) * (1 - ratio) + Color.blue(end_color) * ratio);


                        //I initially didn't use the function there and set variables for the current color in the gradient but the socket stuff made it a whole lot more complicated :(
                        //I'm not very happy about this solution because i'd rather not use globals but it's the best i've found
                        set_main_color();


                        //wait a bit to add up to 2000ms
                        try {
                            Thread.sleep(2000/color_steps); //the steps will always add up to 2 second between each color (to follow Ms. Keller's directions :) but imo it's a bit too long)
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Reset the color to the initial color
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button_red = Color.red(initial_color);
                        button_green = Color.green(initial_color);
                        button_blue = Color.blue(initial_color);
                        set_main_color();
                    }
                });
            }
        }).start();
    }

    public void set_main_color(){


        if (true){ //TODO make this so that i have a switch on wether or not i want it linked to the server



            new Thread(new Runnable() {
                @Override
                public void run() {


                    try {
                        Socket socket = new Socket(server_address, server_port);
                        String message = String.format( "%s%2x%2x%2x",lamp, button_red, button_green, button_blue ); //thanks to killian for this line, i didn't know you could format like this, got me to check the format string doc was interesting https://docs.oracle.com/javase/6/docs/api/java/util/Formatter.html#syntax

                        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                        printWriter.println(message);
                        Log.d("send", message);
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String response = bufferedReader.readLine();
                        Log.d("res", response);
                        bufferedReader.close();
                        printWriter.close();
                        socket.close();

                    } catch (Exception e) {

                    }
                }
            }).start();
           }

        main_button.setBackgroundColor(Color.rgb(button_red, button_green, button_blue)); //button color
        main_button.setText(String.format("(ROUGE: %d, VERT: %d, BLEU: %d)", button_red, button_green, button_blue)); //button text

        //true = luminance is lighter, false = luminance is darker
        if ((button_blue + button_red + button_green) > 382){//382 is the half of 255*3, it's more optimised than using a division to get the average and then comparing to 127 (mental illness)
            main_button.setTextColor(Color.BLACK);
        }else{
            main_button.setTextColor(Color.WHITE);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("red", button_red);
        outState.putInt("green", button_green);
        outState.putInt("blue", button_blue);
    }
}




