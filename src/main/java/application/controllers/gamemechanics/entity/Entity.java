package application.controllers.gamemechanics.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entity {

    protected double xx;
    protected double yy;
    protected double size;
    protected double direction;
    protected double speed;


    public void move() {
        xx += Math.cos(direction) * speed;
        yy += Math.sin(direction) * speed;
        if (xx > 1) {
            xx = 1;
            direction = Math.PI - direction;
        } else if (xx < 0) {
            xx = 0;
            direction = Math.PI - direction;
        }
        if (yy > 1) {
            yy = 1;
            direction = 2 * Math.PI - direction;
        } else if (yy < 0) {
            yy = 0;
            direction = 2 * Math.PI - direction;
        }
        if (direction < 0) {
            direction += 2 * Math.PI;
        }
    }


    @JsonProperty("xx")
    public double getxx() {
        return xx;
    }


    @JsonProperty("yy")
    public double getyy() {
        return yy;
    }


    @JsonProperty("size")
    public double getSize() {
        return size;
    }


    static double dist(Entity e1, Entity e2) {
        return (Math.sqrt(Math.pow(e1.xx - e2.xx, 2) +
                Math.pow(e1.yy - e2.yy, 2)));
    }

}
