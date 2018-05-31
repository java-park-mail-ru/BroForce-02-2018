package application.controllers.gamemechanics.entity;

public class Enemy extends Entity {

    private static final double MAX_SIZE = 0.01;
    private static final double MIN_SIZE = 0.005;

    private static final double D_DIST = 1.35;


    public Enemy() {
        xCoord = Math.random();
        yCoord = Math.random();
        size = Math.random() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE;
        direction = Math.random() * Math.PI * 2;
        speed = size / 10;
    }


    public void move(Player p1, Player p2) {

        if (dist(this, p1) / p1.size < D_DIST &&
                dist(this, p2) / p2.size < D_DIST) {

            double p1Angle = Math.acos((xCoord - p1.xCoord) / dist(this, p1));
            if (Math.asin((xCoord - p1.xCoord) / dist(this, p1)) < 0)
                p1Angle += 2 * Math.PI;
            double p2Angle = Math.acos((xCoord - p2.xCoord) / dist(this, p2));
            if (Math.asin((xCoord - p2.xCoord) / dist(this, p2)) < 0)
                p2Angle += 2 * Math.PI;
            direction = (p1Angle + p2Angle) / 2;

        } else {

            if (dist(this, p1) / p1.size < D_DIST) {
                double p1Angle = Math.acos((xCoord - p1.xCoord) / dist(this, p1));
                if (Math.asin((xCoord - p1.xCoord) / dist(this, p1)) < 0)
                    p1Angle += 2 * Math.PI;
                direction = p1Angle;
            }

            if (dist(this, p2) / p2.size < D_DIST) {
                double p2Angle = Math.acos((xCoord - p2.xCoord) / dist(this, p2));
                if (Math.asin((xCoord - p2.xCoord) / dist(this, p2)) < 0)
                    p2Angle += 2 * Math.PI;
                direction = p2Angle;
            }

        }

        super.move();

    }


    public void reincarnation() {
        xCoord = Math.random();
        yCoord = Math.random();
        size = Math.random() * (MAX_SIZE - MIN_SIZE) + MIN_SIZE;
        direction = Math.random() * Math.PI * 2;
        speed = size / 10;
    }

}
