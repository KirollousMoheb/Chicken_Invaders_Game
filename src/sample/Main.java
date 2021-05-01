package sample;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;


public class Main extends Application {

    //variables
    private static final Random RAND = new Random();
    private static final File themeFile=new File("music\\theme.mp3");
    private static final Media theme=new Media(themeFile.toURI().toString());
    private static final MediaPlayer themePlayer=new MediaPlayer(theme);

    File chickenFile=new File("music\\chickenSound.wav");
    Media chickenTheme=new Media(chickenFile.toURI().toString());
    MediaPlayer chickenSound=new MediaPlayer(chickenTheme);

    private static final int width = 1000;//screen width
    private static final int height = 800;//screen height
    private static final int rocketSize = 60;// rocket size in pixels
    static final Image backGround = new Image("file:images/back.png");
    static final Image chickensImage = new Image("file:images/1.png");
    static final Image rocketImage = new Image("file:images/spaceship.png");
    static final Image explosion = new Image("file:images/dead.png");
    static final Image logo = new Image("file:images/logo.png");
    static final Image heart = new Image("file:images/health.png");

    static final int steps = 15;
    static final int explosionHeight = 120;
    static final int explosionWidth = 120;
    static final int explosionImageRows = 3;
    static final int explosionImageColumns = 3;

    final int chickensOnScreen = 8;//max number of chickens can exist on screen
    final int shotsOnScreen = chickensOnScreen * 2;
    boolean dead = false;
    boolean start=false;
    private GraphicsContext gc;

    Rocket player;//instantiate the rocket class
    List<Shot> shots; //list of references shots
    List<Chicken> chickenList;

