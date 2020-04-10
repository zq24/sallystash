package edu.duke.ece651.sallysstash.zq24;

/**
 * This class represents game board's cell
 */
public class Cell {

    /**
     * This field stores the content of this cell
     */
    private char content = ' ';

    public char getContent() {
        return content;
    }

    public void setContent(char content) {
        this.content = content;
    }

    /**
     * This method prints out the content of this cell
     */
    public void display() {
        System.out.print(this.content);
    }
}