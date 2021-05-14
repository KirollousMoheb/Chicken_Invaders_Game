package Main;

import javafx.scene.image.Image;

public interface Data {
     //Sizing constants
     int width = 1000;//screen width
     int height = 800;//screen height
     int rocketSize = 60;// rocket size in pixels
     //Explosion animation Parameters
     int steps = 15;
     int explosionHeight = 120;
     int explosionWidth = 120;
     int explosionImageRows = 3;
     int explosionImageColumns = 3;
     //Restrictions of number of shots and chickens appearing on screen
     int chickensOnScreen = 8;//max number of chickens can exist on screen
     int shotsOnScreen = chickensOnScreen * 2;
     //Images
     Image[] chickensImage = {new Image("file:images/1.png"),new Image("file:images/2.png"),new Image("file:images/1.png"),new Image("file:images/1.png"),new Image("file:images/1.png")};
     Image rocketImage = new Image("file:images/spaceship.png");
     Image explosion = new Image("file:images/dead.png");
     Image logo = new Image("file:images/logo.png");
     Image heart = new Image("file:images/health.png");

     void explosionSound();
     int distance(int x1, int y1, int x2, int y2);
}
