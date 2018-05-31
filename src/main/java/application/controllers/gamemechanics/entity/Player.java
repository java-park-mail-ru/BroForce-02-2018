package application.controllers.gamemechanics.entity;

import org.jetbrains.annotations.NotNull;

public class Player extends Entity {

    private static final double MIN_SIZE = 0.02;
    private static final double MAX_SPEED = 0.013;
    private static final double SPEED_DIF = MAX_SPEED / 20;
    private static final double WIN_DIF = 1.2;
    private static final double ANGLE_STEP = Math.PI / 20;

    private double curDirection;


    public Player(double xxx, double yyy) {
        xx = xxx;
        yy = yyy;
        size = MIN_SIZE;
        direction = 0;
        curDirection = 0;
        speed = 0;
    }


    public boolean eat(Enemy enemy) {
        if (isTouched(enemy)) {
            size += enemy.size;
            enemy.reincarnation();
            return true;
        }
        return false;
    }


    public boolean isEaten(Player enemy) {
        return isTouched(enemy) && size / enemy.size > WIN_DIF;
    }


    @Override
    public void move() {
        super.move();
        speed -= SPEED_DIF;
        if (speed < 0) {
            speed = 0;
        }
        if (curDirection != direction) {
            final double dif = curDirection - direction;
            final int sign1 = dif > 0 ? 1 : -1;
            final int sign2 = Math.abs(dif) > Math.PI ? -1 : 1;
            direction += ANGLE_STEP * sign1 * sign2;
            if (direction > Math.PI * 2) {
                direction -= Math.PI * 2;
            }
            if (direction < 0) {
                direction += Math.PI * 2;
            }
            if (Math.abs(curDirection - direction) < ANGLE_STEP) {
                direction = curDirection;
            }
        }
    }


    public void buttonClick(Direction dir) {
        switch (dir) {
            case UPPP:
                curDirection = Math.PI / 2;
                break;
            case DOWN:
                curDirection = Math.PI * 3 / 2;
                break;
            case LEFT:
                curDirection = Math.PI;
                break;
            case RIGHT:
                curDirection = 0;
                break;
            default:
                break;
        }
        speed += SPEED_DIF * 2;
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
    }


    private boolean isTouched(@NotNull Entity enemy) {
        return (Math.pow(xx - enemy.xx, 2) +
                Math.pow(yy - enemy.yy, 2)) <
                Math.pow(size + enemy.size, 2);
    }

}
