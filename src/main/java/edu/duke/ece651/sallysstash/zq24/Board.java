package edu.duke.ece651.sallysstash.zq24;

/**
 * This class represents the game board.
 */
public class Board {

    /**
     * This field represents the whole game board.
     */
    private Cell[][] board;

    /**
     * This field represents your own board.
     */
    private Cell[][] actualBoard;

    /**
     * This field represents the hit/miss board.
     */
    private Cell[][] hitMissBoard;

    public Board() {
        this.board = new Cell[22][64];
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[0].length; j++) {
                this.board[i][j] = new Cell();
            }
        }
        addColumnTitle();
        addRowTitle();
        addSeparator();
        setActualBoard();
        setHitMissBoard();
    }

    public Cell[][] getActualBoard() {
        return this.actualBoard;
    }

    public Cell[][] getHitMissBoard() {
        return this.hitMissBoard;
    }

    /**
     * This method sets up you own board.
     */
    private void setActualBoard() {
        this.actualBoard = new Cell[20][10];
        for (int i = 0; i < this.actualBoard.length; i++) {
            for (int j = 0; j < this.actualBoard[0].length; j++) {
                this.actualBoard[i][j] = this.board[i + 1][j * 2 + 2];
            }
        }
    }

    /**
     * This method sets up the hit/miss board.
     */
    private void setHitMissBoard() {
        this.hitMissBoard = new Cell[20][10];
        for (int i = 0; i < this.hitMissBoard.length; i++) {
            for (int j = 0; j < this.hitMissBoard[0].length; j++) {
                this.hitMissBoard[i][j] = this.board[i + 1][j * 2 + 42];
            }
        }
    }

    /**
     * This method gives names
     */
    private void addRowTitle() {
        char[] titles = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'
                , 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T'};

        for (int i = 0; i < titles.length; i++) {
            this.board[i + 1][0].setContent(titles[i]);
            this.board[i + 1][22].setContent(titles[i]);

            this.board[i + 1][40].setContent(titles[i]);
            this.board[i + 1][62].setContent(titles[i]);
        }
    }

    private void addColumnTitle() {
        char[] titles = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        for (int i = 0; i < titles.length; i++) {
            this.board[0][i * 2 + 2].setContent(titles[i]);
            this.board[21][i * 2 + 2].setContent(titles[i]);

            this.board[0][i * 2 + 42].setContent(titles[i]);
            this.board[21][i * 2 + 42].setContent(titles[i]);
        }
    }

    private void addSeparator() {
        for (Cell[] cells : this.board) {
            for (int j = 3; j <= 19; j = j + 2) {
                cells[j].setContent('|');
            }
        }
        for (Cell[] cells : this.board) {
            for (int j = 43; j <= 59; j = j + 2) {
                cells[j].setContent('|');
            }
        }
    }

    public void printBoard() {
        System.out.println("------------------------");
        for (Cell[] cells : this.board) {
            for (int j = 0; j < 24; j++) {
                cells[j].display();
            }
            System.out.println();
        }
        System.out.println("------------------------\n");
    }

    public void printAllBoard(String you, String opponent) {
        System.out.println("----------------------------------------------------------------");
        System.out.println(you + "'s turn:");
        System.out.println("  Your tree" + "                             " + opponent + "'s tree");
        for (Cell[] cells : this.board) {
            for (int j = 0; j < this.board[0].length; j++) {
                cells[j].display();
            }
            System.out.println();
        }
        System.out.println("----------------------------------------------------------------\n");
    }
}