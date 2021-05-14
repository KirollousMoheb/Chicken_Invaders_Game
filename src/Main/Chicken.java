package Main;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Chicken extends  Rocket{
    int SPEED = (score/5)+10;//speed of chickens as a function of score

    public Chicken(int posX, int posY, int size, Image image,GraphicsContext gc) {

        super(posX, posY, size, image,gc);
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
    public void explode() {
        exploding = true;
        explosionStep = -1;

    }

}
