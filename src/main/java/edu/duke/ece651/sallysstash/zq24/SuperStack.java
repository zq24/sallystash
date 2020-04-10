package edu.duke.ece651.sallysstash.zq24;

public class SuperStack extends Shape {


    public SuperStack(int row, int col, String color, char direction) {
        super(row, col, color, direction);
    }

    /**
     * This method generates all the coordinates for a Super Stack.
     */
    @Override
    public void generateShape() {
        if (this.direction == 'u') {
            this.shape.add(generateCoordinates(this.upperLeftRow, this.upperLeftColumn));
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn - 1));
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn));
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn + 1));
        } else if (this.direction == 'r') {
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn + 1));
            this.shape.add(generateCoordinates(this.upperLeftRow, this.upperLeftColumn));
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn));
            this.shape.add(generateCoordinates(this.upperLeftRow + 2, this.upperLeftColumn));
        } else if (this.direction == 'd') {
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn + 1));
            this.shape.add(generateCoordinates(this.upperLeftRow, this.upperLeftColumn + 2));
            this.shape.add(generateCoordinates(this.upperLeftRow, this.upperLeftColumn + 1));
            this.shape.add(generateCoordinates(this.upperLeftRow, this.upperLeftColumn));
        } else {
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn - 1));
            this.shape.add(generateCoordinates(this.upperLeftRow + 2, this.upperLeftColumn));
            this.shape.add(generateCoordinates(this.upperLeftRow + 1, this.upperLeftColumn));
            this.shape.add(generateCoordinates(this.upperLeftRow, this.upperLeftColumn));
        }
    }
}
