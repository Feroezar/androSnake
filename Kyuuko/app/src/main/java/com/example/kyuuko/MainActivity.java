package com.example.kyuuko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.*;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    //list of snake points / snake length
    private final List<SnakePoints> snakePointsList = new ArrayList<>();

    private SurfaceView surfaceView;
    private TextView scoreTv;
    //surface holder to draw snake on surface canvas
    private SurfaceHolder surfaceHolder;
    //snake moving position,value must be right,left,top,bottom.
    //jalan utamanya arah kanan
    private String movingPosition = "right";
    private  int score = 0;

    //bisa ganti valuenya agar ular bisa jadi panjang
    private static final int pointSize = 28;

    //awal snake tale
    private static final int defaultTalePoints = 3;

    private static final int snakeColor = Color.YELLOW;

    //kecepatan ular. value harus diantara 1 - 1000
    private static final int snakeMovingSpeed = 800;

    //random point posisi cordinat on surface view
    private int positionX, positionY;

    //timer to move snake
    private Timer timer;

    //canvass to draw snake
    private Canvas canvas = null;

    //point color/single point color
    private Paint pointColor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting surfaceview and score textview from xml file
        surfaceView = findViewById(R.id.surfaceView);
        scoreTv = findViewById(R.id.scoreTV);

        //getting imagebuttons from xml file
        final AppCompatImageButton topBtn = findViewById(R.id.topBtn);
        final AppCompatImageButton leftBtn = findViewById(R.id.leftBtn);
        final AppCompatImageButton rightBtn = findViewById(R.id.rightBtn);
        final AppCompatImageButton bottomBtn = findViewById(R.id.bottomBtn);

        //adding callback to surfaceview
        surfaceView.getHolder().addCallback(this);
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //buat check agar ular dari atas tidak langsung kebawah jadi
                //harus kanan atau kiri
                if(movingPosition.equals("bottom")){
                    movingPosition ="right";
                }
            }
        });
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movingPosition.equals("right")){
                    movingPosition = "buttom";
                }
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movingPosition.equals("left")){
                    movingPosition = "top";
                }
            }
        });
        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movingPosition.equals("top")){
                    movingPosition = "left";
                }
            }
        });
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        //when surface is created then get surfaceholder from it and assign to surfaceholder
        this.surfaceHolder = surfaceHolder;

        //init data for snake
        init();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void init(){
        //clear snake points / snake length
        snakePointsList.clear();
        //set awal score jadi 0
        scoreTv.setText("0");
        score = 0;
        movingPosition = "right";

        //awalan snake posisi mulai
        int starPositionX = (pointSize) * defaultTalePoints;
        //make snake panjang awal / score
        for(int i = 0; i < defaultTalePoints; i++){

            //addign point to snake's tale
            SnakePoints snakePoints = new SnakePoints(starPositionX, pointSize);
            snakePointsList.add(snakePoints);

            //incresing value for next point as snake tale
            starPositionX = starPositionX - (pointSize * 2);
        }
        //add random point on the screen to be eaten by the snake
        addPoint();

        // start moving snake
        moveSnake();
    }
    private void addPoint(){
        //getting surfaceview width and height
        int surfaceWidth = surfaceView.getWidth() - (pointSize * 2);
        int surfaceHeight = surfaceView.getHeight() - (pointSize * 2);

        int randomXPosition = new Random().nextInt(surfaceWidth / pointSize);
        int randomYPosition = new Random().nextInt(surfaceHeight / pointSize);

        //check if randxpos is even or odd value, we need only even number
        if((randomXPosition % 2) != 0){
            randomXPosition = randomXPosition + 1;
        }

        if((randomYPosition % 2 ) !=0){
            randomYPosition = randomYPosition + 1;
        }

        positionX = (pointSize * randomXPosition) + pointSize;
        positionY = (pointSize * randomYPosition) + pointSize;
    }

    private void moveSnake(){
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //geting head position
                int headPositionX = snakePointsList.get(0).getPositionX();
                int headPositionY = snakePointsList.get(0).getPositionY();

                //check if snake eaten a point
                if(headPositionX == positionX && positionY == headPositionY){
                    // grow snake after eaten point
                    growSnake();
                    //add another random point
                    addPoint();
                }
                switch (movingPosition){
                    case "right":
                        //move snake head to right
                        // other points follow snake head point
                        snakePointsList.get(0).setPositionX(headPositionX + (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;

                    case "left":
                        //move snake head to right
                        // other points follow snake head point
                        snakePointsList.get(0).setPositionX(headPositionX - (pointSize * 2));
                        snakePointsList.get(0).setPositionY(headPositionY);
                        break;

                    case "top":
                        //move snake head to right
                        // other points follow snake head point
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY - (pointSize * 2));
                        break;

                    case "bottom":
                        //move snake head to right
                        // other points follow snake head point
                        snakePointsList.get(0).setPositionX(headPositionX);
                        snakePointsList.get(0).setPositionY(headPositionY + (pointSize * 2));
                        break;
                }

                //check if game over
                if(checkGameOver(headPositionX, headPositionY)){
                    //stop timer / stop moving snake
                    timer.purge();
                    timer.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Your score = " +score);
                    builder.setTitle("Game Over");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //restart game
                            init();
                        }
                    });
                    //rimes runs in background
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }

                else{
                    //lock canvas on surfaceholder to draw
                    canvas = surfaceHolder.lockCanvas();
                    //clear canvas
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    //change snake
                    canvas.drawCircle(snakePointsList.get(0).getPositionX(), snakePointsList.get(0).getPositionY(), pointSize, createPointColor());
                    //draw random point circle
                    canvas.drawCircle(positionX, positionY, pointSize, createPointColor());

                    //follow other points
                    for(int i = 1; i <snakePointsList.size(); i++){

                        int getTempPositionX = snakePointsList.get(i).getPositionX();
                        int getTempPositionY = snakePointsList.get(i).getPositionY();

                        //move points accross the head
                        snakePointsList.get(i).setPositionX(headPositionX);
                        snakePointsList.get(i).setPositionY(headPositionY);
                        canvas.drawCircle(snakePointsList.get(i).getPositionX(), snakePointsList.get(i).getPositionY(), pointSize, createPointColor());

                        headPositionX = getTempPositionX;
                        headPositionY = getTempPositionY;
                    }
                    //unlock canvas to draw on surfaceview
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }, 1000- snakeMovingSpeed, 1000-snakeMovingSpeed);
    }
    private void growSnake(){
        //create new snake point
        SnakePoints snakePoints = new SnakePoints(0,0);
        //add point to the snake tale
        snakePointsList.add(snakePoints);
        //increase score
        score++;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreTv.setText(String.valueOf(score));
            }
        });
    };
    private boolean checkGameOver(int headPositionX, int headPositionY){
        boolean gameOver  = false;

        //check if head touches edge
        if(snakePointsList.get(0).getPositionX() < 0 ||
        snakePointsList.get(0).getPositionY() < 0 ||
                snakePointsList.get(0).getPositionX() >= surfaceView.getWidth() ||
                snakePointsList.get(0).getPositionY() >= surfaceView.getHeight())
        {
            gameOver = true;
        }
        else{
            //check if snake head touche snake itself
            for(int i = 0; i < snakePointsList.size(); i++){

                if(headPositionX == snakePointsList.get(i).getPositionX() &&
                        headPositionY == snakePointsList.get(i).getPositionY())
                {
                    gameOver = true;
                    break;
                }
            }
        }
        return gameOver;
    }
    private Paint createPointColor(){

        //check if color not defined before
        if(pointColor == null){

            pointColor = new Paint();
            pointColor.setColor(snakeColor);
            pointColor.setStyle(Paint.Style.FILL);
            pointColor.setAntiAlias(true);//smoothness
        }

        return pointColor;
    }
}