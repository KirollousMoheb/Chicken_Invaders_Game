package Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
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

public class Main extends Application implements Data {

    //Random variable
    private static final Random RAND = new Random();
    //Music Function
    private static void playMusic(){
         final File themeFile=new File("music\\theme.mp3");
         final Media theme=new Media(themeFile.toURI().toString());
         final MediaPlayer themePlayer=new MediaPlayer(theme);
        themePlayer.setOnEndOfMedia(() -> themePlayer.seek(Duration.ZERO));
        themePlayer.play();

    }
     void playShotSound(){
        File shotFile=new File("music\\shoot.wav");
        Media shot=new Media(shotFile.toURI().toString());
        MediaPlayer shotSound=new MediaPlayer(shot);
        shotSound.setAutoPlay(true);


    }
     void chickenSound(){
        File chickenFile=new File("music\\chickenSound.wav");
        Media chickenTheme=new Media(chickenFile.toURI().toString());
        MediaPlayer chickenSound=new MediaPlayer(chickenTheme);
        chickenSound.setAutoPlay(true);
    }
    @Override
    public void explosionSound() {
    }

    @Override
    public int distance(int x1, int y1, int x2, int y2) {
        return 0;
    }

    boolean dead = false;
    boolean start=false;
    private GraphicsContext gc;
    //Game elements lists
    Rocket player;//instantiate the rocket class
    List<Shot> shots; //list of references shots
    List<Chicken> chickenList;
    private double mouseX;

    //start
    public void start(Stage stage) {

        stage.getIcons().add(chickensImage[1]);
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
                playShotSound();
            }
            if(dead) {
                dead = false;
                anotherChance();

            }
            if (Rocket.lives==0){
                Restart();
                Rocket.lives=3;
            }
        });

        if (start) {
            setup();
        }
        shots = new ArrayList<>();
        chickenList = new ArrayList<>();
        player = new Rocket(width / 2, height - rocketSize, rocketSize, rocketImage,gc);
        Rocket.score = 0;//initialize the score variable
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
        player = new Rocket(width / 2, height - rocketSize, rocketSize, rocketImage,gc);
        IntStream.range(0, chickensOnScreen).mapToObj(i -> this.newChicken()).forEach(chickenList::add);//copied
           }
    private void Restart(){
        shots = new ArrayList<>();
        chickenList = new ArrayList<>();
        player = new Rocket(width / 2, height - rocketSize, rocketSize, rocketImage,gc);
        Rocket.score =0;//initialize the score variable
        IntStream.range(0, chickensOnScreen).mapToObj(i -> this.newChicken()).forEach(chickenList::add);    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //screen initialization
    private void run(GraphicsContext gc) {
        gc.setFill(Color.grayRgb(35));//set background color
        gc.fillRect(0, 0, width, height);//fill all the gui
        gc.setTextAlign(TextAlignment.CENTER);//align text to center
        gc.setFont(Font.font(25));//font size
        gc.setFill(Color.WHITE);//font color
        gc.fillText("Score: " + Rocket.score, 70,  20);
        gc.fillText("Made by Kirollous Moheb",width-150,height-10);

        for (int i = 0; i < Rocket.lives; i++) {
            gc.drawImage(heart, i * 50 + 8, height-40,40,40);
        }
        if(!start){
            gc.drawImage(logo,300,40,400,250);
            gc.setFont(Font.font(30));
            gc.setFill(Color.DARKCYAN);
            gc.fillText("Welcome to Chicken Invaders 1.0 \nYou have Three Lives so be careful \nThe Game gets Faster as your score increase\nYour missiles gets Faster and Larger if your score exceeded 25 \nIf you died Three times your score is reset and you start over \nThere is a small chance that a chicken might gain you one more life \n  \n  \n Click on the mouse to start", width / 2, height /2.5);

        }
        if(Rocket.lives==0) {//print a message if the player lost
            gc.setFont(Font.font(35));
            gc.setFill(Color.WHITE);
            gc.fillText("Game Over \n You Scored: " + Rocket.score + " \n Click the Mouse to play again", width / 2, height /2.5);

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
        //if the shot collided with the target increment the score,explode the target and make the shot disappear

            for (Chicken chicken : chickenList) {
                if (shot.collide(chicken) && !chicken.exploding) {
                    Rocket.score++;
                    float chance = RAND.nextFloat();
                    chicken.explode();

                    if (Rocket.lives < 3) {
                        if (chance <= 0.05f) {
                            Rocket.lives++;
                        }
                    }
                    chickenSound();
                    shot.toRemove = true;
                }
            }
        }
        //for loop to create new chickens in the next round if the chicken is killed or passed down the screen
        for (int i =0; i < chickenList.size(); i++){
            if(chickenList.get(i).destroyed)  {
                chickenList.set(i,newChicken());
            }
        }
        dead = player.destroyed;
        if(dead && Rocket.lives!=0){
            gc.setFont(Font.font(35));
            gc.setFill(Color.WHITE);
            gc.fillText("You died but you have " + Rocket.lives + " more chance(s) \n Click the Mouse to continue", width / 2, height /2.5);
        }

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    Chicken newChicken() {//return a new  chicken with random x position
        return new Chicken(50 + RAND.nextInt(width - 100), 0, rocketSize,chickensImage[RAND.nextInt(chickensImage.length)],gc);
    }
    public static void main(String[] args) {

        playMusic();
        launch();
    }
}