    private double mouseX;
    private int score;//score variable
     private int lives=3;
    //start///////////////////////////////////////////////////////////////////////////////////////
    public void start(Stage stage)  {
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        canvas.setCursor(Cursor.MOVE);
        canvas.setOnMouseMoved(e -> mouseX = e.getX());

       canvas.setOnMouseClicked(e -> {
           if(!start){
               start=true;
                setup();
           }
            if(shots.size() < shotsOnScreen) {
                shots.add(player.shoot());
            }
            if(dead) {
                dead = false;
                anotherChance();
            }
            if (lives==0){
                Restart();
                lives=3;
            }
        });

        if (start) {
            setup();
        }
        shots = new ArrayList<>();
        chickenList = new ArrayList<>();
        player = new Rocket(width / 2, height - rocketSize, rocketSize, rocketImage);
        score = 0;//initialize the score variable
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.setTitle("Chicken Invaders 1.0");
        stage.show();
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //setup the game
    private void setup() {
        IntStream.range(0, chickensOnScreen).mapToObj(i -> this.newChicken()).forEach(chickenList::add);//copied
    }
    private void anotherChance(){
        shots = new ArrayList<>();
        chickenList = new ArrayList<>();
        player = new Rocket(width / 2, height - rocketSize, rocketSize, rocketImage);
        IntStream.range(0, chickensOnScreen).mapToObj(i -> this.newChicken()).forEach(chickenList::add);//copied
    }
    private void Restart(){
        shots = new ArrayList<>();
        chickenList = new ArrayList<>();
        player = new Rocket(width / 2, height - rocketSize, rocketSize, rocketImage);
        score =0;//initialize the score variable
        IntStream.range(0, chickensOnScreen).mapToObj(i -> this.newChicken()).forEach(chickenList::add);//copied
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
//screen initialization
    private void run(GraphicsContext gc) {
        gc.setFill(Color.grayRgb(35));//set background color
        gc.fillRect(0, 0, width, height);//fill all the gui
        gc.setTextAlign(TextAlignment.CENTER);//align text to center
        gc.setFont(Font.font(25));//font size
        gc.setFill(Color.WHITE);//font color
        gc.fillText("Score: " + score, 50,  20);
        for (int i = 0; i < lives; i++) {
            gc.drawImage(heart, i * 28 + 8, height-30,20,20);
        }
        if(!start){
            gc.setFont(Font.font(35));
            gc.setFill(Color.WHITE);
            gc.drawImage(logo,300,40,400,250);
            gc.fillText("Welcome to Chicken Invaders 1.0 \n Click on the mouse to start", width / 2, height /2.5);


        }
        if(lives==0) {//print a message if the player lost
            gc.setFont(Font.font(35));
            gc.setFill(Color.WHITE);
            gc.fillText("Game Over \n You Scored: " + score + " \n Click the Mouse to play again", width / 2, height /2.5);

        }
        player.update();
        player.draw();
        player.posX = (int) mouseX;

        chickenList.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
            if(player.collide(e) && !player.exploding) {
                player.explode();
            }
        });

        //remove the shot if the toRemove boolean variable is set
        //it is set when a shot collides with a chicken
        for (int i =0; i < shots.size() ; i++) {
            Shot shot = shots.get(i);
            if(shot.posY < 0 || shot.toRemove)  {
                shots.remove(i);
                continue;
            }
            shot.update();
            shot.draw();

//            for (int j = 0; j <chickenList.size(); j++) {
//                //if the shot collided with the target increment the score,explode the target and make the shot disappear
//                if ( shot.colide(chickenList.get(i))&& !chickenList.get(i).exploding) {
//                    score++;
//                    chickenList.get(i).explode();
//                    shot.toRemove = true;
//                }
//            }


        //if the shot collided with the target increment the score,explode the target and make the shot disappear

            for (Chicken chicken : chickenList) {
                if(shot.collide(chicken) && !chicken.exploding) {
                    score++;
                    chicken.explode();
                      chickenFile=new File("music\\chickenSound.wav");
                      chickenTheme=new Media(chickenFile.toURI().toString());
                      chickenSound=new MediaPlayer(chickenTheme);

                    chickenSound.setAutoPlay(true);

                    shot.toRemove = true;
                }
            }
        }
        //for loop to create new chickens in the next round if the chicken is killed or passed down the screen

        for (int i =0; i < chickenList.size(); i++){
            if(chickenList.get(i).destroyed)  {
                chickenList.set(i, newChicken());
            }
        }

        dead = player.destroyed;
        if(dead && lives!=0){
            gc.setFont(Font.font(35));
            gc.setFill(Color.WHITE);
            gc.fillText("You died but you have " + lives + " more live(s) \n Click the Mouse to continue", width / 2, height /2.5);
        }

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public class Rocket {

        int posX;
        int posY;
        int size;
        boolean exploding;
        boolean destroyed;
        Image img;
        int explosionStep = 0;
        public Rocket(int posX, int posY, int size,  Image image) {
            this.posX = posX;
            this.posY = posY;
            this.size = size;
            img = image;
        }

        public Shot shoot() {
            return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
        }

        public void update() {
            if(exploding){
                explosionStep++;
            }
                destroyed = explosionStep > steps;

        }
        public void draw() {
            if (exploding) {
                gc.drawImage(explosion,explosionStep % explosionImageColumns * explosionHeight, (explosionStep / explosionImageRows) * explosionWidth + 1, explosionHeight, explosionWidth,
                        posX, posY, size, size);
            } else {
                gc.drawImage(img, posX, posY, size, size);
            }
        }


        public boolean collide(Rocket other) { //same collision function used with the chicken and shots
          int d = distance(this.posX + size / 2, this.posY + size /2, other.posX + other.size / 2, other.posY + other.size / 2);
          if (d < other.size / 2 + this.size / 2){
              return true;
          }
            return false ;
        }

        public void explode() {
                exploding = true;
                explosionStep = -1;
                lives--;

        }

    }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class Chicken extends Rocket {

        int SPEED = (score/5)+10;//speed of chickens as a function of score

        public Chicken(int posX, int posY, int size, Image image) {
            super(posX, posY, size, image);
        }

        public void update() {
            super.update();
            if(!exploding && !destroyed) {//if the chicken is alive update its position by using its speed
                posY += SPEED;
            }
            if(posY > height){//if the chicken passed down the screen we consider it dead
                destroyed = true;
            }
        }

      @Override
      public void draw() {
          if (exploding) {
              gc.drawImage(explosion,explosionStep % explosionImageColumns * explosionHeight, (explosionStep / explosionImageRows) * explosionWidth + 1, explosionHeight, explosionWidth,
                      posX, posY, size, size);
          } else {
              gc.drawImage(img, posX, posY, size, size);
          }
      }

      @Override
      public void explode() {
          exploding = true;
          explosionStep = -1;
          //   chickenSound.play();////////////////////*************************AAAAAAAAAAAAAAA*****************************************************************


      }
  }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class Shot {

        public boolean toRemove;
        int posX;
        int posY;
        int speed = 10;//initial shots speed
        static final int size = 6;//initial size speed

        public Shot(int posX, int posY) {//constructor
            this.posX = posX;
            this.posY = posY;
        }

        public void update() {
            posY-=speed;//update the y position of the rocket by using its speed to draw it
        }


        public void draw() {
            gc.setFill(Color.RED);
            if (score >=25) {//change the bullet size and color as score increase
                //to make it easier for the player as the speed increase by increasing the score
                gc.setFill(Color.YELLOWGREEN);
                speed = 40;//also update the speed of shots
                gc.fillRect(posX-5, posY-10, size+10, size+30);//update size of shots(increase it)
            } else {//else leave everything as it is
                gc.fillOval(posX, posY, size, size);
            }
        }

        public boolean collide(Rocket Rocket) {
            int distance = distance(this.posX + size / 2, this.posY + size / 2, Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
            if( distance  < Rocket.size/ 2 + size / 2) {//if the chicken is half way through the rocket return true
               return true;
            }
            return false;
        }


    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    Chicken newChicken() {//return a new object chicken with random x position
        return new Chicken(50 + RAND.nextInt(width - 100), 0, rocketSize,chickensImage);
    }
    int distance(int x1, int y1, int x2, int y2) {//function to calculate the distance between two points
        return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));

        ////////////////////////
    }


    public static void main(String[] args) {


        themePlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                themePlayer.seek(Duration.ZERO);
            }
        });
        themePlayer.play();
        launch();
    }
}