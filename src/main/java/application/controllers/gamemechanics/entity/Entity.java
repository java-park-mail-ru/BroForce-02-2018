package application.controllers.gamemechanics.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Entity {

    protected double xCoord;
    protected double yCoord;
    protected double size;
    protected double direction;
    protected double speed;


    public void move() {
        xCoord += Math.cos(direction) * speed;
        yCoord += Math.sin(direction) * speed;
        if (xCoord > 1) {
            xCoord = 1;
            direction = Math.PI - direction;
        } else if (xCoord < 0) {
            xCoord = 0;
            direction = Math.PI - direction;
        }
        if (yCoord > 1) {
            yCoord = 1;
            direction = 2 * Math.PI - direction;
        } else if (yCoord < 0) {
            yCoord = 0;
            direction = 2 * Math.PI - direction;
        }
        if (direction < 0)
            direction += 2 * Math.PI;
    }


    @JsonProperty("xCoord")
    public double getxCoord() {
        return xCoord;
    }


    @JsonProperty("yCoord")
    public double getyCoord() {
        return yCoord;
    }


    @JsonProperty("size")
    public double getSize() {
        return size;
    }


    static double dist(Entity e1, Entity e2) {
        return (Math.sqrt(Math.pow(e1.xCoord - e2.xCoord, 2) +
                Math.pow(e1.yCoord - e2.yCoord, 2)));
    }

}
