package Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Shot implements  Data {
    private final GraphicsContext gc;
    public boolean toRemove;
    int posX;
    int posY;
    int speed = 10;//initial shots speed
    static final int size = 6;//initial size speed

    public Shot(int posX, int posY,GraphicsContext gc) {//constructor
        this.posX = posX;
        this.posY = posY;
        this.gc=gc;
    }

    public void update() {
        posY-=speed;//update the y position of the rocket by using its speed to draw it
    }

    public void draw() {
        gc.setFill(Color.AQUAMARINE);
        if (Rocket.score >=25) {//change the bullet size and color as score increase
            //to make it easier for the player as the speed increase by increasing the score
            gc.setFill(Color.INDIANRED);
            speed = 40;//also update the speed of shots
            gc.fillRect(posX-5, posY-10, size+10, size+30);//update size of shots(increase it)
        } else {//else leave everything as it is
            gc.fillOval(posX, posY, size, size);

        }
    }

    @Override
    public void explosionSound() {
    }

    @Override
    public int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));

    }

    public boolean collide(Rocket Rocket) {
        int distance = distance(this.posX + size / 2, this.posY + size / 2, Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
        //if the chicken is half way through the rocket return true
        return distance < Rocket.size / 2 + size / 2;
    }
}
