package edu.duke.ece651.sallysstash.zq24;

import java.util.*;

/**
 * This class represents players.
 */
public class Player {

    /**
     * This field represents how many move opportunities this player has.
     */
    private int moveRemainTimes = 3;

    /**
     * This field represents how many scan opportunities this player has.
     */
    private int scanRemainTimes = 3;

    /**
     * This field represents the player's name.
     */
    private String name;

    /**
     * This field represents the player's opponent's name.
     */
    private String opponentName;

    /**
     * This field is the game board for this player.
     */
    private Board board;

    /**
     * This field contains all the coordinates of this player's stashes; a coordinate will be
     * removed from this set if that location is found by opponent.
     */
    private Set<String> coordinatesOfAllShapesInString;
    private List<Shape> allStashes;

    /**
     * This field indicates coordinates corresponding shape.
     */
    private Map<String, Shape> allStashesInfo;

    public Player(String name, String opponentName) {
        this.name = name;
        this.opponentName = opponentName;
        this.board = new Board();
        this.coordinatesOfAllShapesInString = new HashSet<>();
        this.allStashes = new ArrayList<>();
        this.allStashesInfo = new HashMap<>();
    }

    public Board getBoard() {
        return this.board;
    }

    public Set<String> getCoordinatesOfAllShapesInString() {
        return this.coordinatesOfAllShapesInString;
    }

    public List<Shape> getAllStashes() {
        return this.allStashes;
    }

    public Map<String, Shape> getAllStashesInfo() {
        return this.allStashesInfo;
    }

    public String getName() {
        return this.name;
    }

    public String getOpponentName() {
        return this.opponentName;
    }

    public int getMoveRemainTimes() {
        return moveRemainTimes;
    }

    public void setMoveRemainTimes(int moveRemainTimes) {
        this.moveRemainTimes = moveRemainTimes;
    }

    public int getScanRemainTimes() {
        return scanRemainTimes;
    }

    public void setScanRemainTimes(int scanRemainTimes) {
        this.scanRemainTimes = scanRemainTimes;
    }
}