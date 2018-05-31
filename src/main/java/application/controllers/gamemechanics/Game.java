package application.controllers.gamemechanics;

import application.controllers.gamemechanics.entity.Direction;
import application.controllers.gamemechanics.entity.Enemy;
import application.controllers.gamemechanics.entity.Player;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class Game {

    private static final int NUMBER_OF_BOTS = 50;

    private Player player1;
    private Player player2;
    private ArrayList<Enemy> bots;
    private int winner;

    public Game() {
        player1 = new Player(1 / 3, 1 / 3);
        player2 = new Player(2 / 3, 2 / 3);
        bots = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_BOTS; i++) {
            bots.add(new Enemy());
        }
        winner = 0;
    }

    public void move() {
        player1.move();
        player2.move();
        for (Enemy b : bots) {
            b.move();
            if (!player1.eat(b)) {
                player2.eat(b);
            }
        }
        if (player1.isEaten(player2)) {
            winner = 1;
        } else if (player2.isEaten(player1)) {
            winner = 2;
        }
    }

    public void buttonClick(Direction dir, int player) {
        switch (player) {
            case 1:
                player1.buttonClick(dir);
                break;
            case 2:
                player2.buttonClick(dir);
                break;
            default:
                break;
        }
    }

    @JsonProperty("Player1")
    public Player getPlayer1() {
        return player1;
    }

    @JsonProperty("Player2")
    public Player getPlayer2() {
        return player2;
    }

    @JsonProperty("bots")
    public ArrayList<Enemy> getBots() {
        return bots;
    }

}
