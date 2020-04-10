package edu.duke.ece651.sallysstash.zq24;

/**
 * This class represents rectangular shape stash.
 */
public class Rectangle extends Shape {

    /**
     * This field is the width of the rectangle.
     */

    private int width;

    /**
     * This field is the height of the rectangle.
     */

    private int height;

    public Rectangle(int row, int col, String color, char direction, int width, int height) {
        super(row, col, color, direction);
        this.width = width;
        this.height = height;
    }

    /**
     * This method generates all coordinates for a rectangle based on its
     * width, height and direction.
     */
    @Override
    public void generateShape() {
        if (this.direction == 'h') {
            for (int i = 0; i < this.width; i++) {
                for (int j = 0; j < this.height; j++) {
                    this.shape.add(generateCoordinates(this.upperLeftRow + i,
                            this.upperLeftColumn + j));
                }
            }
        } else {
            for (int i = 0; i < this.height; i++) {
                for (int j = 0; j < this.width; j++) {
                    this.shape.add(generateCoordinates(this.upperLeftRow + i,
                            this.upperLeftColumn + j));
                }
            }
        }
    }
}