package edu.duke.ece651.sallysstash.zq24;

import java.util.ArrayList;

/**
 * This class represents shapes.
 */
public abstract class Shape {

    /**
     * This is the color of the Shape.
     */
    protected String color;

    /**
     * This is the y coordinate of the stash.
     */
    protected int upperLeftRow;

    /**
     * This is the x coordinate of the stash.
     */
    protected int upperLeftColumn;

    /**
     * This is the direction of the stash
     */
    protected char direction;

    /**
     * This list contains all the coordinates that consist of this Shape.
     */
    protected ArrayList<int[]> shape;

    /**
     * This list contains all the coordinates that consist of this Shape in String.
     */
    private ArrayList<String> shapeInString;

    public Shape(int row, int col, String color, char direction) {
        this.color = color;
        this.direction = direction;
        this.upperLeftRow = row;
        this.upperLeftColumn = col;
        this.shape = new ArrayList<>();
        this.shapeInString = new ArrayList<>();
    }

    /**
     * This method generates all the coordinates for a particular Shape.
     */

    public abstract void generateShape();

    /**
     * This method verifies if the generated coordinates are within the board
     *
     * @return true if all the coordinates are within the board; false if any
     * coordinate is outside of the board.
     */
    public boolean verifyShape() {
        for (int[] coordinates : this.shape) {
            int row = coordinates[0];
            int col = coordinates[1];
            if (row < 0 || row > 19 || col < 0 || col > 9)
                return false;
        }
        return true;
    }

    /**
     * This method encodes all the coordinates into string after being verified
     * using verifyShape() method.
     */
    public void encodeCoordinates() {
        for (int[] coordinates : this.shape) {
            this.shapeInString.add(coordinates[0] + " " + coordinates[1]);
        }
    }

    /**
     * This method returns all the encoded coordinates.
     *
     * @return list that contains all the encoded coordinates.
     */
    public ArrayList<String> getShapeInString() {
        return this.shapeInString;
    }

    /**
     * This method stores one pair of coordinate inside of an array.
     *
     * @param row x coordinate
     * @param col y coordinate
     * @return an array that stores one pair of coordinate.
     */
    protected int[] generateCoordinates(int row, int col) {
        int[] coordinates = new int[2];
        coordinates[0] = row;
        coordinates[1] = col;
        return coordinates;
    }
}