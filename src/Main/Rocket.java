package Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Rocket implements  Data {
    private final GraphicsContext gc;
    int posX;
    int posY;
    int size;
    boolean exploding;
    boolean destroyed;
    Image img;
    int explosionStep = 0;
    static int score=0;
    static int lives =3;

    public Rocket(int posX, int posY, int size,  Image image,GraphicsContext gc) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        img = image;
        this.gc=gc;
    }

    public Shot shoot() {
        return new Shot(posX + size / 2 -Shot.size, posY - Shot.size,gc);
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
    public int distance(int x1, int y1, int x2, int y2) {//function to calculate the distance between two points
        return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }


    public boolean collide(Chicken other) { //same collision function used with the chicken and shots
        int d = distance(this.posX + size / 2, this.posY + size /2, other.posX + other.size / 2, other.posY + other.size / 2);
        return d < other.size / 2 + this.size / 2;
    }
    public void explosionSound(){
        File explode=new File("music\\explosion.wav");
        Media explodeTheme=new Media(explode.toURI().toString());
        MediaPlayer explodeSound=new MediaPlayer(explodeTheme);
        explodeSound.setAutoPlay(true);

    }
    public void explode() {
        exploding = true;
        explosionStep = -1;
        lives--;
        explosionSound();
    }
}
